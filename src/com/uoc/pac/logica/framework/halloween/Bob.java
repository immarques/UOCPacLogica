package com.uoc.pac.logica.framework.halloween;

/*
 * Esta clase representa a nuestro personaje principal, debido que tiene que
 * moverse por toda la pantalla extiende de DynamicGameObject.
 */

public class Bob extends Bird{
	//Inicializamos los estados en los que puede encontrarse el objecto.
    public static final int BOB_STATE_JUMP = 0;
    public static final int BOB_STATE_FALL = 1;
    public static final int BOB_STATE_HIT = 2;
    //Velocidad de salto, solo se aplica en el eje Y
    public static final float BOB_JUMP_VELOCITY = 11;
    //Velocidad de desplazamiento solo se aplica al eje X
    public static final float BOB_MOVE_VELOCITY = 20;
    //Definimos el tamaño
    public static final float BOB_WIDTH = 0.8f;
    public static final float BOB_HEIGHT = 0.8f;
    
    int state;
    float stateTime;    

   /*
    * Inicializamos la posición donde sera representado, llamando al constructor
    * de la clase padre, inicializamos el estado y el tiempo de vida del objeto.
    */
    public Bob(float x, float y) {
        super(x, y, BOB_WIDTH, BOB_HEIGHT);
        state = BOB_STATE_FALL;
        stateTime = 0; 
        color = "green";
    	location.x = (int) x;
    	location.y = (int) y;
    }

    public void update(float deltaTime) { 
    	//Actualizamos posion, velocidad y contorno.
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        location.x = (int) position.x;
        location.y = (int) position.y;
        
        //En los proximos 2 bloques controlamos los estados del personaje
        if(velocity.y > 0 && state != BOB_STATE_HIT) {
            if(state != BOB_STATE_JUMP) {
                state = BOB_STATE_JUMP;
                stateTime = 0;
            }
        }
        
        if(velocity.y < 0 && state != BOB_STATE_HIT) {
            if(state != BOB_STATE_FALL) {
                state = BOB_STATE_FALL;
                stateTime = 0;
            }
        }
        
        /*
         * A diferencia de otros objetos con el objeto que representa al personaje
         * principal le permitimos cruzar los bordes de la pantalla, apareciendo por
         * el otro extremo.
         * 
         * Según las opciones elegidas podra o no cruzar los limites de la pantalla.
         */
        if(!Settings.limitedScreen){
        	if(position.x < 0)
        		position.x = World.WORLD_WIDTH;
        	if(position.x > World.WORLD_WIDTH)
        		position.x = 0;
        }else{
        	if(position.x < 0)
        		position.x = 0;
        	if(position.x > World.WORLD_WIDTH)
        		position.x = World.WORLD_WIDTH;
        	
        }
        //Actualizamos el ciclo de vida
        stateTime += deltaTime;
    }
    
    /*
     * Este metodo es llamado en el caso de que el personaje colisione con una 
     * ardilla, en este caso reiniciamos la velocidad y el ciclo de vida y 
     * establecemos el estado del objeto.
     */
    public void hitSquirrel() {
        velocity.set(0,0);
        state = BOB_STATE_HIT;        
        stateTime = 0;
    }

    /*
     * Este metodo es llamado en el caso de que el personaje colisione con una 
     * plataforma, en este caso reiniciamos el ciclo de vida, establecemos la 
     * velocidad y el nuevo estado del objeto.
     */
    public void hitPlatform() {
        velocity.y = BOB_JUMP_VELOCITY;
        state = BOB_STATE_JUMP;
        stateTime = 0;
    }

    /*
     * Este metodo es llamado en el caso de que el personaje colisione con una 
     * propulsor, en este caso reiniciamos el ciclo de vida, establecemos la 
     * velocidad de salto mas la proporcionada por el propulsor y el nuevo estado 
     * del objeto.
     */
    public void hitSpring() {
        velocity.y = BOB_JUMP_VELOCITY * 1.5f;
        state = BOB_STATE_JUMP;
        stateTime = 0;   
    }
}
