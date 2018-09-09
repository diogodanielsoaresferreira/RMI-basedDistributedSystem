
package MainPackage;

import EntitiesState.*;
import HelperClasses.SerializableHorse;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import Interfaces.LoggerInterface;
import Interfaces.RacingTrackInterface;

/**
 * Contains the methods that refer to the execution of a race by the horse/Jockey,
 * and his control by the broker.
 * It also has a list of all the horses that are registered in the current race
 * and other internal variables for synchronization between the horse/jockey
 * and the broker.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class RacingTrack implements RacingTrackInterface {
    
    /**
     * Logger class for debugging.
     */
    private final LoggerInterface loggerInt;
    
    /**
     * Track Distance.
     */
    private int trackDistance = -1;
    
    /**
     * Check if the race has started.
     */
    private boolean raceStarted = false;
    
    /**
     * All the horses registered in the current race.
     */
    private final ArrayList<SerializableHorse> horsesInRace = new ArrayList<>();
    
    /**
     * ID of the horse that is currently running.
     */
    private int horseRunning = -1;
    
    /**
     * Number of spectators that are no longer interested in the race.
     */
    private int spectatorLeavesRace = 0;
    
    /**
     * Spectators that are currently watching the race.
     */
    private int spectatorsWatchingRace = 0;
    
    /**
     * Racing track constructor.
     * @param logger logger stub for debugging operations
     */
    public RacingTrack(LoggerInterface logger){
        this.loggerInt = logger;
    }
    
    /**
     * Sets the track distance to a value between min_distance and max_distance.
     * Also sets the race number in the logger.
     * @param raceNumber number of the race
     */
    @Override
    public synchronized void calculateTrackDistance(int raceNumber) throws RemoteException{
        Random random = new Random(); 
        this.trackDistance = random.nextInt(SimulationParameters.MAXIMUM_TRACK_DISTANCE-SimulationParameters.MINIMUM_TRACK_DISTANCE+1)+SimulationParameters.MINIMUM_TRACK_DISTANCE;
        
        loggerInt.cleanRaceVariables();
        loggerInt.updateTrack(this.trackDistance, raceNumber);
        
        loggerInt.updateBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
        
    }
    
    /**
     * Notify all horses to start the race.
     */
    @Override
    public synchronized void startRace() throws RemoteException{
        
        loggerInt.updateBrokerState(BrokerState.SUPERVISING_THE_RACE);
        raceStarted = true;
        notifyAll();
    }
    
    /**
     * Broker signals that the race has ended.
     */
    @Override
    public synchronized void endRace() throws RemoteException{
        raceStarted = false;
        horseRunning = -1;
    }
    
    
    /** 
     * Report results about the race to the logger.
     */
    @Override
    public synchronized void reportResults() throws RemoteException{
        int [] horseIds = new int[horsesInRace.size()];
        int [] positions = new int[horsesInRace.size()];
        
        for(int i=0; i<horsesInRace.size(); i++){
            horseIds[i] = horsesInRace.get(i).getID();
            positions[i] = getHorseTrackPosition(horseIds[i]);
        }
        
        loggerInt.reportResults(horseIds, positions);
    }
    
    /**
     * Get the position of a horse in a race
     * @param horseID id of the horse in a race
     * @return position of a horse in a race
     */
    private synchronized int getHorseTrackPosition(int horseID) throws RemoteException{
        ArrayList <SerializableHorse> sortedHorses = (ArrayList <SerializableHorse>) this.horsesInRace.clone();
        Collections.sort(sortedHorses);
        
        SerializableHorse tempHorse = null;
        int position = -1;
        
        /**
         * Traverse ordered list of horses in the race.
         */
        for(int i=0; i<sortedHorses.size(); i++){
            SerializableHorse horse = sortedHorses.get(i);
            
            /**
             * If there is no position stored, or the temporary Horse is in a
             * higher position than the index, update.
             */
            if (position == -1 || tempHorse.compareTo(horse)<0){
                tempHorse = horse;
                position = i;
            }
            
            /**
             * If the ID of both horses match, we have found its position.
             */
            if(horse.getID()==horseID)
                return position + 1;
        }
        
        return -1;
    }
    
    /**
     * Get the horse/jockey winners in a race.
     * @return list of the horse/jockey winners
     */
    @Override
    public synchronized ArrayList<SerializableHorse> getTheWinners() throws RemoteException{
        
        ArrayList <SerializableHorse> sortedHorses = (ArrayList <SerializableHorse>) this.horsesInRace.clone();
        Collections.sort(sortedHorses);
        ArrayList<SerializableHorse> winnerHorsePositions = new ArrayList<>();
        
        /**
         * Get the winner(s).
         */
        for(SerializableHorse horse : sortedHorses){
            if(winnerHorsePositions.isEmpty()){
                winnerHorsePositions.add(horse);
            }
            else{
                /**
                 * Check if there was a draw.
                 */
                if(horse.compareTo(winnerHorsePositions.get(0))==0)
                    winnerHorsePositions.add(horse);
                else{
                    break;
                }
                    
            }  
        }
        
        return winnerHorsePositions;
    }
    
    /**
     * Clear the race positions.
     * Waits for all spectators to leave the race, and clears the variables
     * that refer to the race.
     */
    @Override
    public synchronized void clearRacePositions() throws RemoteException{
        /**
         * Wait for all the spectators to leave the race.
         */
        while(spectatorLeavesRace < SimulationParameters.N_SPECTATORS){
            try {
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        horsesInRace.clear();
        spectatorsWatchingRace = 0;
    }
    
    /**
     * Add horse to the list of horses that are on the next race.
     * @param horse serializable horse to be added to the race
     */
    @Override
    public synchronized void addHorseToRace(SerializableHorse horse) throws RemoteException{
        horsesInRace.add(horse);
    }
    
    /**
     * Send horse to the start line.
     * Waits for the broker to start the race, or for any movement of any other 
     * horse, to start the race.
     * @param id horse id
     */
    @Override
    public synchronized void proceedToStartLine(int id) throws RemoteException{
        
        /**
         * The first horse to get to the start line will be the first to start the race.
         */
        if(horseRunning==-1){
            horseRunning = id;
        }
        
        /**
         * Wait for the race to start.
         */
        while(!raceStarted){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Checks if the finish line has been crossed.
     * @param horseDistance distance that the horse has travelled
     * @return true if the horse crossed the finish line, false otherwise.
     */
    @Override
    public synchronized boolean hasFinishedLineBeenCrossed(int horseDistance) throws RemoteException{
        return horseDistance>=trackDistance;
    }
    
    /**
     * Horse makes a move.
     * Horse waits for his turn to move, then updates his distance and the number of iterations.
     * If it has finished the race, removes himself from the list of running horses
     * and adds itself to the final positions.
     * At the end, if there are still horses to be woke, wakes up the next horse.
     * @param id horse id
     * @return array with two positions: the horse distance and the horse iterations
     */
    @Override
    public synchronized int[] makeAMove(int id) throws RemoteException{
        
        /**
         * Wait for other horses to calculate their distance and wait also for
         * all spectators to watch the race.
         */
        while(horseRunning != id || spectatorsWatchingRace<SimulationParameters.N_SPECTATORS){
            
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        /**
         * Update horse race distance and iteration.
         */
        SerializableHorse horse = findHorseById(id);
        horse.setDistance(horse.getDistance()+calculateHorseDistance(horse.getAgility()));
        horse.setIterations(horse.getIterations()+1);
        loggerInt.updateHorseInRace(id, horse.getIterations(), horse.getDistance());
        
        /**
         * Wake up next horse if there are still horses running.
         */
        wakeUpNextHorse(horse);
        return new int[] {horse.getDistance(), horse.getIterations()};
    }
    
    /**
     * Get a list of the horses in the race.
     * @return horses in the race
     */
    @Override
    public synchronized ArrayList<SerializableHorse> getHorsesInRace() throws RemoteException{
        return horsesInRace;
    }
    
    /**
     * Spectator ended the race round. He is ready for another race.
     */
    @Override
    public synchronized void spectatorEndRace() throws RemoteException{
        spectatorLeavesRace ++;
        notifyAll();
    }
    
    /**
     * Wake up next horse in the list to continue its race.
     * @param horse horse that has finished his iteration
     */
    private synchronized void wakeUpNextHorse(SerializableHorse horse) throws RemoteException{
        int currIdx = horsesInRace.indexOf(horse);
        int nextIdx = (horsesInRace.indexOf(horse)+1)%horsesInRace.size();
        
        if(nextIdx >= horsesInRace.size()){
            horseRunning = -1;
            return;
        }
        
        /**
         * Find the next horse that has finished the race.
         */
        while(horsesInRace.get(nextIdx).getDistance()>=this.trackDistance){
            /**
             * If true, there are no horses to wake up.
             */
            if(nextIdx==currIdx){
                horseRunning = -1;
                return;
            }
            nextIdx = (nextIdx+1)%horsesInRace.size();
        }
        
        /**
         * Wake up next horse.
         */
        horseRunning = horsesInRace.get(nextIdx).getID();
        notifyAll();
    }
    
    /**
     * Calculate the distance of the horse step in a move.
     * @param agility agility of a horse
     * @return distance that the horse moved in this step
     */
    private synchronized int calculateHorseDistance(int agility) throws RemoteException{
        Random random = new Random();
        return random.nextInt(agility)+1;
    }
    
    /**
     * Find horse object by his id
     * @param id horse id
     * @return horse object
     */
    private synchronized SerializableHorse findHorseById(int id) throws RemoteException{
        SerializableHorse horse = null;
        for (SerializableHorse findHorse : horsesInRace) {
            if(findHorse.getID()==id){
                horse = findHorse;
                break;
            }
        }
        return horse;
    }
    
    /**
     * Spectator is watching the race.
     * @param id spectator id
     */
    @Override
    public synchronized void goWatchTheRace(int id) throws RemoteException{
        loggerInt.updateSpectatorState(id, SpectatorState.WATCHING_A_RACE);
        spectatorsWatchingRace++;
        notifyAll();
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

