package com.uoc.pac.logica.framework.halloween;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.uoc.pac.logica.framework.State;
import com.uoc.pac.logica.framework.impl.GLGame;

public class Halloween extends GLGame {
    boolean firstTimeCreate = true;
    
    @Override
    public State getStartScreen() {
        return new VideoPresentationScreen(this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }
}