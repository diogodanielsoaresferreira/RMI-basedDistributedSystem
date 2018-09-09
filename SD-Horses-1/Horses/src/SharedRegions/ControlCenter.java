
package SharedRegions;

import BettingHelper.Bet;
import InterveningEntities.*;
import MainProgram.*;
import genclass.GenericIO;
import java.util.ArrayList;

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

public class ControlCenter {
    
    /**
     * Logger class for debugging.
     */
    private LoggerInterface logger;
    
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
     * Waits for all spectators to go to the paddock.
     * Only returns when all spectators are on paddock.
     * Also resets the number of horses that have finished the race.
     */
    public synchronized void summonHorsesToPaddock(){
        
        /**
         * Reset the variable horsesFinishedTheRace.
         */
        horsesFinishedTheRace = 0;
        while(spectatorsOnPaddock < SimulationParameters.N_SPECTATORS){
            try{
                wait();
            }
            catch(InterruptedException e){
                GenericIO.writelnString("summonHorsesToPaddock - Thread of broker was interrupted.");
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Start race and wait for all horses finish the race.
     * Ony returns when all horses have finished the race.
     */
    public synchronized void startTheRace(){
        
        while(SimulationParameters.N_HORSE_JOCKEY > horsesFinishedTheRace){
           try{
                wait();
            } catch (InterruptedException ex) {
                GenericIO.writelnString("startTheRace - Thread of broker was interrupted.");
                System.exit(1);
            }
        }
    }
    
    /** 
     * Report results about the race that occurred and notify the spectators.
     */
    public synchronized void reportResults(){
        reportedResults = true;
        notifyAll();
    }
    
    /**
     * Check if any spectator won money with its bet.
     * @param winners list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if any spectator has won the bet, false otherwise
     */
    public synchronized boolean areThereAnyWinners(ArrayList<HorseJockey> winners, ArrayList<Bet> betList){
        /**
         * Check if there is any match between the ID of the bet and the ID of the winner horse.
         */
        return betList.stream().anyMatch((bet) -> (winners.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    /**
     * Spectator waits for the next race.
     * Waits that all horses reach the paddock, if there is a next race.
     * Returns false if there is no next race.
     * Also changes the internal state of the spectator to WAITING_FOR_A_RACE_TO_START.
     * @return true if there is a next race, false otherwise.
     */
    public synchronized boolean waitForNextRace(){
        Spectator spectator = ((Spectator)Thread.currentThread());
        if(spectator.getSpectatorState() != SpectatorState.WAITING_FOR_A_RACE_TO_START){
            spectator.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START);
            logger.updateSpectatorState(spectator.getID(), SpectatorState.WAITING_FOR_A_RACE_TO_START);
        }
        
        
        /**
         * Wait for all horses to reach the paddock or if there is no next race.
         */
        
        while((horsesOnPaddock < SimulationParameters.N_HORSE_JOCKEY) && !endRaces){
            try{
                wait();
            }
            catch(InterruptedException e){
                GenericIO.writelnString("waitForNextRace - One thread of Spectator was interrupted.");
                System.exit(1);
            }

        }
        
        return !endRaces;
    }
    
    /**
     * Spectator goes to watch the race.
     * Waits for the broker to report the results of a race.
     */
    public synchronized void goWatchTheRace(){
        
        spectatorsOnPaddock -= 1;
        
        /**
         * Wait for the results of the broker.
         */
        while(!reportedResults){
            try{
                wait();
            } catch (InterruptedException ex) {
                GenericIO.writelnString("goWatchTheRace - One thread of Spectator was interrupted.");
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
     * Change the state of the spectator to CELEBRATING.
     */
    public synchronized void relaxABit(){
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.CELEBRATING);
        logger.updateSpectatorState(spectator.getID(), SpectatorState.CELEBRATING);
    }
    
    /**
     * Horse proceeds to paddock.
     * Changes the internal state of the horse to AT_THE_PADDOCK.
     * Increments the number of horses in the paddock and notifies
     * the spectators of next race if all horses are on the paddock.
     */
    public synchronized void proceedToPaddock(){
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_PADDOCK);
        logger.updateHorseState(horse.getID(), HorseJockeyState.AT_THE_PADDOCK);
        horsesOnPaddock += 1;
        if(horsesOnPaddock >= SimulationParameters.N_HORSE_JOCKEY){
            notifyAll();
        }
    }
    
    /**
     * Horse got out of paddock.
     */
    public synchronized void horseOutOfPaddock(){
        horsesOnPaddock -= 1;
    }
    
    /**
     * Notify the broker that all spectators are on the paddock.
     * Also changes the state of the spectator to APPRAISING_THE_HORSES.
     */
    public synchronized void goCheckHorses(){
        Spectator spectator = (Spectator)Thread.currentThread();
        spectator.setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);
        logger.updateSpectatorState(spectator.getID(), SpectatorState.APPRAISING_THE_HORSES);
        
        spectatorsOnPaddock += 1;
        if(spectatorsOnPaddock >= SimulationParameters.N_SPECTATORS){
            notifyAll();
        }
    }
    
    /**
     * Tells the spectator that races have ended.
     */
    public synchronized void endRaces(){
        endRaces = true;
        notifyAll();
    }
    
    /**
     * Checks if the spectator has won the bet.
     * @param winnerPositions list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if the spectator won the bet, false otherwise
     */
    public synchronized boolean haveIWon(ArrayList<HorseJockey> winnerPositions, ArrayList<Bet> betList){
        Spectator spectator = ((Spectator)Thread.currentThread());
        /**
         * Check if, for every winner positions and for every bet, there is a bet made by the spectator of any winner position.
         */
        return betList.stream().filter((bet) -> (bet.getSpectatorID()==spectator.getID())).anyMatch((bet) -> (winnerPositions.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    
    /**
     * Horse goes to the finish line.
     * Changes the state of the horse to AT_THE_FINISH_LINE.
     */
    public synchronized void atTheFinishLine(){
        HorseJockey horse = ((HorseJockey)Thread.currentThread());
        horse.setHorseState(HorseJockeyState.AT_THE_FINISH_LINE);
        logger.updateHorseState(horse.getID(), HorseJockeyState.AT_THE_FINISH_LINE);
        horsesFinishedTheRace += 1;
        
        while(horsesFinishedTheRace < SimulationParameters.N_HORSE_JOCKEY){
            try {
                wait();
            } catch (InterruptedException ex) {
                GenericIO.writelnString("atTheFinishLine - One thread of HorseJockey was interrupted.");
                System.exit(1);
            }
        }
        
        notifyAll();
    }
    
    
    /**
     * Set the current logger
     * @param logger Logger to be used for the entity
     */
    public synchronized void setLogger(LoggerInterface logger){
        this.logger = logger;
    }
    
}
