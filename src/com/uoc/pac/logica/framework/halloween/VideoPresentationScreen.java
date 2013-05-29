package com.uoc.pac.logica.framework.halloween;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.net.Uri;
import android.widget.VideoView;

import com.uoc.pac.logica.framework.Game;
import com.uoc.pac.logica.framework.Input.TouchEvent;
import com.uoc.pac.logica.framework.gl.Camera2D;
import com.uoc.pac.logica.framework.gl.SpriteBatcher;
import com.uoc.pac.logica.framework.impl.GLState;
import com.uoc.pac.logica.framework.math.OverlapTester;
import com.uoc.pac.logica.framework.math.Rectangle;
import com.uoc.pac.logica.framework.math.Vector2;

public class VideoPresentationScreen extends GLState {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;

    public VideoPresentationScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 100);
        touchPoint = new Vector2();   
        
    }       

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);                        
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
        
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();  
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.logo1);                  
        batcher.drawSprite(150, 200, 241, 179, Assets.logo1Region);
        batcher.endBatch();
        if(deltaTime > 10){
            game.setScreen(new MainMenuScreen(game));
            return;       	
        }
        
        
    }
    
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
