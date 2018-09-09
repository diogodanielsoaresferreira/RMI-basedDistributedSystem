
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.Bet;
import HelperClasses.BettingStrategy;
import HelperClasses.SerializableHorse;
import MainPackage.Spectator;
import EntitiesState.SpectatorState;
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
     * Spectator places a bet.
     * Method used by the spectators to place a bet.
     * It waits for the broker to be ready to receive bets.
     * When the broker is ready, calculates the bet, based on his strategy, 
     * and adds the bet to the bet list.
     * Returns when the broker has accepted the bet.
     * @param id spectator id
     * @param horsesInRace list of horses in a race
     * @param strategy spectator strategy
     * @param money spectator money
     */
    public void placeABet(int id, ArrayList<SerializableHorse> horsesInRace, BettingStrategy strategy, int money){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.PLACE_A_BET, id, horsesInRace, strategy, money);
        Spectator spectator = ((Spectator)Thread.currentThread());
        spectator.setSpectatorState(SpectatorState.PLACING_A_BET);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        spectator.addMoney(-inMessage.getMoney());
    }
    
    /**
     * Spectator collects the gains from the bets.
     * Puts the spectator  in the list of the spectators waiting to retrieve bets.
     * When the broker has retrieved the bet, collects the gains given by the
     * broker and returns them.
     * @param id spectator id
     */
    public void collectTheGains(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.COLLECTING_THE_GAINS);
        Message msg = new Message(MessageType.COLLECT_THE_GAINS, id);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        spectator.addMoney(inMessage.getMoney());
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
