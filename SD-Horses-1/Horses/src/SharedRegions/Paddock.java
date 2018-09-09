
package SharedRegions;

import InterveningEntities.*;
import MainProgram.*;
import genclass.GenericIO;

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
    private LoggerInterface logger;
    
    /**
     * Number of spectators on the paddock.
     */
    private int spectatorsOnPaddock = 0;
    
    /**
     * Number of horses on the start line.
     */
    private int horsesOnStartLine = 0;
    
    
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
                GenericIO.writelnString("goCheckHorses - One thread of spectator was interrupted.");
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
                GenericIO.writelnString("proceedToPaddock - One Horse/Jockey thread was interrupted.");
                System.exit(1);
            }
        }
        
    }
    
    /** 
     * If all horses are on the start line, notify all spectators. 
     * Changes the state of the horse to AT_THE_START_LINE.
     */
    public synchronized void proceedToStartLine(){
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_START_LINE);
        logger.updateHorseState(horse.getID(), HorseJockeyState.AT_THE_START_LINE);
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
     * Set the current logger
     * @param logger Logger to be used for the entity
     */
    public synchronized void setLogger(LoggerInterface logger){
        this.logger = logger;
    }
}
