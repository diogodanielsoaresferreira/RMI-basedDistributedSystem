
package MainProgram;

import BettingHelper.BettingStrategy;

/**
 * Class with all parameters needed for the simulation referent to the races,
 * horses, spectators and log file.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class SimulationParameters {
    
    /**
     * Number of races.
     */
    public static final int N_RACES = 5;
    
    /** 
     * Number of horse/jockey pairs in each race.
     */
    public static final int N_HORSE_JOCKEY = 4;
    
    /**
     * Agility of all horses.
     */
    public static final int[] HORSES_AGILITY = {5, 4, 3, 2, 4, 6, 8, 1, 4, 6, 4, 2, 4, 3, 2, 4, 5, 4, 3, 2};
    
    /**
     * Number of spectators in the races.
     */
    public static final int N_SPECTATORS = 4;
    
    /**
     * Money of each one of the spectators in the beginning of the race.
     */
    public static final int[] SPECTATORS_MONEY = {1000, 1000, 1000, 1000};
    
    /**
     * Betting strategy of the spectators.
     */
    public static final BettingStrategy[] SPECTATORS_BETTING_STRATEGY = {
        BettingStrategy.HIGH_RISK_AND_HIGH_BETS,
        BettingStrategy.LOW_RISK_AND_HIGH_BETS,
        BettingStrategy.RANDOM_AND_HIGH_BETS,
        BettingStrategy.RANDOM_AND_LOW_BETS
    };
    
    /**
     * Minimum track distance for a race.
     */
    public static final int MINIMUM_TRACK_DISTANCE = 15;
    
    /**
     * Maximum track distance for a race.
     */
    public static final int MAXIMUM_TRACK_DISTANCE = 25;
    
    /**
     * Name of the file used for the logger.
     */
    public static final String FILE_NAME = "log.txt";
}
