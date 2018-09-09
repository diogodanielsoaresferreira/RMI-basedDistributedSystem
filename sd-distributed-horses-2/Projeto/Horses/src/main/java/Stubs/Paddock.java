
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import MainPackage.HorseJockey;
import EntitiesState.HorseJockeyState;

/**
 * Paddock stub. Class used to communicate with the Paddock
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class Paddock {
    
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
    public Paddock (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Horse/Jockey goes to paddock and waits for all spectators to reach the paddock.
     */
    public void proceedToPaddock(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.PADDOCK_PROCEED_TO_PADDOCK);
        
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse proceed to start line.
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
        
        Message msg = new Message(MessageType.PADDOCK_PROCEED_TO_START_LINE, id);
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_START_LINE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse in not on the start line anymore.
     */
    public void horseOutOfStartLine(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HORSE_OUT_OF_START_LINE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
}
