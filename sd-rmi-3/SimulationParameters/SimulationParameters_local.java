
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
    public final static String BETTING_CENTER_HOST_NAME = "localhost";
    
    /**
     * Betting center server port.
     */
    public final static int BETTING_CENTER_PORT = 22420;
    
    /**
     * Betting Center name entry for the register.
     */
    public final static String BETTING_CENTER_NAME_ENTRY = "BettingCenterHandler";
    
    /**
     * Control center server host name.
     */
    public final static String CONTROL_CENTER_HOST_NAME = "localhost";
    
    /**
     * Control center server port.
     */
    public final static int CONTROL_CENTER_PORT = 22421;
    
    /**
     * Control center name entry for the register.
     */
    public final static String CONTROL_CENTER_NAME_ENTRY = "ControlCenterHandler";

    /**
     * Paddock server host name.
     */
    public final static String PADDOCK_HOST_NAME = "localhost";
    
    /**
     * Paddock server port.
     */
    public final static int PADDOCK_PORT = 22422;
    
    /**
     * Paddock name entry for the register.
     */
    public final static String PADDOCK_NAME_ENTRY = "PaddockHandler";
    
    /**
     * Racing Track server host name.
     */
    public final static String RACING_TRACK_HOST_NAME = "localhost";
    
    /**
     * Racing Track server port.
     */
    public final static int RACING_TRACK_PORT = 22423;
    
    /**
     * Racing Track name entry for the register.
     */
    public final static String RACING_TRACK_NAME_ENTRY = "RacingTrackHandler";
    
    /**
     * Stable server host name.
     */
    public final static String STABLE_HOST_NAME = "localhost";
    
    /**
     * Stable server port.
     */
    public final static int STABLE_PORT = 22424;
    
    /**
     * Stable name entry for the register.
     */
    public final static String STABLE_NAME_ENTRY = "StableHandler";
    
    /**
     * Logger server host name.
     */
    public final static String LOGGER_HOST_NAME = "localhost";
    
    /**
     * Logger server port.
     */
    public final static int LOGGER_PORT = 22425;
    
    /**
     * Logger name entry for the register.
     */
    public final static String LOGGER_NAME_ENTRY = "LoggerHandler";
    
    /**
     * Server registry host name.
     */
    public final static String SERVER_REGISTRY_HOST_NAME = "localhost";
    
    /**
     * Server registry server port.
     */
    public final static int SERVER_REGISTRY_PORT = 22426;
    
    /**
     * Server registry host name.
     */
    public final static String REGISTRY_HOST_NAME = "localhost";
    
    /**
     * Server registry server port.
     */
    public final static int REGISTRY_PORT = 22427;
    
    /**
     * Registry name entry for the register.
     */
    public final static String REGISTRY_NAME_ENTRY = "RegisterHandler";
    
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
