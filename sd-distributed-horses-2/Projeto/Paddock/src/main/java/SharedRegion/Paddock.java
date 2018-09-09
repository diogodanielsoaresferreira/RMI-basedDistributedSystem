
package SharedRegion;

import EntitiesState.HorseJockeyState;
import MainPackage.MainProgram;
import MainPackage.SimulationParameters;
import Stubs.Logger;

/**
 * Where the horses are showed to the spectators.
 * It also contains as internal variables the number os spectators on paddock
 * and th number of horses on the start line.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Paddock {
    
    /**
     * Logger class for debugging.
     */
    private final Logger logger;
    
    /**
     * Number of spectators on the paddock.
     */
    private int spectatorsOnPaddock = 0;
    
    /**
     * Number of horses on the start line.
     */
    private int horsesOnStartLine = 0;
    
    public Paddock(Logger logger){
        this.logger = logger;
    }
    
    /**
     * Spectator goes to the paddock check the horses.
     * Only returns when all horses are on the start line.
     */
    public synchronized void goCheckHorses(){
        
        spectatorsOnPaddock += 1;
        if(spectatorsOnPaddock >= SimulationParameters.N_SPECTATORS){
            /**
             * Notify all horses that all spectators are on paddock.
             */
            notifyAll();
        }
        
        /**
         * Wait for all horses to be on the start line.
         */
        while(horsesOnStartLine < SimulationParameters.N_HORSE_JOCKEY){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        spectatorsOnPaddock -= 1;
    }
    
    /**
     * Horse/Jockey goes to paddock and waits for all spectators to reach the paddock.
     */
    public synchronized void proceedToPaddock(){
        /**
         * Wait for all spectators to reach the paddock.
         */
        while(spectatorsOnPaddock < SimulationParameters.N_SPECTATORS){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Horse proceed to start line.
     * @param id horse id
     */
    public synchronized void proceedToStartLine(int id){
       
        logger.updateHorseState(id, HorseJockeyState.AT_THE_START_LINE);
        horsesOnStartLine += 1;
        
        if(horsesOnStartLine>=SimulationParameters.N_HORSE_JOCKEY){
            notifyAll();
        }
    }
    
    /**
     * Horse in not on the start line anymore.
     */
    public synchronized void horseOutOfStartLine(){
        horsesOnStartLine -= 1;
    }
    
    /**
     * Terminate the paddock service.
     */
    public synchronized void serviceEnd(){
        MainProgram.serviceEnd = true;
    }
}

