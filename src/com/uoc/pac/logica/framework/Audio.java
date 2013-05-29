package com.uoc.pac.logica.framework;

public interface Audio {
	//banda sonora, se usara en modo streaming
    public Music newMusic(String filename);

    //efecto, al ser un sonido corto, se cargara el sonido en memoria
    public Sound newSound(String filename);
}
