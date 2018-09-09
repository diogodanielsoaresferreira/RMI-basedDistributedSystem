
package MainPackage;

import Stubs.RacingTrack;
import Stubs.Stable;
import Stubs.ControlCenter;
import Stubs.BettingCenter;
import static MainPackage.SimulationParameters.*;

/**
 * Main broker program.
 * Initialize stubs and start broker lifecycle.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    public static void main(String [] args){
        
        /**
         * Stub initialization.
         */
        RacingTrack rt = new RacingTrack(RACING_TRACK_HOST_NAME, RACING_TRACK_PORT);
        ControlCenter cc = new ControlCenter(CONTROL_CENTER_HOST_NAME, CONTROL_CENTER_PORT);
        BettingCenter bc = new BettingCenter(BETTING_CENTER_HOST_NAME, BETTING_CENTER_PORT);
        Stable st = new Stable(STABLE_HOST_NAME, STABLE_PORT);
        
        /**
         * Broker lifecycle start.
         */
        Broker broker = new Broker(rt, cc, bc, st);
        broker.start();
        try {
            broker.join();
        } catch (InterruptedException ex) {
        }
        cc.serviceEnd();
    }
    
    
}
