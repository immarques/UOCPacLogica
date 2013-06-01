package com.uoc.pac.logica.framework.impl;

import com.uoc.pac.logica.framework.Game;
import com.uoc.pac.logica.framework.State;

public abstract class GLState extends State {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLState(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
