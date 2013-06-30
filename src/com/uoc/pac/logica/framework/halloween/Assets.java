package com.uoc.pac.logica.framework.halloween;

import com.uoc.pac.logica.framework.Music;
import com.uoc.pac.logica.framework.Sound;
import com.uoc.pac.logica.framework.gl.Animation;
import com.uoc.pac.logica.framework.gl.Font;
import com.uoc.pac.logica.framework.gl.Texture;
import com.uoc.pac.logica.framework.gl.TextureRegion;
import com.uoc.pac.logica.framework.impl.GLGame;

/*
 * Esta clase nos permite tener localizados y referenciadas todas las texturas, animaciones, musica y sonido
 * que necesitemos durante todo el juego.
 */

public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture logo1;
    public static TextureRegion logo1Region;
    public static Texture logo2;
    public static TextureRegion logo2Region;
    
    public static Texture items;        
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion options;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;    
    public static TextureRegion spring;
    public static TextureRegion predator;
    public static TextureRegion castle;
    public static Animation coinAnim;
    public static Animation bobJump;
    public static Animation bobFall;
    public static TextureRegion bobHit;
    public static Animation squirrelFly;
    public static TextureRegion platform;
    public static Animation bat;
    public static Animation brakingPlatform;    
    public static Font font;
    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;
    
    /*
     * Esta funcion se llama una vez al inicio del juego y es la encargada de iniciar todos los 
     * elementos estaticos, los sonidos y las animaciones.
     */
    
    public static void load(GLGame game) {
        background = new Texture(game, "img/background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
  
    	
    	logo1 = new Texture(game, "img/testlogo.png");
        logo1Region = new TextureRegion(logo1, 0, 0, 241, 179);

        logo2 = new Texture(game, "img/iipesp.png");
        logo2Region = new TextureRegion(logo2, 0, 0, 241, 75);
        
        
        items = new Texture(game, "img/items.png");        
        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        options = new TextureRegion(items, 321, 352, 191, 36);
        highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
        logo = new TextureRegion(items, 0, 352, 274, 142);
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 64, 64, 64, 64);
        
        spring = new TextureRegion(items, 128, 0, 32, 32);
        castle = new TextureRegion(items, 128, 64, 64, 64);
        coinAnim = new Animation(0.2f,                                 
                                 new TextureRegion(items, 128, 32, 32, 32),
                                 new TextureRegion(items, 160, 32, 32, 32),
                                 new TextureRegion(items, 192, 32, 32, 32),
                                 new TextureRegion(items, 160, 32, 32, 32));
        bobJump = new Animation(0.2f,
                                new TextureRegion(items, 0, 128, 32, 32),
                                new TextureRegion(items, 32, 128, 32, 32));
        bat = new Animation(0.2f, 
        						new TextureRegion(items, 380, 470, 40, 40),
        						new TextureRegion(items, 420, 470, 40, 40));
        predator = new TextureRegion(items, 460, 470, 45, 45);
        bobFall = new Animation(0.2f,
                                new TextureRegion(items, 64, 128, 32, 32),
                                new TextureRegion(items, 96, 128, 32, 32));
        bobHit = new TextureRegion(items, 128, 128, 32, 32);
        squirrelFly = new Animation(0.2f, 
                                    new TextureRegion(items, 0, 160, 32, 32),
                                    new TextureRegion(items, 32, 160, 32, 32));
        platform = new TextureRegion(items, 64, 160, 64, 16);
        brakingPlatform = new Animation(0.2f,
                                     new TextureRegion(items, 64, 160, 64, 16),
                                     new TextureRegion(items, 64, 176, 64, 16),
                                     new TextureRegion(items, 64, 192, 64, 16),
                                     new TextureRegion(items, 64, 208, 64, 16));
        
        //Se crea una instancia de la clase font donde se implementa la logica del uso del atlas como texto.
        font = new Font(items, 224, 0, 16, 16, 20);
        
        //Se cargan los sonidos y la muscia
        music = game.getAudio().newMusic("music/music.mp3");
        //Se habilita el modo bucle.
        music.setLooping(true);
        //Se asigna el volumen
        music.setVolume(0.5f);
        //Solo se reproducira si el usuario no ha desabilitado previamente el sonido en la clase de configuracion.
        if(Settings.soundEnabled)
            music.play();
        //Se cargan los demas sonidos.
        jumpSound = game.getAudio().newSound("music/jump.ogg");
        highJumpSound = game.getAudio().newSound("music/highjump.ogg");
        hitSound = game.getAudio().newSound("music/hit.ogg");
        coinSound = game.getAudio().newSound("music/coin.ogg");
        clickSound = game.getAudio().newSound("music/click.ogg");       
    }       
    /*
     * Este metodo es el que nos permite recargar las texturas. Esto es debido que el contexto de OpenGL ES se
     * pierde cuando se detiene la aplicacion.
     * Tambien nos permite seguir con la reproducción de la musica en caso que se active el sonido.
     */
    
    public static void reload() {
        background.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }
    
    //Este metodo nos permite reproducir audio en diferentes partes del juego, sin tener que comprobar constantemente la configuracion.
    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
