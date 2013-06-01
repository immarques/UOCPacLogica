package com.uoc.pac.logica.framework.halloween;

import com.uoc.pac.logica.framework.DynamicGameObject;

/*
 * Este objeto representa las plataformas que nos permiten saltar
 * sin caernos, debido a que algunas de ellas podran desplazarse
 * extiende de DynamicGameObject.
 */

public class Platform extends DynamicGameObject {
	//Definimos el tamaño
    public static final float PLATFORM_WIDTH = 2;
    public static final float PLATFORM_HEIGHT = 0.5f;
    //Diferenciamos el tipo de plataforma estatica o dinamica
    public static final int PLATFORM_TYPE_STATIC = 0;
    public static final int PLATFORM_TYPE_MOVING = 1;
    /*
     * En este caso es necesario tener diferentes estados, para poder
     * obtener la representacion correcta en cada momento.
     */
    public static final int PLATFORM_STATE_NORMAL = 0;
    public static final int PLATFORM_STATE_PULVERIZING = 1;
    public static final float PLATFORM_PULVERIZE_TIME = 0.2f * 4;
    
    //Definimos la velocidad del movimiento.
    public static final float PLATFORM_VELOCITY = 2;
    
    int type;
    int state;
    float stateTime;
    
    /*
     * En el constructor, definimos la posicion, el tamaño, el estado y el tipo.
     */
    public Platform(int type, float x, float y) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.type = type;
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if(type == PLATFORM_TYPE_MOVING) {
            velocity.x = PLATFORM_VELOCITY;
        }
    }
    
    public void update(float deltaTime) {
    	//En el caso de ser una plataforma en movimiento, se hacen las mismas
    	//comprobaciones que en el caso de las ardillas.
        if(type == PLATFORM_TYPE_MOVING) {
            position.add(velocity.x * deltaTime, 0);
            bounds.lowerLeft.set(position).sub(PLATFORM_WIDTH / 2, PLATFORM_HEIGHT / 2);
            
            if(position.x < PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x;
                position.x = PLATFORM_WIDTH / 2;
            }
            if(position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x;
                position.x = World.WORLD_WIDTH - PLATFORM_WIDTH / 2;
            }            
        }
        //Se actualiza el tiempo de vida        
        stateTime += deltaTime;        
    }
    
    //Este metodo nos permite cambiar entre el estado normal y el estado roto
    //Se inicializan las variables de velocidad y tiempo de vida.
    //Este metodo es llamado cuando se detecta una colicion entre objetos.
    public void pulverize() {
        state = PLATFORM_STATE_PULVERIZING;
        stateTime = 0;
        velocity.x = 0;
    }
}
