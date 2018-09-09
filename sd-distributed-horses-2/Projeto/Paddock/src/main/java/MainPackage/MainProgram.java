package MainPackage;

import SharedRegion.PaddockProxy;
import SharedRegion.Paddock;
import java.net.SocketTimeoutException;
import Communication.ServerComm;
import SharedRegion.ServiceProvider;
import Stubs.Logger;

/**
 * Main paddock program.
 * Initializes the service provider and waits for a connection
 * in a communication channel.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    /**
     * Used to check if the service must terminate.
     */
    public static boolean serviceEnd = false;
    
    /**
     * Main paddock launcher
     * @param args args
     */
    public static void main(String [] args){
        /**
         * Communication channels.
         */
        ServerComm scon, sconi;
        ServiceProvider sp;
        
        /**
         * Stub initialization.
         */
        Logger logger = new Logger(SimulationParameters.LOGGER_HOST_NAME, SimulationParameters.LOGGER_PORT);
        
        /**
         * Shared region and proxy initialization.
         */
        Paddock pd = new Paddock(logger);
        PaddockProxy pdInt = new PaddockProxy(pd);
        
        /**
         * Start listening on the communication channel.
         */
        scon = new ServerComm(SimulationParameters.PADDOCK_PORT);
        scon.start();
        
        /**
         * While the service is not terminated, accept connections and send them
         * to the service provider.
         */
        while(!serviceEnd){
            try {
                sconi = scon.accept();
                sp = new ServiceProvider(sconi, pdInt);
                sp.start();
            } catch (SocketTimeoutException ex) {
            }
            
        }
    }
    
}
