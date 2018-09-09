
package SharedRegion;

import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import HelperClasses.Bet;
import HelperClasses.SerializableHorse;
import MainPackage.MainProgram;
import MainPackage.SimulationParameters;
import Stubs.*;
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
    private final Logger logger;
    
    /**
     * Betting Center stub class.
     */
    private final BettingCenter bc;
    
    /**
     * Paddock stub class.
     */
    private final Paddock pd;
    
    /**
     * Racing track stub class.
     */
    private final RacingTrack rt;
    
    /**
     * Stable stub class.
     */
    private final Stable st;
    
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
     * Number of entities that have sent a shutdown message.
     */
    private int shutdownNumber = 0;
    
    /**
     * Control center constructor
     * @param logger logger stub instance
     * @param pd paddock stub instance
     * @param rt racing track stub instance
     * @param st stable stub instance
     * @param bc betting center stub instance
     */
    public ControlCenter(Logger logger, Paddock pd, RacingTrack rt, Stable st, BettingCenter bc){
        this.logger = logger;
        this.bc = bc;
        this.pd = pd;
        this.rt = rt;
        this.st = st;
    }
    
    /**
     * Waits for all spectators to go to the paddock.
     * Only returns when all spectators are on paddock.
     * Also resets the number of horses that have finished the race.
     */
    public synchronized void summonHorsesToPaddock(){
        
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
    public synchronized void startTheRace(){
        
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
    public synchronized boolean areThereAnyWinners(ArrayList<SerializableHorse> winners, ArrayList<Bet> betList){
        /**
         * Check if there is any match between the ID of the bet and the ID of the winner horse.
         */
        
        return betList.stream().anyMatch((bet) -> (winners.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    /**
     * Tells the spectator that races have ended.
     */
    public synchronized void endRaces(){
        endRaces = true;
        notifyAll();
    }
    
    /**
     * Horse proceeds to paddock.
     * Increments the number of horses in the paddock and notifies
     * the spectators of next race if all horses are on the paddock.
     * @param id spectator id
     */
    public synchronized void proceedToPaddock(int id){
        logger.updateHorseState(id, HorseJockeyState.AT_THE_PADDOCK);
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
     * Horse goes to the finish line.
     * @param id horse id
     */
    public synchronized void atTheFinishLine(int id){
        
        logger.updateHorseState(id, HorseJockeyState.AT_THE_FINISH_LINE);
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
     * @param state spectator state
     * @return true if there is a next race, false otherwise.
     */
    public synchronized boolean waitForNextRace(int id, SpectatorState state){
        if(state != SpectatorState.WAITING_FOR_A_RACE_TO_START){
            logger.updateSpectatorState(id, SpectatorState.WAITING_FOR_A_RACE_TO_START);
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
    public synchronized void goCheckHorses(int id){
        
        logger.updateSpectatorState(id, SpectatorState.APPRAISING_THE_HORSES);
        
        spectatorsOnPaddock += 1;
        if(spectatorsOnPaddock >= SimulationParameters.N_SPECTATORS){
            notifyAll();
        }
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
    public synchronized void relaxABit(int id){
        logger.updateSpectatorState(id, SpectatorState.CELEBRATING);
    }
    
    /**
     * Checks if the spectator has won the bet.
     * @param id spectator id
     * @param winnerPositions list of the winner horses
     * @param betList list of bets made by the spectators on the race
     * @return true if the spectator won the bet, false otherwise
     */
    public synchronized boolean haveIWon(int id, ArrayList<SerializableHorse> winnerPositions, ArrayList<Bet> betList){
        /**
         * Check if, for every winner positions and for every bet, there is a bet made by the spectator of any winner position.
         */
        return betList.stream().filter((bet) -> (bet.getSpectatorID()==id)).anyMatch((bet) -> (winnerPositions.stream().anyMatch((position) -> (bet.getHorseToBet().getID()==position.getID()))));
    }
    
    /**
     * Receive shutdown from an entity.
     * If all entities sent a shutdown, send a shutdown to all other shared regions.
     */
    public synchronized void serviceEnd(){
        shutdownNumber++;
        if(shutdownNumber==3){
            bc.serviceEnd();
            rt.serviceEnd();
            st.serviceEnd();
            pd.serviceEnd();
            logger.serviceEnd();
            MainProgram.serviceEnd = true;
        }
    }
    
}
