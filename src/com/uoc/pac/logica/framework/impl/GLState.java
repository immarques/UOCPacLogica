package com.uoc.pac.logica.framework.impl;

import com.uoc.pac.logica.framework.Game;
import com.uoc.pac.logica.framework.State;

/*
 * Todas las pantallas (estados) del juego derivan de esta clase.
 * As’ podemos instanciar siempre glGame y glGraphics.
 */

public abstract class GLState extends State {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLState(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
