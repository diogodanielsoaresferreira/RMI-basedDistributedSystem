
package SharedRegion;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import MainPackage.MainProgram;
import MainPackage.SimulationParameters;
import Stubs.Logger;

/**
 * Where the horses can be before or after a race, waiting for another race.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Stable {
    
    /**
     * Logger class for debugging.
     */
    private final Logger logger;
    
    /**
     * Checks if the broker called for the horses.
     */
    private boolean brokerCalled = false;
    
    /**
     * Checks if horses will entertain the guests.
     */
    private boolean entertainGuests = false;
    
    /**
     * Number of horses on the stable.
     */
    private int horsesAtStable = 0;
    
    /**
     * ID of the race to be raced.
     */
    private int raceNumber = -1;
    
    /**
     * Stable constructor
     * @param logger logger stub for debugging operations
     */
    public Stable(Logger logger){
        this.logger = logger;
    }
    
    /**
     * Set the number of the next race.
     * @param raceNumber number of the next race
     */
    public synchronized void setRaceNumber(int raceNumber){
        this.raceNumber = raceNumber;
    }
    
    /**
     * Call all horses to come out of the stable to the paddock.
     */
    public synchronized void summonHorsesToPaddock(){
        brokerCalled = true;
        notifyAll();
    }
    
    /**
     * Broker calls the horses to entertain the guests.
     */
    public synchronized void entertainTheGuests(){
        
        /**
         * Only entertain the guests when all horses are on stable.
         */
        while(horsesAtStable<SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES){
            try {
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        
        logger.updateTrack(-1, -1);
        logger.updateBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
        entertainGuests = true;
        notifyAll();
    }
    
    /**
     * Horses go to the stable and wait for the calling of the broker.
     * Waits for the broker to call the horses to the race number of
     * the horse, or to entertain the guests.
     * @param id horse id
     * @param raceNumber current race number
     * @param state horse state
     */
    public synchronized void proceedToStable(int id, int raceNumber, HorseJockeyState state){
        
        if(state != HorseJockeyState.AT_THE_STABLE){
            logger.updateHorseState(id, HorseJockeyState.AT_THE_STABLE);
        }
        
        horsesAtStable += 1;
        
        /** 
         * Wait for the broker to call horses to his race.
         */
        while((!brokerCalled || raceNumber != this.raceNumber) && !entertainGuests){
            try{
                wait();
            }
            catch (InterruptedException e) {
                System.exit(1);
            }
        }
        
        /**
         * If all horses are on stable, notify the broker to entertain the guests,
         * if it is the last race.
         */
        if(horsesAtStable>=SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES)
            notifyAll();
    }
    
    /**
     * Horse came out of stable.
     */
    public synchronized void horseOutOfStable(){
        horsesAtStable -= 1;
        
        /**
         * If there are no horses at stable, disable the calling of the broker.
         */
        if(horsesAtStable == 0){
            brokerCalled = false;
        }
    }
    
    /**
     * Termination of stable service.
     */
    public synchronized void serviceEnd(){
        MainProgram.serviceEnd = true;
    }
}

