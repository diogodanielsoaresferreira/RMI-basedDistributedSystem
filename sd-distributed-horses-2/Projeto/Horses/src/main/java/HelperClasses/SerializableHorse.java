
package HelperClasses;

import java.io.Serializable;

/**
 * Class that contains the attributes of a horse to be serialized.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class SerializableHorse implements Serializable, Comparable<SerializableHorse>  {
    
    /**
     * Serial version of the class. Format used is 
     * Class-Group-Number of project (XXYYZZ)
     */
    private static final long serialVersionUID = 040302L;
    
    /**
     * ID of the horse.
     */
    private final int id;
    
    /**
     * Distance travelled by the horse.
     */
    private int distance;
    
    /**
     * Iterations made by the horse.
     */
    private int iterations;
    
    /**
     * Horse agility.
     */
    private int agility;
    
    /**
     * Constructor of the horse
     * @param id id of the horse
     * @param distance distance travelled
     * @param iterations iterations done on the race
     * @param agility agility
     */
    public SerializableHorse(int id, int distance, int iterations, int agility){
        this.id = id;
        this.distance = distance;
        this.iterations = iterations;
        this.agility = agility;
    }
    
    /**
     * Get the id of the horse.
     * @return id of the horse
     */
    public int getID() {
        return id;
    }
    
    /**
     * Get the distance travelled by the horse
     * @return distance travelled
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Set the distance travelled by the horse
     * @param distance distance travelled
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    /**
     * Get the iterations that a horse has made on a race
     * @return number of iterations
     */
    public int getIterations() {
        return iterations;
    }
    
    /**
     * Set the number of iterations of the horse
     * @param iterations number of iterations
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    
    /**
     * Get the agility of a horse
     * @return horse agility
     */
    public int getAgility() {
        return agility;
    }
    
    /**
     * Set the agility of the horse
     * @param agility horse agility
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }
    
    /**
     * Compare two horses in the finish line.
     * @param horse horse/jockey to be compared
     * @return if the argument horse has a lower race position than the object, returns -1;
     * if the argument horse has a higher race position than the object, returns 1;
     * if the argument horse has the same race position than the object, returns 0.
     */
    @Override
    public int compareTo(SerializableHorse horse) {
        if(this.iterations<horse.iterations){
            return -1;
        }
        else if(this.iterations>horse.iterations){
            return 1;
        }
        
        if(this.distance>horse.distance)
            return -1;
        else if(this.distance<horse.distance)
            return 1;
        
        return 0;
    }

    
}
