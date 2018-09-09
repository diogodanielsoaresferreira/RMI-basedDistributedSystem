
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;



/**
 * Paddock stub. Class used to communicate with the paddock
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
     * Terminate the service.
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
