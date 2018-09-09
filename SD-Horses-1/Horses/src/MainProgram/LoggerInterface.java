
package MainProgram;

import InterveningEntities.*;

/**
 *
 * Interface for a logger for the program.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface LoggerInterface {
    
    /**
     * Cleans the log file and logs the description of the problem and the header
     * of the log file.
     */
    public void initLog();
    
     /**
     * Cleans the variables of the last race.
     */
    public void cleanRaceVariables();
    
     /**
     * Update the current Track distance and the race number
     * @param trackDistance distance of the current track
     * @param raceNumber new race number
     */
    public void updateTrack(int trackDistance, int raceNumber);
    
    
    /**
     * Updates the state of the broker
     * @param state new broker state
     */
    public void updateBrokerState(BrokerState state);
    
    /**
     * Updates the state of the spectator
     * @param id id of the spectator
     * @param state new spectator state
     */
    public void updateSpectatorState(int id, SpectatorState state);
    
    /**
     * Updates the state of a horse/jockey
     * @param id id of the horse/jockey
     * @param state new horse/jockey state
     */
    public void updateHorseState(int id, HorseJockeyState state);
    
    /**
     * Updates the horse variables in a race
     * @param id id of the horse
     * @param iteration number of iteration that the horse has made
     * @param distance distance that the horse has travelled
     */
    public void updateHorseInRace(int id, int iteration, int distance);
    
    /**
     * Updates the spectator money
     * @param id id of the spectator
     * @param money new money ammount
     */
    public void updateSpectatorMoney(int id, int money);
    
    /**
     * Updates the bet of a new spectator.
     * The bet ammount will be automatically gathered from the spectator money
     * @param id id of the spectator
     * @param betSel bet selection of the spectator
     * @param betAmmount ammount of the bet that the spectator has made
     */
    public void updateSpectatorBet(int id, int betSel, int betAmmount);
    
    /**
     * Report the results of the race.
     * @param horseIds id of the horses in the race
     * @param horsePositions position of the horses in the race
     */
    public void reportResults(int[] horseIds, int[] horsePositions);
}
