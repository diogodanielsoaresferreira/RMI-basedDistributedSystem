
package MainPackage;

import EntitiesState.HorseJockeyState;
import java.rmi.RemoteException;
import Interfaces.LoggerInterface;
import Interfaces.PaddockInterface;

/**
 * Where the horses are showed to the spectators.
 * It also contains as internal variables the number os spectators on paddock
 * and th number of horses on the start line.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Paddock implements PaddockInterface {
    
    /**
     * Logger class for debugging.
     */
    private final LoggerInterface loggerInt;
    
    /**
     * Number of spectators on the paddock.
     */
    private int spectatorsOnPaddock = 0;
    
    /**
     * Number of horses on the start line.
     */
    private int horsesOnStartLine = 0;
    
    /**
     * Paddock constructor
     * @param logger logger to be used in the processing of the calls
     */
    public Paddock(LoggerInterface logger){
        this.loggerInt = logger;
    }
    
    /**
     * Spectator goes to the paddock check the horses.
     * Only returns when all horses are on the start line.
     */
    @Override
    public synchronized void goCheckHorses() throws RemoteException{
        
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
    @Override
    public synchronized void proceedToPaddock() throws RemoteException{
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
    @Override
    public synchronized void proceedToStartLine(int id) throws RemoteException{
       
        loggerInt.updateHorseState(id, HorseJockeyState.AT_THE_START_LINE);
        horsesOnStartLine += 1;
        
        if(horsesOnStartLine>=SimulationParameters.N_HORSE_JOCKEY){
            notifyAll();
        }
    }
    
    /**
     * Horse in not on the start line anymore.
     */
    @Override
    public synchronized void horseOutOfStartLine() throws RemoteException{
        horsesOnStartLine -= 1;
    }
    
    /**
     * Stop the service and shut down the betting center.
     */
    @Override
    public synchronized void serviceEnd() throws RemoteException{
        MainProgram.serviceEnd = true;
        notifyAll();
    }
    
}

