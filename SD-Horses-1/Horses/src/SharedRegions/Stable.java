
package SharedRegions;

import InterveningEntities.*;
import MainProgram.LoggerInterface;
import MainProgram.SimulationParameters;
import genclass.GenericIO;

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
    private LoggerInterface logger;
    
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
     * Horses go to the stable and wait for the calling of the broker.
     * Changes the state of the broker to AT_THE_STABLE and waits for the broker
     * to call the horses to the race number of the horse, or to entertain the guests.
     */
    public synchronized void proceedToStable(){
        
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        if(horse.getHorseState() != HorseJockeyState.AT_THE_STABLE){
            horse.setHorseState(HorseJockeyState.AT_THE_STABLE);
            logger.updateHorseState(horse.getID(), HorseJockeyState.AT_THE_STABLE);
        }
        
        horsesAtStable += 1;
        
        /** 
         * Wait for the broker to call horses to his race.
         */
        while((!brokerCalled || raceNumber != horse.getRaceNumber()) && !entertainGuests){
            try{
                wait();
            }
            catch (InterruptedException e) {
                GenericIO.writelnString("proceedToStable - One thread of HorseJockey was interrupted.");
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
     * Call all horses to come out of the stable to the paddock.
     */
    public synchronized void summonHorsesToPaddock(){
        brokerCalled = true;
        notifyAll();
    }
    
    /**
     * Broker calls the horses to entertain the guests.
     * Changes the state of the broker to PLAYING_HOST_AT_THE_BAR.
     */
    public synchronized void entertainTheGuests(){
        Broker broker = (Broker)Thread.currentThread();
        
        /**
         * Only entertain the guests when all horses are on stable.
         */
        while(horsesAtStable<SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES){
            try {
                wait();
            } catch (InterruptedException ex) {
                GenericIO.writelnString("entertainTheGuests -Broker thread was interrupted.");
                System.exit(1);
            }
        }
        
        broker.setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
        logger.updateTrack(-1, -1);
        logger.updateBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
        entertainGuests = true;
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
     * Set the number of the next race.
     * @param raceNumber number of the next race
     */
    public synchronized void setRaceNumber(int raceNumber){
        this.raceNumber = raceNumber;
    }
    
    
    /**
     * Set the current logger
     * @param logger Logger to be used for the entity
     */
    public synchronized void setLogger(LoggerInterface logger){
        this.logger = logger;
    }
    
}
