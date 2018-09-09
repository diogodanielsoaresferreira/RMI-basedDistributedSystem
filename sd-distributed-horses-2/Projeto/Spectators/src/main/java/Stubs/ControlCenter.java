
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
import MainPackage.Spectator;
import EntitiesState.SpectatorState;
import java.util.ArrayList;

/**
 * Control Center stub. Class used to communicate with the control center
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class ControlCenter {
   
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
    public ControlCenter (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Spectator waits for the next race.
     * Waits that all horses reach the paddock, if there is a next race.
     * Returns false if there is no next race.
     * @param id spectator id
     * @param state spectator state
     * @return true if there is a next race, false otherwise.
     */
    public boolean waitForNextRace(int id, SpectatorState state){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.WAIT_FOR_NEXT_RACE, id, state);
        
        Spectator spectator = ((Spectator)Thread.currentThread());
        spectator.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getIsThereNextRace();
    }
    
    /**
     * Spectator goes to watch the race.
     * Waits for the broker to report the results of a race.
     */
    public void goWatchTheRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_GO_WATCH_THE_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Spectator relaxes after the races.
     * @param id spectator id
     */
    public void relaxABit(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.RELAX_A_BIT, id);
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.CELEBRATING);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Notify the broker that all spectators are on the paddock.
     * @param id spectator id
     */
    public void goCheckHorses(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_CHECK_HORSES, id);
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Checks if the spectator has won the bet.
     * @param id spectator id
     * @param winners list of the winner horses
     * @param bets list of bets made by the spectators on the race
     * @return true if the spectator won the bet, false otherwise
     */
    public boolean haveIWon(int id, ArrayList<SerializableHorse> winners, ArrayList<Bet> bets){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HAVE_I_WON, id, winners, bets);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getHaveIWon();
    }
    
    /**
     * Receive shutdown from an entity.
     * If all entities sent a shutdown, send a shutdown to all other shared regions.
     */
    public void serviceEnd(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.SERVICE_END);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
}
