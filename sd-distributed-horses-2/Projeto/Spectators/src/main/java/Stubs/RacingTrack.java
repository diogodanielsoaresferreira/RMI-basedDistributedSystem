
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.SerializableHorse;
import MainPackage.Spectator;
import EntitiesState.SpectatorState;
import java.util.ArrayList;

/**
 * Racing Track stub. Class used to communicate with the racing track
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class RacingTrack {
   
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
    public RacingTrack (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Get the horse/jockey winners in a race.
     * @return list of the horse/jockey winners
     */
    public ArrayList<SerializableHorse> getTheWinners(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.GET_THE_WINNERS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getWinners();
    }
    
    /**
     * Spectator is watching the race.
     * @param id spectator id
     */
    public void goWatchTheRace(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.RACING_TRACK_GO_WATCH_THE_RACE, id);
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.WATCHING_A_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Spectator ended the race round. He is ready for another race.
     */
    public void spectatorEndRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.SPECTATOR_END_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Get a list of the horses in the race.
     * @return horses in the race
     */
    public ArrayList<SerializableHorse> getHorsesInRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.GET_HORSES_IN_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.getHorsesInRace();
    }
    
    
}
