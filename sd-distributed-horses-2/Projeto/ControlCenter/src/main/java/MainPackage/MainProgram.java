package MainPackage;

import SharedRegion.ControlCenter;
import SharedRegion.ControlCenterProxy;
import java.net.SocketTimeoutException;
import Communication.ServerComm;
import SharedRegion.ServiceProvider;
import Stubs.*;

/**
 * Main Control Center program.
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
     * Main control center launcher
     * @param args args
     */
    public static void main(String [] args){
        /**
         * Communication channels.
         */
        ServerComm scon, sconi;
        ServiceProvider sp;
        
        /**
         * Stubs initialization.
         */
        Logger logger = new Logger(SimulationParameters.LOGGER_HOST_NAME, SimulationParameters.LOGGER_PORT);
        RacingTrack rt = new RacingTrack(SimulationParameters.RACING_TRACK_HOST_NAME, SimulationParameters.RACING_TRACK_PORT);
        Paddock pd = new Paddock(SimulationParameters.PADDOCK_HOST_NAME, SimulationParameters.PADDOCK_PORT);
        BettingCenter bc = new BettingCenter(SimulationParameters.BETTING_CENTER_HOST_NAME, SimulationParameters.BETTING_CENTER_PORT);
        Stable st = new Stable(SimulationParameters.STABLE_HOST_NAME, SimulationParameters.STABLE_PORT);
        
        /**
         * Shared region and proxy initialization.
         */
        ControlCenter cc = new ControlCenter(logger, pd, rt, st, bc);
        ControlCenterProxy ccInt = new ControlCenterProxy(cc);
        
        /**
         * Start listening on the communication channel.
         */
        scon = new ServerComm(SimulationParameters.CONTROL_CENTER_PORT);
        scon.start();
        
        /**
         * While the service is not terminated, accept connections and send them
         * to the service provider.
         */
        while(!serviceEnd){
            try {
                sconi = scon.accept();
                sp = new ServiceProvider(sconi, ccInt);
                sp.start();
            } catch (SocketTimeoutException ex) {
            }
            
        }
    }
    
}
