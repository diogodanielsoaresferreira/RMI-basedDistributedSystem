
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import MainPackage.HorseJockey;
import EntitiesState.HorseJockeyState;

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
     * Horse proceeds to paddock.
     * Increments the number of horses in the paddock and notifies
     * the spectators of next race if all horses are on the paddock.
     * @param id spectator id
     */
    public void proceedToPaddock(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.CONTROL_CENTER_PROCEED_TO_PADDOCK, id);
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_PADDOCK);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse got out of paddock.
     */
    public void horseOutOfPaddock(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HORSE_OUT_OF_PADDOCK);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse goes to the finish line.
     * @param id horse id
     */
    public void atTheFinishLine(int id){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.AT_THE_FINISH_LINE, id);
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_FINISH_LINE);
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
