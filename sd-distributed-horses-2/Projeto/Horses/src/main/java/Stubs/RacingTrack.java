
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import HelperClasses.SerializableHorse;
import MainPackage.HorseJockey;
import EntitiesState.HorseJockeyState;

/**
 * Racing Track stub. Class used to communicate with the Racing Track
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
     * Send horse to the start line.
     * Waits for the broker to start the race, or for any movement of any other 
     * horse, to start the race.
     * @param id horse id
     */
    public void proceedToStartLine(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.RACING_TRACK_PROCEED_TO_START_LINE, id);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse makes a move.
     * Horse waits for his turn to move, then updates his distance and the number of iterations.
     * If it has finished the race, removes himself from the list of running horses
     * and adds itself to the final positions.
     * At the end, if there are still horses to be woke, wakes up the next horse.
     * @param id horse id
     */
    public void makeAMove(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.MAKE_A_MOVE, id);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.RUNNING);
        horse.setDistance(inMessage.getDistance());
        horse.setIteration(inMessage.getIterations());
        com.close ();
    }
    
    /**
     * Checks if the finish line has been crossed.
     * @param horseDistance distance that the horse has travelled
     * @return true if the horse crossed the finish line, false otherwise.
     */
    public boolean hasFinishedLineBeenCrossed(int horseDistance){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HAS_FINISHED_LINE_BEEN_CROSSED, horseDistance);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
        return inMessage.hasFinishLineBeenCrossed();
    }
    
    /**
     * Add horse to the list of horses that are on the next race.
     * @param horse serializable horse to be added to the race
     */
    public void addHorseToRace(SerializableHorse horse){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.ADD_HORSE_TO_RACE, horse);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
}
