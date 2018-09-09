
package MainPackage;

import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
import java.rmi.RemoteException;
import java.util.ArrayList;
import Interfaces.ControlCenterInterface;
import Interfaces.LoggerInterface;

/**
 * Where the broker controls the race and the spectator sees the race.
 * Contains internal variables to synchronize the spectator, the horses and the
 * broker, such as the number of spectators on paddock, the number of horses in 
 * the paddock, the number of horses that have finished the race, the number
 * of spectators that saw the results of the race, a boolean variable to check
 * if the broker has reported the results and another boolean variable to check
 * if the race has ended.
 * 
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class ControlCenter implements ControlCenterInterface {
    
    /**
     * Logger class for debugging.
     */
    private final LoggerInterface loggerInt;
    
    /**
     * Number of spectators in the paddock.
     */
    private int spectatorsOnPaddock = 0;
    
    /**
     * Number of horses in the paddock.
     */
    private int horsesOnPaddock = 0;
    
    /**
     * Number of horses that have finished the race.
     */
    private int horsesFinishedTheRace = 0;
    
    /**
     * Number of spectators that saw the results reported by the broker.
     */
    private int spectatorsSawReportedResult = 0;
    
    /**
     * Checks if the broker has already reported the results from the race.
     */
    private boolean reportedResults = false;
    
    /**
     * Checks if races have ended.
     */
    private boolean endRaces = false;
    
    /**
     * Flag that is true if the spectator state is waiting for next race.
     */
    private final boolean waitingState[];
    
    /**
     * Number of entities that have sent a shutdown message.
     */
    private int shutdownNumber = 0;
    
    /**
     * Control center constructor
     * @param logger logger stub instance
     */
    public ControlCenter(LoggerInterface logger){
        this.loggerInt = logger;
        this.waitingState = new boolean[SimulationParameters.N_SPECTATORS];
    }
    
    /**
     * Waits for all spectators to go to the paddock.
     * Only returns when all spectators are on paddock.
     * Also resets the number of horses that have finished the race.
     */
    @Override
    public synchronized void summonHorsesToPaddock() throws RemoteException{
        
        horsesFinishedTheRace = 0;
        while(spectatorsOnPaddock < SimulationParameters.N_SPECTATORS){
            try{
                wait();
            }
            catch(InterruptedException e){
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Start race and wait for all horses finish the race.
     * Ony returns when all horses have finished the race.
     */
    @Override
    public synchronized void startTheRace() throws RemoteException{
        
        while(SimulationParameters.N_HORSE_JOCKEY > horsesFinishedTheRace){
           try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
    }
    
    /** 
     * Report results about the race that occurred and notify the spectators.
     */
    @Override
    public synchronized void reportResults() throws RemoteException{
        reportedResults = true;
        notifyAll();
    }
    
    /**
     * Check if any spectator won money with its bet.
     * @param winners list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if any spectator has won the bet, false otherwise
     */
    @Override
    public synchronized boolean areThereAnyWinners(ArrayList<SerializableHorse> winners, ArrayList<Bet> betList) throws RemoteException{
        /**
         * Check if there is any match between the ID of the bet and the ID of the winner horse.
         */
        
        return betList.stream().anyMatch((bet) -> (winners.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    /**
     * Tells the spectator that races have ended.
     */
    @Override
    public synchronized void endRaces() throws RemoteException{
        endRaces = true;
        notifyAll();
    }
    
    /**
     * Horse proceeds to paddock.
     * Increments the number of horses in the paddock and notifies
     * the spectators of next race if all horses are on the paddock.
     * @param id spectator id
     */
    @Override
    public synchronized void proceedToPaddock(int id) throws RemoteException{
        loggerInt.updateHorseState(id, HorseJockeyState.AT_THE_PADDOCK);
        horsesOnPaddock += 1;
        if(horsesOnPaddock >= SimulationParameters.N_HORSE_JOCKEY){
            notifyAll();
        }
    }
    
    /**
     * Horse got out of paddock.
     */
    @Override
    public synchronized void horseOutOfPaddock() throws RemoteException{
        horsesOnPaddock -= 1;
    }
    
    /**
     * Horse goes to the finish line.
     * @param id horse id
     */
    @Override
    public synchronized void atTheFinishLine(int id) throws RemoteException{
        
        loggerInt.updateHorseState(id, HorseJockeyState.AT_THE_FINISH_LINE);
        horsesFinishedTheRace += 1;
        
        while(horsesFinishedTheRace < SimulationParameters.N_HORSE_JOCKEY){
            try {
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        notifyAll();
    }
    
    /**
     * Spectator waits for the next race.
     * Waits that all horses reach the paddock, if there is a next race.
     * Returns false if there is no next race.
     * @param id spectator id
     * @return true if there is a next race, false otherwise.
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized boolean waitForNextRace(int id) throws RemoteException{
        if(!waitingState[id]){
            loggerInt.updateSpectatorState(id, SpectatorState.WAITING_FOR_A_RACE_TO_START);
            waitingState[id] = true;
        }
        
        /**
         * Wait for all horses to reach the paddock or if there is no next race.
         */
        
        while((horsesOnPaddock < SimulationParameters.N_HORSE_JOCKEY) && !endRaces){
            try{
                wait();
            }
            catch(InterruptedException e){
                System.exit(1);
            }

        }
        
        return !endRaces;
    }
    
    /**
     * Notify the broker that all spectators are on the paddock.
     * @param id spectator id
     */
    @Override
    public synchronized void goCheckHorses(int id) throws RemoteException{
        
        loggerInt.updateSpectatorState(id, SpectatorState.APPRAISING_THE_HORSES);
        waitingState[id] = false;
        spectatorsOnPaddock += 1;
        if(spectatorsOnPaddock >= SimulationParameters.N_SPECTATORS){
            notifyAll();
        }
    }
    
    /**
     * Spectator goes to watch the race.
     * Waits for the broker to report the results of a race.
     */
    @Override
    public synchronized void goWatchTheRace() throws RemoteException{
        
        spectatorsOnPaddock -= 1;
        
        /**
         * Wait for the results of the broker.
         */
        while(!reportedResults){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        /**
         * If all spectators saw the results, clear the reportedResults variable.
         */
        spectatorsSawReportedResult++;
        if(spectatorsSawReportedResult==SimulationParameters.N_SPECTATORS){
            spectatorsSawReportedResult = 0;
            reportedResults = false;
        }
    }
    
    /**
     * Spectator relaxes after the races.
     * @param id spectator id
     */
    @Override
    public synchronized void relaxABit(int id) throws RemoteException{
        loggerInt.updateSpectatorState(id, SpectatorState.CELEBRATING);
    }
    
    /**
     * Checks if the spectator has won the bet.
     * @param id spectator id
     * @param winnerPositions list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if the spectator won the bet, false otherwise
     */
    @Override
    public synchronized boolean haveIWon(int id, ArrayList<SerializableHorse> winnerPositions, ArrayList<Bet> betList) throws RemoteException{
        /**
         * Check if, for every winner positions and for every bet, there is a bet made by the spectator of any winner position.
         */
        return betList.stream().filter((bet) -> (bet.getSpectatorID()==id)).anyMatch((bet) -> (winnerPositions.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    /**
     * Receive shutdown from an entity.
     * If all entities sent a shutdown, send a shutdown to all other shared regions.
     */
    @Override
    public synchronized void serviceEnd() throws RemoteException{
        shutdownNumber++;
        if(shutdownNumber==3){
            MainProgram.serviceEnd = true;
            notifyAll();
        }
    }
    
}
