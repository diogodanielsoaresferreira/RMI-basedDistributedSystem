
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
import MainPackage.Broker;
import EntitiesState.BrokerState;
import java.util.ArrayList;

/**
 * Betting Center stub. Class used to communicate with the betting center
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class BettingCenter {
    
    /**
     * Name of the computational system where it is located the server.
     */
    private final String serverHostName;

    /**
     * Number of server listening port.
     */
    private final int serverPortNumb;
    
    /**
     *  Stub instatiation.
     *
     *    @param hostName Name of the computational system where it is located the server.
     *    @param port Number of server listening port.
     */
    public BettingCenter (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Method used by the broker to accept all the bets from the spectators.
     * Waits for each spectator to come to the betting center and retrieves
     * the bets made by them.
     * Only returns when all spectators made his bets.
     */
    public void acceptTheBets(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.ACCEPT_THE_BETS);
        Broker broker = (Broker)Thread.currentThread();
        broker.setBrokerState(BrokerState.WAITING_FOR_BETS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * The broker gives the reward to the winning bets.
     * This method is called by the broker
     * Waits for each spectator to come retrieve his bet. When they are waiting
     * on the betting center, saves the ammount of money to give to them.
     * Only returns when all spectators come collect their gains.
     * @param winners list of all the horses that won the race
     */
    public void honourTheBets(ArrayList<SerializableHorse> winners){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HONOUR_THE_BETS, winners);
        Broker broker = (Broker)Thread.currentThread();
        broker.setBrokerState(BrokerState.SETTLING_ACCOUNTS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Get the list of bets from the spectators
     * @return list of bets from the spectators
     */
    public ArrayList<Bet> getSpectatorBetList(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.GET_SPECTATOR_BET_LIST);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getSpectatorBetList();
    }
}
