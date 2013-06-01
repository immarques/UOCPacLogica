package com.uoc.pac.logica.framework;

import com.uoc.pac.logica.framework.math.Vector2;

public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;
    
    /*
     * Esta clase nos permite crear objetos dinamicos, simplemente extiende
     * de la clase GameObject, pero agregamos dos vectores, velocidad y aceleracion.
     */
    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
    }
}
