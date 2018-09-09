
package Interfaces;

import HelperClasses.SerializableHorse;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Racing Track interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface RacingTrackInterface extends Remote {
    
    /**
     * Sets the track distance to a value between min_distance and max_distance.
     * Also sets the race number in the logger.
     * @param raceNumber number of the race
     * @throws java.rmi.RemoteException
     */
    public void calculateTrackDistance(int raceNumber) throws RemoteException;
    
    /**
     * Notify all horses to start the race.
     * @throws java.rmi.RemoteException
     */
    public void startRace() throws RemoteException;
    
    /**
     * Broker signals that the race has ended.
     * @throws java.rmi.RemoteException
     */
    public void endRace() throws RemoteException;
    
    
    /** 
     * Report results about the race to the logger.
     * @throws java.rmi.RemoteException
     */
    public void reportResults() throws RemoteException;
    
    /**
     * Get the horse/jockey winners in a race.
     * @return list of the horse/jockey winners
     * @throws java.rmi.RemoteException
     */
    public ArrayList<SerializableHorse> getTheWinners() throws RemoteException;
    
    /**
     * Clear the race positions.
     * Waits for all spectators to leave the race, and clears the variables
     * that refer to the race.
     * @throws java.rmi.RemoteException
     */
    public void clearRacePositions() throws RemoteException;
    
    /**
     * Add horse to the list of horses that are on the next race.
     * @param horse serializable horse to be added to the race
     * @throws java.rmi.RemoteException
     */
    public void addHorseToRace(SerializableHorse horse) throws RemoteException;
    
    /**
     * Send horse to the start line.
     * Waits for the broker to start the race, or for any movement of any other 
     * horse, to start the race.
     * @param id horse id
     * @throws java.rmi.RemoteException
     */
    public void proceedToStartLine(int id) throws RemoteException;
    
    
    /**
     * Checks if the finish line has been crossed.
     * @param horseDistance distance that the horse has travelled
     * @return true if the horse crossed the finish line, false otherwise.
     * @throws java.rmi.RemoteException
     */
    public boolean hasFinishedLineBeenCrossed(int horseDistance) throws RemoteException;
    
    /**
     * Horse makes a move.
     * Horse waits for his turn to move, then updates his distance and the number of iterations.
     * If it has finished the race, removes himself from the list of running horses
     * and adds itself to the final positions.
     * At the end, if there are still horses to be woke, wakes up the next horse.
     * @param id horse id
     * @return array with two positions: the horse distance and the horse iterations
     * @throws java.rmi.RemoteException
     */
    public int[] makeAMove(int id) throws RemoteException;
    
    /**
     * Get a list of the horses in the race.
     * @return horses in the race
     * @throws java.rmi.RemoteException
     */
    public ArrayList<SerializableHorse> getHorsesInRace() throws RemoteException;
    
    /**
     * Spectator ended the race round. He is ready for another race.
     * @throws java.rmi.RemoteException
     */
    public void spectatorEndRace() throws RemoteException;
    
    
    /**
     * Spectator is watching the race.
     * @param id spectator id
     * @throws java.rmi.RemoteException
     */
    public void goWatchTheRace(int id) throws RemoteException;
    
    /**
     * Stop the service and shuts down the shared region.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
}
