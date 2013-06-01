package com.uoc.pac.logica.framework.halloween;

import com.uoc.pac.logica.framework.GameObject;

/*
 * Este objeto representa la calabaza final que hay que encontrar, como es un objeto estatico
 * extiende de GameObject y solo tenemos que determinar la posicion y el tamaño.
 */

public class Castle extends GameObject {
    public static float CASTLE_WIDTH = 1.7f;
    public static float CASTLE_HEIGHT = 1.7f;

    public Castle(float x, float y) {
        super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
    }

}
