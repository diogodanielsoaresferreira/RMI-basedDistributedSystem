
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.SerializableHorse;
import MainPackage.Broker;
import EntitiesState.BrokerState;
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
     * Sets the track distance to a value between min_distance and max_distance.
     * Also sets the race number in the logger.
     * @param raceNumber number of the race
     */
    public void calculateTrackDistance(int raceNumber){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CALCULATE_TRACK_DISTANCE, raceNumber);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        Broker broker = (Broker)Thread.currentThread();
        broker.setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
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
     * Notify all horses to start the race.
     */
    public void startRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.RACING_TRACK_START_RACE);
        Broker broker = (Broker)Thread.currentThread();
        broker.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Broker signals that the race has ended.
     */
    public void endRace(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.END_RACE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Clear the race positions.
     * Waits for all spectators to leave the race, and clears the variables
     * that refer to the race.
     */
    public void clearRacePositions(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CLEAR_RACE_POSITIONS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /** 
     * Report results about the race to the logger.
     */
    public void reportResults(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.RACING_TRACK_REPORT_RESULTS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
}
