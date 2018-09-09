
package InterveningEntities;

import MainProgram.SimulationParameters;
import SharedRegions.*;

/**
 * HorseJockey thread:
 * Implements the life-cycle of the horse/jockey and stores his internal variables
 * and current state.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class HorseJockey extends Thread implements Comparable<HorseJockey> {

    /**
     * Identifier of the HorseJockey.
     */
    private final int id;
    
    /**
     * Agility of the HorseJockey.
     */
    private final int agility;
    
    /**
     * Number of the race when the horse/jockey will run.
     */
    private final int raceNumber;
    
    /**
     * Distance travelled by the horse.
     */
    private int distance = 0;
    
    /**
     * Number of iterations that the horse needed to reach the finish line.
     */
    private int iterations = 0;
    
    /**
     * Current state of the HorseJockey.
     */
    private HorseJockeyState state;
    
    /**
     * Instance of Paddock.
     */
    private final Paddock pd;
    
    /**
     * Instance of the Racing Track.
     */
    private final RacingTrack rt;
    
    /**
     * Instance of the Control Center.
     */
    private final ControlCenter cc;
    
    /**
     * Instance of the Stable.
     */
    private final Stable st;
    
    /**
     * HorseJockey constructor
     * @param id ID of the HorseJockey
     * @param raceNumber number of the race in which the horse/jockey will participate
     * @param pd instance of Paddock
     * @param rt instance of Racing Track
     * @param st instance of Stable
     * @param cc instance of Control Center
     */
    public HorseJockey(int id, int raceNumber, Paddock pd, RacingTrack rt, Stable st, ControlCenter cc){
        this.id = id;
        this.raceNumber = raceNumber;
        this.agility = SimulationParameters.HORSES_AGILITY[id];
        this.state = HorseJockeyState.AT_THE_STABLE;
        this.pd = pd;
        this.rt = rt;
        this.st = st;
        this.cc = cc;
    }
    
    /**
     * Implements the life-cycle of the HorseJockey
     */
    @Override
    public void run(){
        
        /** 
         * Waits for the calling of the broker.
         */
        st.proceedToStable();
        
        /**
         * Horse added to new race.
         */
        rt.addHorseToRace();
        
        /** 
         * If all horses are on paddock, wake up the spectators.
         */
        cc.proceedToPaddock();

        /** 
         * Waits for all spectators to reach the paddock.
         */
        pd.proceedToPaddock();
        
        /** 
         * Horse is not on the paddock anymore.
         */
        cc.horseOutOfPaddock();
        
        /**
         * Horse is no longer on the stable.
         */
        st.horseOutOfStable();
        
        /** 
         * If all horses are on the start line, notify the spectators.
         */
        pd.proceedToStartLine();
        
        /** 
         * Waits for the race to start.
         */
        rt.proceedToStartLine();

        /** 
         * Horse is not on the start line anymore.
         */
        pd.horseOutOfStartLine();
        
        /** 
         * While the finish line has not been crossed, horse makes
         * moves to the finish line.
         */
        while(!rt.hasFinishedLineBeenCrossed()){
            rt.makeAMove();
        }
        
        /**
         * Signalize that horse is in the finish line and notify the broker that
         * the horse ended the race.
         */
        cc.atTheFinishLine();
        
        
        /**
         * Horse goes to stable.
         */
        st.proceedToStable();
        
    }
    
    /**
     * Get the HorseJockey state
     * 
     * @return HorseJockey state
     */
    public HorseJockeyState getHorseState(){
        return this.state;
    }
    
    /**
     * Get the ID of the HorseJockey
     * 
     * @return ID of the HorseJockey
     */
    public int getID(){
        return this.id;
    }
    
    /**
     * Get the agility of the horse
     * @return agility of the horse
     */
    public int getAgility() {
        return agility;
    }
    
    /**
     * Set the horse/jockey state
     * @param state new state of horse/jockey
     */
    public void setHorseState(HorseJockeyState state){
        this.state = state;
    }
    
    /**
     * Get the number of the race in which the horse will run.
     * @return number of the race
     */
    public int getRaceNumber(){
        return raceNumber;
    }
    
    /**
     * Get the distance travelled by the horse.
     * @return distance travelled by the horse
     */
    public int getDistance(){
        return this.distance;
    }
    
    /**
     * Get the number of iterations that the horse/jockey needed to finish the race.
     * @return number of iterations
     */
    public int getNumberOfIterations(){
        return this.iterations;
    }
    
    /**
     * Adds distance travelled by the horse to the current distance.
     * @param distance distance travelled by the horse to be incremented
     */
    public void addDistance(int distance){
        this.distance += distance;
    }
    
    /**
     * Adds a number of iterations to the iterations of the horse.
     * @param iterations number of iterations to be added to the horse
     */
    public void addIteration(int iterations){
        this.iterations += iterations;
    }
    
    /**
     * Compare two horses in the finish line.
     * @param horse horse/jockey to be compared
     * @return if the argument horse has a lower race position than the object, returns -1;
     * if the argument horse has a higher race position than the object, returns 1;
     * if the argument horse has the same race position than the object, returns 0.
     */
    @Override
    public int compareTo(HorseJockey horse) {
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
