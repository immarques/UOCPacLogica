package com.uoc.pac.logica.framework;

public interface Game {
	
	/*
	 * Configura la pantalla, la interfaz de usuario y recoge los eventos que 
	 * podamos recibir.
	 * 
	 * Inicia el bucle principal.
	 * 
	 * Controla la pantalla actual, que debemos actualizar.
	 * 
	 * Permite el acceso a los diferentes modulos que hemos desarrollado.
	 * 
	 */
	
	//permitimos el acceso a los diferentes modulos.
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    //Asignamos una pantalla activa
    public void setScreen(State screen);

    //Devuelve la pantalla (estado) activa
    public State getCurrentScreen();

    public State getStartScreen();
}