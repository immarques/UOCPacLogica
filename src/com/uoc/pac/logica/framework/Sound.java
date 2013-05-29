package com.uoc.pac.logica.framework;

public interface Sound {
	//Al tratarse de un efecto (sonido de corta duracion), 
	//simplemente tiene que reproducirse.
    public void play(float volume);

    public void dispose();
}
