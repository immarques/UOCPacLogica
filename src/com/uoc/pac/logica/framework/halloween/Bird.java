package com.uoc.pac.logica.framework.halloween;


import com.uoc.pac.logica.framework.DynamicGameObject;

import android.graphics.Point;

/**
 * Esta clase representa los murcielagos de un color, los murcielagos
 * intentaran viajar juntos a los de su misma especie y alejarse de los demas
 */
class Bird extends DynamicGameObject {

    float stateTime;
    
	//El rango de separacion entre murcielagos
    protected static int SeparationRange;
    
    //El rango mediante el cual puede detectar otros murcielados o obstaculos.
    protected static int DetectionRange;

    //Parametro que permite enseñar los rangos de separacion
    static boolean showRanges = false;
    
    //El actual punto de murcielago
    protected Point location = new Point(0,0);
    
    //La direccion que esta llevando actualmente
    private int currentTheta;
    
    //El color del murcielago
    protected String color;
    
    //La velocidad de los murcielagos. Esta puede variar.
    private double currentSpeed = 5f;
    
    //Maxima inclinacion en grados para variar la direccion
    private int maxTurnTheta;
    
    public static final float BIRD_WIDTH = 1f;
    public static final float BIRD_HEIGHT = 1f;

    /**
     * El constructor
     *
     * @param  x coordinate X
     * @param  y coordinate Y
     * @param  theta angulo del a direccion inicial
     * @param  birdColor color asignado
     */
     Bird(int x, int y, int theta, String birdColor) {
    	super(x, y, BIRD_WIDTH, BIRD_HEIGHT);
    	location.x = x;
    	location.y = y;
        currentTheta = theta;
        color = birdColor;
        this.stateTime = 0;
    }

    /**
     * El constructor asignando un punto aleatorio y una direccion aleatoria.
     *
     * @param  birdColor The color of the bird.
     */
    Bird(String birdColor) {
        // call the other constructor.
        this((int)(Math.random() * World.WORLD_WIDTH), (int)(Math.random() * (World.WORLD_HEIGHT/2)), (int)(Math.random() * 360), birdColor);
    }
    
    public Bird(float x, float y, float bobWidth, float bobHeight) {
    	super(x, y, bobWidth, bobHeight);
	}

	/**
     * Causes the bird to attempt to face a new direction.
     * Based on maxTurnTheta, the bird may not be able to complete the turn.
     *
     * @param  newHeading The direction in degrees that the bird should turn toward.
     */
    public void update(int newHeading, float deltaTime) {
    	
        int left = (newHeading - currentTheta + 360) % 360;
        int right = (currentTheta - newHeading + 360) % 360;

        int thetaChange = 0;
        if (left < right) {
            thetaChange = Math.min(maxTurnTheta, left);
        }
        else {
            thetaChange = -Math.min(maxTurnTheta, right);
        }

        currentTheta = (currentTheta + thetaChange + 360) % 360;

        position.add((float) (currentSpeed * deltaTime * Math.cos(currentTheta * Math.PI/180)), (float) (currentSpeed * deltaTime * Math.sin(currentTheta * Math.PI/180)));

    	if(position.x < 0)
    		position.x = World.WORLD_WIDTH;
    	if(position.x > World.WORLD_WIDTH)
    		position.x =  0;
    	
    	if(position.y < 0)
    		position.y = World.WORLD_HEIGHT;
    	if(position.y > World.WORLD_HEIGHT)
    		position.y = 0;

        location.x = (int) position.x;
        location.y = (int) position.y;
  
        stateTime += deltaTime;  
    }
 
    /**
     * Obtenemos la distancia entre este murcielago y otro.
     */
    public int getDistance(Bird otherBird) {
        int dX = (int) (otherBird.getLocation().x - location.x);
        int dY = (int) (otherBird.getLocation().y - location.y);
        
        return (int)Math.sqrt( Math.pow( dX, 2 ) + Math.pow( dY, 2 ));
    }
    
    /**
     * Obtenemos la distancia entre este murcielago y un punto.
     */
    public int getDistance(Point p) {
        int dX = (int) (p.x - location.x);
        int dY = (int) (p.y - location.y);
        
        return (int)Math.sqrt( Math.pow( dX, 2 ) + Math.pow( dY, 2 ));
    }
 
    /**
     * Devuelve la actual direccion del murcielago
     */
    public int getMaxTurnTheta() {
        return maxTurnTheta;
    }
    
    /**
     * Asignamos la maxima capacidad de giro en cada movimiento del murcielago
     */
    public void setMaxTurnTheta(int theta)
    {
        maxTurnTheta = theta;
    }
    
    /**
     * Devuelve la actual direccion del murcielago
     */
    public int getTheta() {
        return currentTheta;
    }
 
    /**
     * Devuelve el punto donde se encuentra el murcielago
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Devuelve la velocidad actual
     */
    public void setSpeed( double speed ) {
        currentSpeed = speed;
    }
    
    /**
     * Devuelve el color asignado
     */
    public String getColor() {
        return color;
    }
}
