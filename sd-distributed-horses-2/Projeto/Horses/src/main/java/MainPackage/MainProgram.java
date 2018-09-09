
package MainPackage;


import Stubs.*;
import static MainPackage.SimulationParameters.*;

/**
 * Main horses program.
 * Initialize stubs and start horses lifecycle.
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
        Stable st = new Stable(STABLE_HOST_NAME, STABLE_PORT);
        Paddock pd = new Paddock(PADDOCK_HOST_NAME, PADDOCK_PORT);
        
        
        HorseJockey[] horses = new HorseJockey[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        
        for(int i = 0; i < horses.length; i++){
            horses[i] = new HorseJockey(i, i/SimulationParameters.N_HORSE_JOCKEY, pd, rt, st, cc);
        }
        /**
         * Broker lifecycle start.
         */
        for (HorseJockey horse : horses) {
            horse.start();
        }
        
        for (HorseJockey horse : horses) {
            try{
                horse.join();
            }
            catch (InterruptedException ex) {
            }
        }
        cc.serviceEnd();
    }
    
}
