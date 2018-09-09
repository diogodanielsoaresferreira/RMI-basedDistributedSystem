
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import MainPackage.Broker;
import EntitiesState.BrokerState;

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
     * Call all horses to come out of the stable to the paddock.
     */
    public void summonHorsesToPaddock(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.STABLE_SUMMON_HORSES_TO_PADDOCK);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Broker calls the horses to entertain the guests.
     */
    public void entertainTheGuests(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.ENTERTAIN_THE_GUESTS);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        
        Broker broker = (Broker)Thread.currentThread();
        broker.setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
        com.close ();
    }
    
    /**
     * Set the number of the next race.
     * @param raceNumber number of the next race
     */
    public void setRaceNumber(int raceNumber){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        
        Message msg = new Message(MessageType.SET_RACE_NUMBER, raceNumber);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
}
