package com.uoc.pac.logica.framework;

public abstract class State {
    protected final Game game;

    //El constructor recibe la instancia del juego como parametro.
    public State(Game game) {
        this.game = game;
    }

    //Actualizamos el juego
    public abstract void update(float deltaTime);

    //Presentamos la pantalla al usuario
    public abstract void present(float deltaTime);

    //Se para el uso de la pantalla
    public abstract void pause();

    //Se devuelve el control al usuario
    public abstract void resume();

    //Se libera memoria.
    public abstract void dispose();
}
