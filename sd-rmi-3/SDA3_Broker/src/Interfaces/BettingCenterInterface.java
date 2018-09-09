
package Interfaces;

import HelperClasses.Bet;
import HelperClasses.BettingStrategy;
import HelperClasses.SerializableHorse;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Betting Center interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface BettingCenterInterface extends Remote {
    /**
     * Method used by the broker to accept all the bets from the spectators.
     * Waits for each spectator to come to the betting center and retrieves
     * the bets made by them.
     * Only returns when all spectators made his bets.
     * @throws java.rmi.RemoteException
     */
    public void acceptTheBets() throws RemoteException;
    
    /**
     * Get the list of bets from the spectators
     * @return list of bets from the spectators
     * @throws java.rmi.RemoteException
     */
    public ArrayList<Bet> getSpectatorBetList() throws RemoteException;
    
    /**
     * The broker gives the reward to the winning bets.
     * This method is called by the broker
     * Waits for each spectator to come retrieve his bet. When they are waiting
     * on the betting center, saves the ammount of money to give to them.
     * Only returns when all spectators come collect their gains.
     * @param winners list of all the horses that won the race
     * @throws java.rmi.RemoteException
     */
    public void honourTheBets(ArrayList<SerializableHorse> winners) throws RemoteException;
    
    
    /**
     * Spectator places a bet.
     * Method used by the spectators to place a bet.
     * It waits for the broker to be ready to receive bets.
     * When the broker is ready, calculates the bet, based on his strategy, 
     * and adds the bet to the bet list.
     * Returns when the broker has accepted the bet.
     * @param id spectator id
     * @param horses list of horses in a race
     * @param strategy spectator strategy
     * @param money spectator money
     * @return bet amount
     * @throws java.rmi.RemoteException
     */
    public int placeABet(int id, ArrayList<SerializableHorse> horses, BettingStrategy strategy, int money) throws RemoteException;
    
    /**
     * Spectator collects the gains from the bets.
     * Puts the spectator  in the list of the spectators waiting to retrieve bets.
     * When the broker has retrieved the bet, collects the gains given by the
     * broker and returns them.
     * @param id spectator id
     * @return money
     * @throws java.rmi.RemoteException
     */
    public int collectTheGains(int id) throws RemoteException;
    
    /**
     * Stop the service and shuts down the shared region.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
}
