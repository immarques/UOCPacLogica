package com.uoc.pac.logica.framework.halloween;

import javax.microedition.khronos.opengles.GL10;

import com.uoc.pac.logica.framework.gl.Animation;
import com.uoc.pac.logica.framework.gl.Camera2D;
import com.uoc.pac.logica.framework.gl.SpriteBatcher;
import com.uoc.pac.logica.framework.gl.TextureRegion;
import com.uoc.pac.logica.framework.impl.GLGraphics;

/*
 * Esta clase nos permite dibujar el mundo, le pasamos el spriteBatcher y la logica 
 * del mundo.
 */
public class WorldRenderer {
	//Difinimos el tamaño del trozo de mundo a visualizar
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;    
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;    
    
    //El constructor recibe una instancia de GLGrphics, del SpriteBatcher y del Mundo.
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    /*
     * El metodo render divide la representacion en dos bloques:
     * -> el fondo del juego
     * -> los demas elementos
     */
    public void render() {
    	//Actualizamos la posicion de la camara, referente a la posicion del personaje principal.
        if(world.bob.position.y > cam.position.y )
            cam.position.y = world.bob.position.y;
        cam.setViewportAndMatrices();
        //renderizamos el fondo
        renderBackground();
        //renderizamos los objetos
        renderObjects();        
    }
    
    /*
     * Aqui renderizamos el fondo, lo que hacemos es dibujar siempre la imagen de fondo,
     * sin scroll siempre ocupa el maximo de la pantalla.
     */
    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegion);
        batcher.endBatch();
    }
    
    /*
     * Mediante este metodo representamos los demas objetos, aquí utilizamos la mezcla (alpha) 
     * ya que los sprite de los objetos tienen fondo transparente.
     * 
     * Representamos los objetos en un solo lote, ya que en la definicion del SpriteBatcher (en GameScreen.class)
     * hemos limitado el lote a 1000.
     * Cada objeto se representa mediante su propio metodo.
     * 
     * Hay que tener en cuenta que la representacion se hace a una escala 32 a 1, no tiene por que ser la misma definida
     * en la logica de colisiones.
     * 
     * Bob ->  1x1
     * Calabazas, ardillas, propulsores -> 1x1
     * Castillo -> 2x2
     * Plataformas -> 2x0.5
     */
    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        renderBob();
        renderPlatforms();
        renderItems();
        renderSquirrels();
        renderCastle();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }
    
    /*
     * Este metodo es el responsable de representar al protagonista basandonos en su propio estado y en el estado del tiempo.
     */
    private void renderBob() {
        TextureRegion keyFrame;
        switch(world.bob.state) {
        case Bob.BOB_STATE_FALL:
            keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_JUMP:
            keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_HIT:
        default:
            keyFrame = Assets.bobHit;                       
        }
        
        //Nos basamos en la velocidad en el eje X para determinar el lado hacia el que se mueve y asi representar la imagen correcta.
        float side = world.bob.velocity.x < 0? -1: 1;        
        batcher.drawSprite(world.bob.position.x, world.bob.position.y, side * 1, 1, keyFrame);        
    }
    
    /*
     * Nos recorremos la lista de platadormas definidas en nuestro mundo logico y las representamos
     */
    private void renderPlatforms() {
        int len = world.platforms.size();
        for(int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            TextureRegion keyFrame = Assets.platform;
            //En el caso que la plataforma este en estado de pulverizarse, cambiamos el keyframe de su representacion
            if(platform.state == Platform.PLATFORM_STATE_PULVERIZING) {                
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
            }            
            //Representamos la plataforma en cuestion                       
            batcher.drawSprite(platform.position.x, platform.position.y, 
                               2, 0.5f, keyFrame);            
        }
    }
    /*
     * Mediante este metodo se representan los propulsores y las calabazas
     * Para los propulsores simplemente representamos la textura correspondiente,
     * en el caso de las calabazas seleccionamos el keyframe a representar segun
     * el tiempo de vida de la calabaza.
     */
    private void renderItems() {
        int len = world.springs.size();
        for(int i = 0; i < len; i++) {
            Spring spring = world.springs.get(i);            
            batcher.drawSprite(spring.position.x, spring.position.y, 1, 1, Assets.spring);
        }
        
        len = world.coins.size();
        for(int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(coin.position.x, coin.position.y, 1, 1, keyFrame);
        }
    }
    
    /*
     * Representamos las ardillas en funcion del tiempo de vida. Hay que tener en cuenta que solo disponemos
     * de la representacion hacia la izquierda en el atlas y por eso hay que comprobar la dirección actual
     * del eje x.
     */
    private void renderSquirrels() {
        int len = world.squirrels.size();
        for(int i = 0; i < len; i++) {
            Squirrel squirrel = world.squirrels.get(i);
            TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
            float side = squirrel.velocity.x < 0?-1:1;
            batcher.drawSprite(squirrel.position.x, squirrel.position.y, side * 1, 1, keyFrame);
        }
    }
    
    /*
     * Este metodo simplemente dibuja el objeto final que debemos alcanzar, en nuestro caso
     * una calabaza enorme :O mediante la textura correspondiente.
     */
    private void renderCastle() {
        Castle castle = world.castle;
        batcher.drawSprite(castle.position.x, castle.position.y, 2, 2, Assets.castle);
    }
}
