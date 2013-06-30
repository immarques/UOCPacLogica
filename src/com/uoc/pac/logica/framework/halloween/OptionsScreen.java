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
 * En esta pantalla podremos elegir diferentes opciones que se almacenaran
 * en el fichero de propiedades.
 */
public class OptionsScreen extends GLState {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle limitedMapBounds;
    Rectangle delimitedBounds;
    Rectangle inmortalBounds;
    Rectangle dificultadBounds;
    Rectangle backBounds;
    
    Vector2 touchPoint;   
    
    public OptionsScreen(Game game) {
        super(game);
        
        guiCam = new Camera2D(glGraphics, 320, 480);
        soundBounds = new Rectangle(320 - 64, 0, 64, 64);
        backBounds = new Rectangle(0, 0, 64, 64);
        limitedMapBounds = new Rectangle(30 -10, 140 -5, 300, 36);
        delimitedBounds = new Rectangle(30 -10, 182 -5, 300, 36);
        inmortalBounds = new Rectangle(30 -10, 222 -5, 300, 36);
        dificultadBounds = new Rectangle(30 -10, 110 -5, 300, 36);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
    }

    @Override
    public void update(float deltaTime) {
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    	game.getInput().getKeyEvents();
    	int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_UP) {
                if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) 
                        Assets.music.play();
                    else
                        Assets.music.pause();
                    return;
                }
                if(OverlapTester.pointInRectangle(limitedMapBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.limitedScreen = !Settings.limitedScreen;
                    return;
                }
                if(OverlapTester.pointInRectangle(delimitedBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.viewDelimited = !Settings.viewDelimited;
                    return;
                }
                if(OverlapTester.pointInRectangle(inmortalBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.inmortal = !Settings.inmortal;
                    return;
                }
                if(OverlapTester.pointInRectangle(dificultadBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.dificultad = !Settings.dificultad;
                    return;
                }
            }
        }
    }

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
        Assets.font.drawText(batcher, Settings.limitedScreen?"Limitar borde: ON":"limitar borde: OFF", 30, 140);
        Assets.font.drawText(batcher, Settings.viewDelimited?"Limitadores: ON":"Limitadores: OFF", 30, 182);
        Assets.font.drawText(batcher, Settings.inmortal?"Inmortal: ON":"Inmortal: OFF", 30, 222);
        Assets.font.drawText(batcher, Settings.dificultad?"Dificultad:DIFICIL":"DIFICULTAD:FACIL", 30, 110);
        batcher.drawSprite(32, 32, 64, 64, Assets.arrow);
        batcher.drawSprite(288, 32, 64, 64, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
                
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void resume() {
    }
    
    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }
    
    @Override
    public void dispose() {
    }
    
}
