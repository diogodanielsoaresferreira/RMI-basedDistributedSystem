
package MainPackage;


import Stubs.*;
import static MainPackage.SimulationParameters.*;

/**
 * Main spectators program.
 * Initialize stubs and start spectators lifecycle.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    public static void main(String [] args){
        
        /**
         * Stubs initialization.
         */
        RacingTrack rt = new RacingTrack(RACING_TRACK_HOST_NAME, RACING_TRACK_PORT);
        ControlCenter cc = new ControlCenter(CONTROL_CENTER_HOST_NAME, CONTROL_CENTER_PORT);
        BettingCenter bc = new BettingCenter(BETTING_CENTER_HOST_NAME, BETTING_CENTER_PORT);
        Paddock pd = new Paddock(PADDOCK_HOST_NAME, PADDOCK_PORT);
        
        Spectator[] spectators = new Spectator[SimulationParameters.N_SPECTATORS];
        
        for(int i = 0; i < spectators.length; i++){
            spectators[i] = new Spectator(i, cc, pd, bc, rt);
        }
        
        /**
         * Spectators lifecycle start.
         */
        for (Spectator spectator : spectators) {
            spectator.start();
        }
        
        for (Spectator spectator : spectators) {
            try{
                spectator.join();
            }catch (InterruptedException ex) {
            }
        }
        cc.serviceEnd();
        
    }
    
}
