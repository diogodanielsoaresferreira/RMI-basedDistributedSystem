
package MainPackage;

import EntitiesState.HorseJockeyState;
import HelperClasses.SerializableHorse;
import Stubs.*;

/**
 * HorseJockey thread:
 * Implements the life-cycle of the horse/jockey and stores his internal variables
 * and current state.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class HorseJockey extends Thread {

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
     * Implements the life-cycle of the HorseJockey.
     */
    @Override
    public void run(){
        
        /** 
         * Waits for the calling of the broker.
         */
        st.proceedToStable(id, raceNumber, state);
        
        /**
         * Horse added to new race.
         */
        rt.addHorseToRace(new SerializableHorse(id, distance, iterations, agility));
        
        /** 
         * If all horses are on paddock, wake up the spectators.
         */
        cc.proceedToPaddock(id);

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
        pd.proceedToStartLine(id);
        
        /** 
         * Waits for the race to start.
         */
        rt.proceedToStartLine(id);

        /** 
         * Horse is not on the start line anymore.
         */
        pd.horseOutOfStartLine();
        
        /** 
         * While the finish line has not been crossed, horse makes
         * moves to the finish line.
         */
        while(!rt.hasFinishedLineBeenCrossed(distance)){
            rt.makeAMove(id);
        }
        
        /**
         * Signalize that horse is in the finish line and notify the broker that
         * the horse ended the race.
         */
        cc.atTheFinishLine(id);
        
        
        /**
         * Horse goes to stable.
         */
        st.proceedToStable(id, raceNumber, state);
        
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
     * Set the distance travelled by the horse
     * @param distance distance travelled
     */
    public void setDistance(int distance){
        this.distance = distance;
    }
    
    /**
     * Set the number of iterations made by the horse.
     * @param iterations number of iterations
     */
    public void setIteration(int iterations){
        this.iterations = iterations;
    }
    
}
