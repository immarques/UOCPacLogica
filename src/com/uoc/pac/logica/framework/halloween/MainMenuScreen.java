package com.uoc.pac.logica.framework.halloween;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.uoc.pac.logica.framework.Game;
import com.uoc.pac.logica.framework.Input.TouchEvent;
import com.uoc.pac.logica.framework.gl.Camera2D;
import com.uoc.pac.logica.framework.gl.SpriteBatcher;
import com.uoc.pac.logica.framework.impl.GLState;
import com.uoc.pac.logica.framework.math.OverlapTester;
import com.uoc.pac.logica.framework.math.Rectangle;
import com.uoc.pac.logica.framework.math.Vector2;

/*
 * En ella se renderiza el menu principal, se carga el fondo de pantalla y los 
 * elementos de la UI y se queda esperando que se interaccione con alguno.
 * 
 * Interacciones posibles:
 *  -> Activar/desactivar sonido
 *  -> empezar el juego
 *  -> Ir a pantalla de puntuaciones
 *  -> Ayuda (ver el manual de ayuda)
 *  
 *  Esta clase derivade GLstate así podemos acceder de forma más facil a 
 *  glGraphics.
 */
public class MainMenuScreen extends GLState {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;

    public MainMenuScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 100);
        
        /*
         * Utilizamos rectangulos para identificar si el usuario a interaccionado
         * con algun elemento de la UI.
         */
        soundBounds = new Rectangle(0, 0, 64, 64);
        playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
        highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
        helpBounds = new Rectangle(160 - 150, 200 - 18 - 36, 300, 36);
        
        /*
         * Este vector es necesario para tranformar el toque de la pantalla a cordenadas
         * del mundo.
         */
        touchPoint = new Vector2();               
    }       

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        
        //Nos recorremos las pulsaciones de pantalla 
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);                        
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                
                //Esto nos permite transformar la pulsacion en una cordenada del mundo
                guiCam.touchToWorld(touchPoint);
                
                /*
                 * Ahora ya podemos comporbar si alguno de los elementos de la UI han
                 * sido activados. 
                 */
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    //reproducimos un sonido
                	Assets.playSound(Assets.clickSound);
                	//Hacemos la transicion a la pantalla correspondiente.
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HighscoresScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HelpScreen(game));
                    return;
                }
                /*
                 * En el caso del boton de sonido a parte de activar/deactivar el sonido
                 * tambien modificamos la variable de configuracion almacenada.
                 */
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) 
                        Assets.music.play();
                    else
                        Assets.music.pause();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.uoc.pac.logica.framework.State#present(float)
     * En este metodo limpiamos la pantalla, establecemos las matrices de
     * proyección y pintamos los elementos de la interfaz de usuario.
     * 
     * Debido que las imagenes tiene fondo transparente nos permite 
     * combinarlas.
     * 
     */
    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        //Fondo
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);               
        
        //UI
        batcher.beginBatch(Assets.items);                 

        batcher.drawSprite(160, 480 - 10 - 71, 274, 142, Assets.logo);
        batcher.drawSprite(160, 200, 300, 110, Assets.mainMenu);
        batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
                
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    /*
     * (non-Javadoc)
     * @see com.uoc.pac.logica.framework.State#pause()
     * Aquí simplemente nos aseguramos de guardar los ajustes.
     */
    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        
    }       

    @Override
    public void dispose() {        
    }
}
