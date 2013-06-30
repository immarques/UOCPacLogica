package com.uoc.pac.logica.framework.halloween;


import java.util.List;



import android.graphics.Point;

/** 
 * Esta clase representa el conjunto de murcielagos y obstaculos.
 * Y controla la cordinacion de los movimientos de los murcielagos, 
 * así como la deteccion de obstaculos y separacion logica.
 */
class Flock {
    // This is the list of birds in the map
    private List<Bird> bats;
    private Bird bob;
    
    // This is the range that birds will separate to avoid an object
    static int SeparationRange;
    // This is the range at which birds can detect an object
    static int DetectionRange;

    /**
     * This is the flock contstructor.
     */
    Flock(List<Bird> bats2, Bird bob)
    {
        this.bats = bats2;
        this.bob = bob;
    }
   
    /**
     * Tells each Bird in the Vector to move in the direction of the generalHeading.
     *
     * @return  A vector of birds that were removed. This is used to update the
     *           sliders for the number of birds still on the map.
     */
     public void update(float deltaTime) {
        int movingBird = 0;

        // Loop through each bird to move.
        // Done this way, because the vector can change in the loop
        while (movingBird < bats.size()) {
            
            Bird bird = (Bird)bats.get(movingBird);
            bird.update(generalHeading(bird),deltaTime);
            movingBird++;
        }
    }
    
    /**
     * Add two points together, scaling both according to their weight
     *
     * @param  p1 The first point to add
     * @param  w1 The weight of the first poitn
     * @param  p2 The second point to add
     * @param  w2 The weight of the second point
     */
    public static Point sumPoints(Point p1, double w1, Point p2, double w2) {
        return new Point((int)(w1*p1.x + w2*p2.x), (int)(w1*p1.y + w2*p2.y));
    }
 
    /**
     * Distance from the top left of the map to a given point
     *
     * @param  p The point to measure the distance to.
     */
    public static double sizeOfPoint(Point p) {
        return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
    }
 
    /**
     * Normalize a point.
     *
     * @param  p The point to normalize.
     * @param  n The normalization value.
     */
    public static Point normalisePoint(Point p, double n) {
        if (sizeOfPoint(p) == 0.0) {
            return p;
        }
        else {
            double weight = n / sizeOfPoint(p);
            return new Point((int)(p.x * weight), (int)(p.y * weight));
        }
    }
    
    /**
     * Sometimes, two birds are closer together if you go off one edge of the map
     * and return on the other. This function will convert the "other point" into
     * a point that closest to the point p, even if it is off the map.
     *
     * @param  p The point to measure the distance to.
     * @param  otherPoint The point to measure the distance from.
     */
    public static Point closestLocation(Point p, Point otherPoint) {
        int dX = Math.abs(otherPoint.x - p.x);
        int dY = Math.abs(otherPoint.y - p.y);
        int x = otherPoint.x;
        int y = otherPoint.y;
        
        // now see if the distance between birds is closer if going off one
        // side of the map and onto the other.
        if ( Math.abs(World.WORLD_WIDTH - otherPoint.x + p.x) < dX ) {
            dX = (int) (World.WORLD_WIDTH - otherPoint.x + p.x);
            x = (int) (otherPoint.x - World.WORLD_WIDTH);
        }
        if ( Math.abs(World.WORLD_WIDTH - p.x + otherPoint.x) < dX ) {
            dX = (int) (World.WORLD_WIDTH - p.x + otherPoint.x);
            x = (int) (otherPoint.x + World.WORLD_WIDTH);
        }
        
        if ( Math.abs(World.WORLD_HEIGHT - otherPoint.y + p.y) < dY ) {
            dY = (int) (World.WORLD_HEIGHT - otherPoint.y + p.y);
            y = (int) (otherPoint.y - World.WORLD_HEIGHT);
        }
        if ( Math.abs(World.WORLD_HEIGHT - p.y + otherPoint.y) < dY ) {
            dY = (int) (World.WORLD_HEIGHT - p.y + otherPoint.y);
            y = (int) (otherPoint.y + World.WORLD_HEIGHT);
        }
        
        return new Point( x, y );
    }
 
    /** 
     * This function determines the direction a Bird will turn towards for this step.
     * The bird looks at every other bird and obstacle on the map to see if it is
     * within the detection range. Predator will move toward birds. Birds will
     * avoid birds of a different color and all obstacles.
     *
     * @param  bird The bird to get the heading for
     */
    private int generalHeading(Bird bird) {
        
        // Sum the location of all birds that are within our detection range
        Point target = new Point(0, 0);
        // Number of birds that are within the detection range
        int numBirds = 0;
        
        // Loop thorough each bird to see if it is within our detection range
        for (int i=0; i < bats.size(); i++) {
            Bird otherBird = (Bird)bats.get(i);
            Point otherLocation = closestLocation(bird.getLocation(), otherBird.getLocation());
            Point bobLocation = closestLocation(bird.getLocation(), bob.getLocation());

            int distance = bird.getDistance(otherLocation);
            int distance2 = bird.getDistance(bobLocation);
            
            if(distance2 < distance){
            	distance = distance2;
            	otherBird = bob;
            	otherLocation = bobLocation;
            }
            
            if (!bird.equals(otherBird) && distance > 0 && distance <= DetectionRange)
            {
  
                if (bird.getColor().equals(otherBird.getColor())) { // birds of same color attract
                    Point align = new Point((int)(100 * Math.cos(otherBird.getTheta() * Math.PI/180)),
                    (int)(-100 * Math.sin(otherBird.getTheta() * Math.PI/180)));
                    align = normalisePoint(align, 100); // alignment weight is 100
                    boolean tooClose = (distance < SeparationRange);
                    double weight = 200.0;
                    if (tooClose) {
                        weight *= Math.pow(1 - (double) distance / SeparationRange, 2);
                    }
                    else {
                        weight *= - Math.pow((double)( distance - SeparationRange ) / ( DetectionRange - SeparationRange ), 2);
                    }
                    Point attract = sumPoints(otherLocation, -1.0, bird.getLocation(), 1.0);
                    attract = normalisePoint(attract, weight); // weight is variable
                    Point dist = sumPoints(align, 1.0, attract, 1.0);
                    dist = normalisePoint(dist, 100); // final weight is 100

                    target = sumPoints(target, 1.0, dist, 1.0);
                    
                }
                numBirds++;
            }
        }
         // if no birds are close enough to detect, continue moving is same direction.
        if (numBirds == 0) {
            return bird.getTheta();
        }
        else { // average target points and add to position
            target = sumPoints(bird.getLocation(), 1.0, target, 1/(double)numBirds);
        }
        
        // Turn the target location into a direction in degrees
        int targetTheta = (int)(180/Math.PI * Math.atan2(bird.getLocation().y - target.y, target.x - bird.getLocation().x));
        // Make sure the angle is 0-360
        return (targetTheta + 360) % 360; // angle for Bird to steer towards
    }
}
