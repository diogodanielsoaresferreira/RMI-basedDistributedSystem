package SharedRegion;

import Communication.Message;
import Communication.ServerComm;

/**
 * Service Provider implementation.
 * Processes and replies messages accordingly to the internal implementation
 * of a shared region.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class ServiceProvider extends Thread {
    
    /**
     * Communication channel with the server.
     */
    private final ServerComm com;
    
    /**
     * Shared region implementation.
     */
    private final ISharedRegion rtInt;
    
    /**
     * Service Provider constructor.
     * @param com communication channel with the server.
     * @param rtInt shared region.
     */
    public ServiceProvider(ServerComm com, ISharedRegion rtInt){
        this.com = com;
        this.rtInt = rtInt;
    }
    
    /**
     * Lifecycle of the service provider.
     */
    @Override
    public void run(){
        /**
         * Read object from the communication channel.
         */
        Message inMessage = (Message) com.readObject();
        
        /**
         * Process and reply request.
         */
        Message outMessage = rtInt.processAndReply(inMessage, com);
        /**
         * Send reply and close communication channel.
         */
        com.writeObject(outMessage);
        com.close();
    }
}
