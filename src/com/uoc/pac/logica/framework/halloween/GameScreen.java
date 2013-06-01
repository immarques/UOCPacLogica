package com.uoc.pac.logica.framework.halloween;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.uoc.pac.logica.framework.Game;
import com.uoc.pac.logica.framework.Input.TouchEvent;
import com.uoc.pac.logica.framework.gl.Camera2D;
import com.uoc.pac.logica.framework.gl.FPSCounter;
import com.uoc.pac.logica.framework.gl.SpriteBatcher;
import com.uoc.pac.logica.framework.impl.GLState;
import com.uoc.pac.logica.framework.math.OverlapTester;
import com.uoc.pac.logica.framework.math.Rectangle;
import com.uoc.pac.logica.framework.math.Vector2;
import com.uoc.pac.logica.framework.halloween.World.WorldListener;

/*
 * En esta pantalla (estado) es donde se presentara el mundo generado al jugador
 * y se le permitira interactuar con el.
 */
public class GameScreen extends GLState {
	
	/*
	 * Tenemos los estados:
	 * READY 	 -> Estado inicial donde se inicializa todos los recursos
	 * RUNNING 	 -> Estado "jugando" donde el jugador interacciona con el juego
	 * PAUSED 	 -> Estado "pausado" El juego esta parado y se alamacenan los datos
	 * LEVEL_END -> Estado "hemos ganado" el juego ha terminado porque hemos llegado a la meta
	 * GAME_OVER -> Estado "hemos perdido" el juego termina porque no hemos conseguido nuestro objetivo.
	 */
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;    
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    int lastScore;
    String scoreString;    
    FPSCounter fpsCounter;
    
    /*
     * En el constructor lo unico a destacar es el worldListener que se han
     * sobreescrito los metodos de la interficie para poder reproducir los 
     * diversos sonidos. 
     */
    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            @Override
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

            @Override
            public void highJump() {
                Assets.playSound(Assets.highJumpSound);
            }

            @Override
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            @Override
            public void coin() {
                Assets.playSound(Assets.coinSound);
            }                      
        };
        //construimos el mundo pasandole el worldlistener anterior.
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        
        //inicializamos los rectangulos que nos permitiran detectar las pulsaciones
        pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        lastScore = 0;
        scoreString = "score: 0";
        //Nos permite controlar la velocidad del juego
        fpsCounter = new FPSCounter();
    }

    /*
     * (non-Javadoc)
     * @see com.uoc.pac.logica.framework.State#update(float)
     * 
     * Nos permite actualizar cualquier elemento a partir de las entradas del 
     * usuario. 
     */
	@Override
	public void update(float deltaTime) {
	    //Controlamos el valor del deltaTime a una decima de segundo.
		if(deltaTime > 0.1f)
	        deltaTime = 0.1f;
	    
		//Segun el estado actual actualizamos el juego.
	    switch(state) {
	    case GAME_READY:
	        updateReady();
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    case GAME_LEVEL_END:
	        updateLevelEnd();
	        break;
	    case GAME_OVER:
	        updateGameOver();
	        break;
	    }
	}
	
	/*
	 * El juego esta en pausa hasta que el usuario interaccione con la pantalla, una vez
	 * detectada una accion actualizamos el estado del juego.
	 */
	private void updateReady() {
	    if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
	    }
	}
	
	/*
	 * Durante el periodo de juego en la actualizacion se realizan diferentes comprobaciones
	 * y acciones.
	 */
	private void updateRunning(float deltaTime) {
		//Capturamos los eventos de usuario con la pantalla
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        //comprobamos que el evento actual es de levantar el dedo.
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        
	        //inicializamos el punto de pantalla
	        touchPoint.set(event.x, event.y);
	        //lo convertimos en coordenadas del juego.
	        guiCam.touchToWorld(touchPoint);
	        
	        //Comprobamos si la pulsacion ha sido sobre el icono de pausa.
	        if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
	        	//En caso afirmativo cambiamos el estado del juego y reproducimos un sonido.
	            Assets.playSound(Assets.clickSound);
	            state = GAME_PAUSED;
	            return;
	        }            
	    }
	    
	    //Actualizamos la puntuacion actual
	    world.update(deltaTime, game.getInput().getAccelX());
	    if(world.score != lastScore) {
	        lastScore = world.score;
	        scoreString = "" + lastScore;
	    }
	    //Comprobamos si hemos llegado al destino, para finalizar el juego.
	    if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
	        state = GAME_LEVEL_END;        
	    }
	    //Comprobamos si el estado del mundo es game over, para finalizarel juego.
	    if(world.state == World.WORLD_STATE_GAME_OVER) {
	        state = GAME_OVER;
	        //Si la puntuacion obtenida es mejor a las existintes lo indicamos
	        if(lastScore >= Settings.highscores[4]) 
	            scoreString = "new highscore: " + lastScore;
	        else
	            scoreString = "score: " + lastScore;
	        //Actualizamos las puntuaciones y guardamos el fichero de propiedades.
	        Settings.addScore(lastScore);
	        Settings.save(game.getFileIO());
	    }
	}
	
	/*
	 * En el metodo actualizar pausa, simplemente controlamos si las pulsaciones del usuario
	 * han interaccionado con nuestro menu de opciones.
	 */
	private void updatePaused() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        
	        if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            state = GAME_RUNNING;
	            return;
	        }
	        
	        if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            game.setScreen(new MainMenuScreen(game));
	            return;
	        
	        }
	    }
	}
	
	/*
	 * En el caso de haber llegado al destino final, actualizamos el juego
	 * creando un nuevo mundo, asignando la puntuacion obtenida hasta el momento al
	 * nuevo mundo y inicializando el estado a READY.
	 */
	private void updateLevelEnd() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {                   
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        world = new World(worldListener);
	        renderer = new WorldRenderer(glGraphics, batcher, world);
	        world.score = lastScore;
	        state = GAME_READY;
	    }
	}
	
	/*
	 * En este metodo devolvemos el juego a la pantalla de menu principal.
	 */
	private void updateGameOver() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {                   
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        game.setScreen(new MainMenuScreen(game));
	    }
	}

	/*
	 * (non-Javadoc)
	 * @see com.uoc.pac.logica.framework.State#present(float)
	 * 
	 * Mediante el metodo present, representamos los diferentes estados del juego
	 * al usuario.
	 */
	
	@Override
	public void present(float deltaTime) {
	    GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    
	    //En primer lugar representamos el mundo sobre el que jugaremos.
	    renderer.render();
	    
	    guiCam.setViewportAndMatrices();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    batcher.beginBatch(Assets.items);
	    
	    //En segundo lugar representaremos los elementos de la UI segun 
	    //el estado en el que nos encontremos.
	    
	    switch(state) {
	    case GAME_READY:
	        presentReady();
	        break;
	    case GAME_RUNNING:
	        presentRunning();
	        break;
	    case GAME_PAUSED:
	        presentPaused();
	        break;
	    case GAME_LEVEL_END:
	        presentLevelEnd();
	        break;
	    case GAME_OVER:
	        presentGameOver();
	        break;
	    }
	    batcher.endBatch();
	    gl.glDisable(GL10.GL_BLEND);
	    fpsCounter.logFrame();
	}
	
	//Representamos el titulo de Ready?
	private void presentReady() {
	    batcher.drawSprite(160, 240, 192, 32, Assets.ready);
	}
	
	//Presentamos el boton de pausa y la puntuacion acumulada hasta el momento.
	private void presentRunning() {
	    batcher.drawSprite(320 - 32, 480 - 32, 64, 64, Assets.pause);
	    Assets.font.drawText(batcher, scoreString, 16, 480-20);
	}
	
	//Presentamos el menu de pausa y la puntuacion obtenida.
	private void presentPaused() {        
	    batcher.drawSprite(160, 240, 256, 96, Assets.pauseMenu);
	    Assets.font.drawText(batcher, scoreString, 16, 480-20);
	}
	
	//Al alcanzar el objetivo presentamos un breve texto.
	private void presentLevelEnd() {
	    String topText = "No es suficiente...";
	    String bottomText = "sigue buscando!";
	    float topWidth = Assets.font.glyphWidth * topText.length();
	    float bottomWidth = Assets.font.glyphWidth * bottomText.length();
	    Assets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
	    Assets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
	}
	
	//Al perder la partida presentamos la imagen de game over y la puntuacion final obtenida.
	private void presentGameOver() {
	    batcher.drawSprite(160, 240, 160, 96, Assets.gameOver);        
	    float scoreWidth = Assets.font.glyphWidth * scoreString.length();
	    Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-20);
	}

	/*
	 * (non-Javadoc)
	 * @see com.uoc.pac.logica.framework.State#pause()
	 * 
	 * Cambiamos el estado del juego.
	 */
    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
