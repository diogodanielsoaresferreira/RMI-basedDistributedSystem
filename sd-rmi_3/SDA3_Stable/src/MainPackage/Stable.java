
package MainPackage;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import java.rmi.RemoteException;
import Interfaces.LoggerInterface;
import Interfaces.StableInterface;

/**
 * Where the horses can be before or after a race, waiting for another race.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Stable implements StableInterface {
    
    /**
     * Logger class for debugging.
     */
    private final LoggerInterface loggerInt;
    
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
    public Stable(LoggerInterface logger){
        this.loggerInt = logger;
    }
    
    /**
     * Set the number of the next race.
     * @param raceNumber number of the next race
     */
    @Override
    public synchronized void setRaceNumber(int raceNumber)  throws RemoteException{
        this.raceNumber = raceNumber;
    }
    
    /**
     * Call all horses to come out of the stable to the paddock.
     */
    @Override
    public synchronized void summonHorsesToPaddock() throws RemoteException{
        brokerCalled = true;
        notifyAll();
    }
    
    /**
     * Broker calls the horses to entertain the guests.
     */
    @Override
    public synchronized void entertainTheGuests() throws RemoteException{
        
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
        
        
        loggerInt.updateTrack(-1, -1);
        loggerInt.updateBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
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
    @Override
    public synchronized void proceedToStable(int id, int raceNumber, HorseJockeyState state) throws RemoteException{
        
        if(state != HorseJockeyState.AT_THE_STABLE){
            loggerInt.updateHorseState(id, HorseJockeyState.AT_THE_STABLE);
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
    @Override
    public synchronized void horseOutOfStable() throws RemoteException{
        horsesAtStable -= 1;
        
        /**
         * If there are no horses at stable, disable the calling of the broker.
         */
        if(horsesAtStable == 0){
            brokerCalled = false;
        }
    }
    
    /**
     * Stop the service and shut down the betting center.
     */
    @Override
    public synchronized void serviceEnd() throws RemoteException{
        MainProgram.serviceEnd = true;
        notifyAll();
    }
    
}
