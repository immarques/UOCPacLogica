package com.uoc.pac.logica.framework;

public interface Music {
	//Usos basicos sobre un sonido. Iniciar, parar, asignar volumen, etc.
    public void play();

    public void stop();

    public void pause();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();
}
