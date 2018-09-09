
package MainPackage;

import HelperClasses.BettingStrategy;

/**
 * Class with all parameters needed for the simulation referent to the races,
 * horses, spectators, shared regions and log file.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class SimulationParameters {
    
    /**
     * Betting center server host name.
     */
    public final static String BETTING_CENTER_HOST_NAME = "l040101-ws02.ua.pt";
    
    /**
     * Betting center server port.
     */
    public final static int BETTING_CENTER_PORT = 22420;
    
    /**
     * Control center server host name.
     */
    public final static String CONTROL_CENTER_HOST_NAME = "l040101-ws03.ua.pt";
    
    /**
     * Control center server port.
     */
    public final static int CONTROL_CENTER_PORT = 22421;

    /**
     * Paddock server host name.
     */
    public final static String PADDOCK_HOST_NAME = "l040101-ws04.ua.pt";
    
    /**
     * Paddock server port.
     */
    public final static int PADDOCK_PORT = 22422;
    
    /**
     * Racing Track server host name.
     */
    public final static String RACING_TRACK_HOST_NAME = "l040101-ws05.ua.pt";
    
    /**
     * Racing Track server port.
     */
    public final static int RACING_TRACK_PORT = 22423;
    
    /**
     * Stable server host name.
     */
    public final static String STABLE_HOST_NAME = "l040101-ws06.ua.pt";
    
    /**
     * Stable server port.
     */
    public final static int STABLE_PORT = 22424;
    
    /**
     * Logger server host name.
     */
    public final static String LOGGER_HOST_NAME = "l040101-ws01.ua.pt";
    
    /**
     * Logger server port.
     */
    public final static int LOGGER_PORT = 22425;
    
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
