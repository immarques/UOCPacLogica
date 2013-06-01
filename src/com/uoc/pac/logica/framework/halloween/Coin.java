package com.uoc.pac.logica.framework.halloween;

import com.uoc.pac.logica.framework.GameObject;

/*
 * Esta clase nos permite dibujar nuestras calabazas, aunque su representacion
 * sea una animacion la clase tambien extiende de GameObject porque es un objeto
 * estatico.
 */
public class Coin extends GameObject {
	//Tamaño que representa el porcentaje de celda que ocupara.
    public static final float COIN_WIDTH = 0.5f;
    public static final float COIN_HEIGHT = 0.8f;
    //La puntuacion que obtenemos al coger una de las calabazas.
    public static final int COIN_SCORE = 10;

    //Iniciamos el tiempo de vida
    float stateTime;
    public Coin(float x, float y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT);
        stateTime = 0;
    }
    
    public void update(float deltaTime) {
    	//Actualizamos el tiempo de vida
        stateTime += deltaTime;
    }
}
