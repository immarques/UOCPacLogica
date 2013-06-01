package com.uoc.pac.logica.framework.halloween;

import com.uoc.pac.logica.framework.DynamicGameObject;

/*
 * Las ardillas voladoras, son un objeto en movimiento (concretamente de
 * izquierda a derecha y viceversa) debido a esto extienden de DynamicGameObject
 * 
 * 
 */
public class Squirrel extends DynamicGameObject {
	
	//Definimos tamaño y velocidad
    public static final float SQUIRREL_WIDTH = 1;
    public static final float SQUIRREL_HEIGHT = 0.6f;
    public static final float SQUIRREL_VELOCITY = 3f;
    
    //Iniciamos el tiempo de vida, para determinar su estado
    float stateTime = 0;
    
    public Squirrel(float x, float y) {
        super(x, y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT);
        velocity.set(SQUIRREL_VELOCITY, 0);
    }
    
    public void update(float deltaTime) {
    	//Actualizamos la posicion
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        //Determinamos la delimitacion actual
        bounds.lowerLeft.set(position).sub(SQUIRREL_WIDTH / 2, SQUIRREL_HEIGHT / 2);
        
        
        if(position.x < SQUIRREL_WIDTH / 2 ) {
            position.x = SQUIRREL_WIDTH / 2;
            velocity.x = SQUIRREL_VELOCITY;
        }
      //Comprovamos si se ha llegado a la esquina de la pantalla, en dicho
      //caso se invierte la velocidad, de esta forma se desplaza hacia el lado
      //opuesto.
        if(position.x > World.WORLD_WIDTH - SQUIRREL_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - SQUIRREL_WIDTH / 2;
            velocity.x = -SQUIRREL_VELOCITY;
        }
        //Actualizamos el ciclo de vida.
        stateTime += deltaTime;
    }
}
