
package Interfaces;

import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Control Center interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface ControlCenterInterface extends Remote {
    /**
     * Waits for all spectators to go to the paddock.
     * Only returns when all spectators are on paddock.
     * Also resets the number of horses that have finished the race.
     * @throws java.rmi.RemoteException
     */
    public void summonHorsesToPaddock() throws RemoteException;
    
    /**
     * Start race and wait for all horses finish the race.
     * Ony returns when all horses have finished the race.
     * @throws java.rmi.RemoteException
     */
    public void startTheRace() throws RemoteException;
    
    /** 
     * Report results about the race that occurred and notify the spectators.
     * @throws java.rmi.RemoteException
     */
    public void reportResults() throws RemoteException;
    
    /**
     * Check if any spectator won money with its bet.
     * @param winners list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if any spectator has won the bet, false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean areThereAnyWinners(ArrayList<SerializableHorse> winners, ArrayList<Bet> betList) throws RemoteException;
    
    /**
     * Tells the spectator that races have ended.
     * @throws java.rmi.RemoteException
     */
    public void endRaces() throws RemoteException;
    
    /**
     * Horse proceeds to paddock.
     * Increments the number of horses in the paddock and notifies
     * the spectators of next race if all horses are on the paddock.
     * @param id spectator id
     * @throws java.rmi.RemoteException
     */
    public void proceedToPaddock(int id) throws RemoteException;
    
    /**
     * Horse got out of paddock.
     * @throws java.rmi.RemoteException
     */
    public void horseOutOfPaddock() throws RemoteException;
    
    /**
     * Horse goes to the finish line.
     * @param id horse id
     * @throws java.rmi.RemoteException
     */
    public void atTheFinishLine(int id) throws RemoteException;
    
    /**
     * Spectator waits for the next race.
     * Waits that all horses reach the paddock, if there is a next race.
     * Returns false if there is no next race.
     * @param id spectator id
     * @return true if there is a next race, false otherwise.
     * @throws java.rmi.RemoteException
     */
    public boolean waitForNextRace(int id) throws RemoteException;
    
    /**
     * Notify the broker that all spectators are on the paddock.
     * @param id spectator id
     * @throws java.rmi.RemoteException
     */
    public void goCheckHorses(int id) throws RemoteException;
    /**
     * Spectator goes to watch the race.
     * Waits for the broker to report the results of a race.
     * @throws java.rmi.RemoteException
     */
    public void goWatchTheRace() throws RemoteException;
    
    /**
     * Spectator relaxes after the races.
     * @param id spectator id
     * @throws java.rmi.RemoteException
     */
    public void relaxABit(int id) throws RemoteException;
    
    /**
     * Checks if the spectator has won the bet.
     * @param id spectator id
     * @param winnerPositions list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if the spectator won the bet, false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean haveIWon(int id, ArrayList<SerializableHorse> winnerPositions, ArrayList<Bet> betList) throws RemoteException;
    
    /**
     * Receive shutdown from an entity.
     * If all entities sent a shutdown, send a shutdown to all other shared regions.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
    
    
}
