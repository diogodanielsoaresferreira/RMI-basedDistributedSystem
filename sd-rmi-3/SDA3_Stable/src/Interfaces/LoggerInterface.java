
package Interfaces;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Logger interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface LoggerInterface extends Remote {
    
    /**
     * Update the current Track distance and the race number
     * @param trackDistance distance of the current track
     * @param raceNumber new race number
     * @throws java.rmi.RemoteException
     */
    public void updateTrack(int trackDistance, int raceNumber) throws RemoteException;
    
    /**
     * Cleans the variables of the last race.
     * @throws java.rmi.RemoteException
     */
    public void cleanRaceVariables() throws RemoteException;
    
    /**
     * Updates the state of the broker
     * @param state new broker state
     * @throws java.rmi.RemoteException
     */
    public void updateBrokerState(BrokerState state) throws RemoteException;
    
    /**
     * Updates the state of the spectator
     * @param id id of the spectator
     * @param state new spectator state
     * @throws java.rmi.RemoteException
     */
    public void updateSpectatorState(int id, SpectatorState state) throws RemoteException;
    
    /**
     * Updates the state of a horse/jockey
     * @param id id of the horse/jockey
     * @param state new horse/jockey state
     * @throws java.rmi.RemoteException
     */
    public void updateHorseState(int id, HorseJockeyState state) throws RemoteException;
    
    /**
     * Updates the horse variables in a race
     * @param id id of the horse
     * @param iteration number of iteration that the horse has made
     * @param distance distance that the horse has travelled
     * @throws java.rmi.RemoteException
     */
    public void updateHorseInRace(int id, int iteration, int distance) throws RemoteException;
    
    /**
     * Updates the bet of a new spectator.
     * The bet ammount will be automatically gathered from the spectator money
     * @param id id of the spectator
     * @param betSel bet selection of the spectator
     * @param betAmmount ammount of the bet that the spectator has made
     * @throws java.rmi.RemoteException
     */
    public void updateSpectatorBet(int id, int betSel, int betAmmount) throws RemoteException;
    
    /**
     * Updates the spectator money
     * @param id id of the spectator
     * @param money new money ammount
     * @throws java.rmi.RemoteException
     */
    public void updateSpectatorMoney(int id, int money) throws RemoteException;
    
    /**
     * Report the results of the race.
     * @param horseIds id of the horses in the race
     * @param horsePositions position of the horses in the race
     * @throws java.rmi.RemoteException
     */
    public void reportResults(int[] horseIds, int[] horsePositions) throws RemoteException;
    
    /**
     * Stop the service and shuts down the shared region.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
    
}
