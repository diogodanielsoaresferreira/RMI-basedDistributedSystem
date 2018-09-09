
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
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
     * Waits for all spectators to go to the paddock.
     * Only returns when all spectators are on paddock.
     * Also resets the number of horses that have finished the race.
     */
    public void summonHorsesToPaddock(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_SUMMON_HORSES_TO_PADDOCK);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Start race and wait for all horses finish the race.
     * Ony returns when all horses have finished the race.
     */
    public void startTheRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_START_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /** 
     * Report results about the race that occurred and notify the spectators.
     */
    public void reportResults(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_REPORT_RESULTS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Check if any spectator won money with its bet.
     * @param winners list of the winner horses
     * @param bets list of bets made by the spectators on the race
     * @return true if any spectator has won the bet, false otherwise
     */
    public boolean areThereAnyWinners(ArrayList<SerializableHorse> winners, ArrayList<Bet> bets){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.ARE_ANY_WINNERS, winners, bets);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getAreThereAnyWinners();
    }
    
    /**
     * Tells the spectator that races have ended.
     */
    public void endRaces(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_END_RACES);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
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
