package com.uoc.pac.logica.framework;

import com.uoc.pac.logica.framework.math.Rectangle;
import com.uoc.pac.logica.framework.math.Vector2;

/*
 * Todos los objetos del juego necesitan tener una posicion (el centro del mismo)
 * Y un contorno que nos permitira detectar colisiones, en este caso usamos una
 * figura simple el rectangulo.
 */

public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;
    
    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
    }
}
