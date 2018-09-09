
package Interfaces;

import EntitiesState.HorseJockeyState;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Stable interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface StableInterface extends Remote {
    /**
     * Set the number of the next race.
     * @param raceNumber number of the next race
     * @throws java.rmi.RemoteException
     */
    public void setRaceNumber(int raceNumber) throws RemoteException;
    
    /**
     * Call all horses to come out of the stable to the paddock.
     * @throws java.rmi.RemoteException
     */
    public void summonHorsesToPaddock() throws RemoteException;
    
    /**
     * Broker calls the horses to entertain the guests.
     * @throws java.rmi.RemoteException
     */
    public void entertainTheGuests() throws RemoteException;
    
    /**
     * Horses go to the stable and wait for the calling of the broker.
     * Waits for the broker to call the horses to the race number of
     * the horse, or to entertain the guests.
     * @param id horse id
     * @param raceNumber current race number
     * @param state horse state
     * @throws java.rmi.RemoteException
     */
    public void proceedToStable(int id, int raceNumber, HorseJockeyState state) throws RemoteException;
    
    /**
     * Horse came out of stable.
     * @throws java.rmi.RemoteException
     */
    public void horseOutOfStable() throws RemoteException;
    
    /**
     * Stop the service and shuts down the shared region.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
}
