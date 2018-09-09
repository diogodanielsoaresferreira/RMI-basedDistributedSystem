
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import MainPackage.HorseJockey;
import EntitiesState.HorseJockeyState;

/**
 * Stable stub. Class used to communicate with the stable
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class Stable {
    
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
    public Stable (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }
    
    /**
     * Horses go to the stable and wait for the calling of the broker.
     * Waits for the broker to call the horses to the race number of
     * the horse, or to entertain the guests.
     * @param id horse id
     * @param raceNumber current race number
     * @param state horse state
     */
    public void proceedToStable(int id, int raceNumber, HorseJockeyState state){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        Message msg = new Message(MessageType.PROCEED_TO_STABLE, id, raceNumber, state);
        horse.setHorseState(HorseJockeyState.AT_THE_STABLE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Horse came out of stable.
     */
    public void horseOutOfStable(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.HORSE_OUT_OF_STABLE);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
}
