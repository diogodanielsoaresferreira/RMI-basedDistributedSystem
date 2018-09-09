package Communication;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import HelperClasses.Bet;
import HelperClasses.BettingStrategy;
import HelperClasses.SerializableHorse;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a message that is goint to be sent between regions.
 * The class is serializable, and has constructors adequated to every kind
 * of message needed. Also has getters to get the values from the fields.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class Message implements Serializable {
    
    /**
     * Serial version of the class. Format used is 
     * Class-Group-Number of project (XXYYZZ)
     */
    private static final long serialVersionUID = 040302L;
    
    /**
     * Type of the message.
     */
    private MessageType type;
    
    /**
     * Track distance of a race.
     */
    private int trackDistance;
    
    /**
     * Race number of current race.
     */
    private int raceNumber;
    
    /**
     * List of horses in the current race.
     */
    private ArrayList<SerializableHorse> horsesInRace;
    
    /**
     * List of winner horses in the last race.
     */
    private ArrayList<SerializableHorse> winners;
    
    /**
     * List of bets of the spectators for the current race.
     */
    private ArrayList<Bet> spectatorBetList;
    
    /**
     * Check if there are any spectator winners in the last race.
     */
    private boolean areThereAnyWinners;
    
    /**
     * ID of the horse.
     */
    private int horseId;
    
    /**
     * Horse object.
     */
    private SerializableHorse horse;
    
    /**
     * Check if the horse has crossed the finish line.
     */
    private boolean hasCrossedFinishLine;
    
    /**
     * ID of the spectator.
     */
    private int spectatorId;
    
    /**
     * Check if there is a next race.
     */
    private boolean isThereNextRace;
    
    /**
     * Check if the spectator has won his bet.
     */
    private boolean haveIWon;
    
    /**
     * State of the horse.
     */
    private HorseJockeyState horseState;
    
    /**
     * Distance travelled by the horse.
     */
    private int distance;
    
    /**
     * Iterations made by the horse.
     */
    private int iterations;
    
    /**
     * State of the spectator.
     */
    private SpectatorState spectatorState;
    
    /**
     * Money gathered by the spectator.
     */
    private int money;
    
    /**
     * Betting Strategy of the spectator.
     */
    private BettingStrategy strategy;
    
    /**
     * State of the broker.
     */
    private BrokerState brokerState;
    
    /**
     * Horse selection of a bet.
     */
    private int betSelection;
    
    /**
     * Ammount of the bet that the spectator won.
     */
    private int betAmmount;
    
    /**
     * List of the ids of the horses.
     */
    private int[] horseIds;
    
    /**
     * List of the positions of the horses in the current race.
     */
    private int[] horsePositions;
    
    /**
     * Empty constructor for the message that initializes the default
     * values for all the variables.
     */
    public Message(){
        type = null;
        trackDistance = -1;
        raceNumber = -1;
        horsesInRace = null;
        winners = null;
        spectatorBetList = null;
        areThereAnyWinners = false;
        horseId = -1;
        horse = null;
        hasCrossedFinishLine = false;
        spectatorId = -1;
        isThereNextRace = false;
        haveIWon = false;
        horseState = null;
        distance = -1;
        iterations = -1;
        spectatorState = null;
        money = -1;
        strategy = null;
        brokerState = null;
        betSelection = -1;
        betAmmount = -1;
        horseIds = null;
        horsePositions = null;
    }
    
    /**
     * Constructor with only the type of the message.
     * @param type type of the message
     */
    public Message(MessageType type){
        this();
        this.type = type;
    }
    
    /**
     * Constructor with the type of the message and an integer argument.
     * @param type type of the message
     * @param value integer argument
     */
    public Message(MessageType type, int value){
        this();
        this.type = type;
        
        switch(type){
            case CALCULATE_TRACK_DISTANCE:
                trackDistance = value;
                break;
            case SET_RACE_NUMBER:
                raceNumber = value;
                break;
            case CONTROL_CENTER_PROCEED_TO_PADDOCK:
            case HORSE_OUT_OF_STABLE:
            case PADDOCK_PROCEED_TO_START_LINE:
            case RACING_TRACK_PROCEED_TO_START_LINE:
            case MAKE_A_MOVE:
            case AT_THE_FINISH_LINE:
                horseId = value;
                break;
            case CONTROL_CENTER_CHECK_HORSES:
            case RACING_TRACK_GO_WATCH_THE_RACE:
            case RELAX_A_BIT:
            case COLLECT_THE_GAINS:
                spectatorId = value;
                break;
            
            case HAS_FINISHED_LINE_BEEN_CROSSED:
                distance = value;
                break;
            
            case RETURN_COLLECT_THE_GAINS:
            case RETURN_PLACE_A_BET:
                this.money = value;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message and a boolean.
     * @param type type of the message
     * @param bool boolean argument
     */
    public Message(MessageType type, boolean bool){
        this();
        this.type = type;
        switch(type){
            case RETURN_ARE_ANY_WINNERS:    
                this.areThereAnyWinners = bool;
                break;
            case RETURN_HAS_FINISHED_LINE_BEEN_CROSSED:
                this.hasCrossedFinishLine = bool;
                break;
            case RETURN_WAIT_FOR_NEXT_RACE:
                this.isThereNextRace = bool;
                break;
            case RETURN_HAVE_I_WON:
                this.haveIWon = bool;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message and a horse.
     * @param type type of the message
     * @param horse serializable horse
     */
    public Message(MessageType type, SerializableHorse horse){
        this();
        this.type = type;
        this.horse = horse;
    }
    
    /**
     * Constructor with the type of the message and the state of the broker.
     * @param type type of the message
     * @param state broker state
     */
    public Message(MessageType type, BrokerState state){
        this();
        this.type = type;
        switch(type){
            case UPDATE_BROKER_STATE:
                this.brokerState = state;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message and the id and state of a horse
     * @param type type of the message
     * @param id horse id
     * @param state horse state
     */
    public Message(MessageType type, int id, HorseJockeyState state){
        this();
        this.type = type;
        switch(type){
            case UPDATE_HORSE_STATE:
                this.horseId = id;
                this.horseState = state;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message and the id and state of a spectator
     * @param type type of the message
     * @param id spectator id
     * @param state spectator state
     */
    public Message(MessageType type, int id, SpectatorState state){
        this();
        this.type = type;
        switch(type){
            case UPDATE_SPECTATOR_STATE:
                this.spectatorId = id;
                this.spectatorState = state;
                break;
            case WAIT_FOR_NEXT_RACE:
                this.spectatorId = id;
                this.spectatorState = state;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message and two integer arguments
     * @param type type of the message
     * @param value1 argument 1
     * @param value2 argument 2
     */
    public Message(MessageType type, int value1, int value2){
        this();
        this.type = type;
        switch(type){
            case UPDATE_TRACK:
                this.trackDistance = value1;
                this.raceNumber = value2;
                break;
            case RETURN_MAKE_A_MOVE:
                this.distance = value1;
                this.iterations = value2;
                break;
            case UPDATE_SPECTATOR_MONEY:
                this.spectatorId = value1;
                this.money = value2;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message, a list of the ids of the horses
     * and a list with the horse positions
     * @param type type of the message
     * @param horseIds array with the IDs of the horses
     * @param horsePositions array with the positions of the horses in the alst race
     */
    public Message(MessageType type, int[] horseIds, int[] horsePositions){
        this();
        this.type = type;
        switch(type){
            case REPORT_RESULTS:
                this.horseIds = horseIds;
                this.horsePositions = horsePositions;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message, and id and two integer arguments
     * @param type type of the message
     * @param id identifier
     * @param value1 argument 1
     * @param value2 argument 2
     */
    public Message(MessageType type, int id, int value1, int value2){
        this();
        this.type = type;
        switch(type){
            case UPDATE_HORSE_IN_RACE:
                this.horseId = id;
                this.distance = value1;
                this.iterations = value2;
                break;
            case UPDATE_SPECTATOR_BET:
                this.spectatorId = id;
                this.betSelection = value1;
                this.betAmmount = value2;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message, the horse id, the race number
     * and the horse state
     * @param type type of the message
     * @param id horse id
     * @param raceNumber race number
     * @param state horse state
     */
    public Message(MessageType type, int id, int raceNumber, HorseJockeyState state){
        this();
        this.type = type;
        switch(type){
            case PROCEED_TO_STABLE:{
                this.horseId = id;
                this.raceNumber = raceNumber;
                this.horseState = state;
            }
        }
        
    }
    
    /**
     * Constructor with the type of the message, and an array list
     * @param type type of the message
     * @param list parameterizable array list
     */
    public Message(MessageType type, ArrayList<?> list){
        this();
        this.type = type;
        
        switch(type){
            case RETURN_WINNERS:
                this.winners = (ArrayList<SerializableHorse>) list;
                break;
            case RETURN_SPECTATOR_BET_LIST:
                this.spectatorBetList = (ArrayList<Bet>) list;
                break;
            case HONOUR_THE_BETS:
                this.winners = (ArrayList<SerializableHorse>) list;
                break;
            case RETURN_HORSES_IN_RACE:
                this.horsesInRace = (ArrayList<SerializableHorse>) list;
                break;
        }
    }
    
    /**
     * Constructor with the type of the message, the spectator id, an array list,
     * a betting strategy and a money ammount
     * @param type type of the message
     * @param id spectator id
     * @param list parameterizable array list
     * @param strategy betting strategy of a spectator
     * @param money money bet by the spectator
     */
    public Message(MessageType type, int id, ArrayList<?> list, BettingStrategy strategy, int money){
        this();
        this.type = type;
        
        switch(type){
            case PLACE_A_BET:
                this.spectatorId = id;
                this.horsesInRace = (ArrayList<SerializableHorse>) list;
                this.strategy = strategy;
                this.money = money;
                break;
        }    
    }
    
    /**
     * Constructor with the type of the message and two array lists
     * @param type type of the message
     * @param winners array list with the winners of the race
     * @param bets array list with the bets of the spectators
     */
    public Message(MessageType type, ArrayList<?> winners, ArrayList<?> bets){
        this();
        this.type = type;
        
        this.winners = (ArrayList<SerializableHorse>) winners;
        this.spectatorBetList = (ArrayList<Bet>) bets;
        
    }
    
    /**
     * Constructor with the type of the message, the spectator id and
     * two array lists
     * @param type type of the message
     * @param id spectator id
     * @param winners array list of the horse winners
     * @param bets array list of the spectator bets
     */
    public Message(MessageType type, int id, ArrayList<?> winners, ArrayList<?> bets){
        this();
        this.type = type;
        switch(type){
            case HAVE_I_WON:
                this.spectatorId = id;
                this.winners = (ArrayList<SerializableHorse>) winners;
                this.spectatorBetList = (ArrayList<Bet>) bets;
                break;
        }
        
    }
    
    /**
     * Get the type of the message
     * @return message type
     */
    public MessageType getType(){
        return this.type;
    }
    
    /**
     * Get the distance of the track
     * @return track distance
     */
    public int getTrackDistance(){
        return trackDistance;
    }
    
    /**
     * Get the current race number
     * @return race number
     */
    public int getRaceNumber(){
        return raceNumber;
    }
    
    /**
     * Get the winners of the current race
     * @return list of horses winners of the current race
     */
    public ArrayList<SerializableHorse> getWinners(){
        return winners;
    }
    
    /**
     * Get the list of all bets made by the spectators
     * @return list of all bets made by the spectators
     */
    public ArrayList<Bet> getSpectatorBetList(){
        return spectatorBetList;
    }
    
    /**
     * Check if there are winners in the last race.
     * @return true if there are winners, false otherwise
     */
    public boolean getAreThereAnyWinners(){
        return areThereAnyWinners;
    }
    
    /**
     * Get the id of the horse
     * @return horse id
     */
    public int getHorseId(){
        return horseId;
    }
    
    /**
     * Get the serialized horse sent
     * @return horse
     */
    public SerializableHorse getHorse(){
        return horse;
    }
    
    /**
     * Check if the finish line has been crossed by a horse
     * @return true if the finish line has been crossed, false otherwise
     */
    public boolean hasFinishLineBeenCrossed(){
        return hasCrossedFinishLine;
    }
    
    /**
     * Get the id of the spectator
     * @return id of the spectator
     */
    public int getSpectatorId(){
        return spectatorId;
    }
    
    /**
     * Check if there is a next race
     * @return true if there is a next race, false otherwise
     */
    public boolean getIsThereNextRace(){
        return isThereNextRace;
    }
    
    /**
     * Get all the horses in the current race
     * @return list of horses in the current race
     */
    public ArrayList<SerializableHorse> getHorsesInRace(){
        return horsesInRace;
    }
    
    /**
     * Check if I have won the current race
     * @return true if the spectator has won the race, false otherwise
     */
    public boolean getHaveIWon(){
        return haveIWon;
    }
    
    /**
     * Get the state of the horse.
     * @return horse state
     */
    public HorseJockeyState getHorseState(){
        return horseState;
    }
    
    /**
     * Get the distance travelled by the horse
     * @return distance travelled by the horse
     */
    public int getDistance(){
        return distance;
    }
    
    /**
     * Get the iterations that the horse has made
     * @return number of iterations
     */
    public int getIterations(){
        return iterations;
    }
    
    /**
     * Get the spectator state
     * @return spectator state
     */
    public SpectatorState getSpectatorState(){
        return spectatorState;
    }
    
    /**
     * Get the betting strategy of a spectator
     * @return betting strategy
     */
    public BettingStrategy getBettingStrategy(){
        return strategy;
    }
    
    /**
     * Get the money sent from a spectator
     * @return money
     */
    public int getMoney(){
        return money;
    }
    
    /**
     * Get the state of the broker
     * @return broker state
     */
    public BrokerState getBrokerState(){
        return brokerState;
    }
    
    /**
     * Get the selection of a bet
     * @return bet selection
     */
    public int getBetSelection(){
        return betSelection;
    }
    
    /**
     * Get the bet ammount from a spectator
     * @return bet ammount
     */
    public int getBetAmmount(){
        return betAmmount;
    }
    
    /**
     * Get a list of the ids of the horses
     * @return list of horse ids
     */
    public int[] getHorseIds(){
        return horseIds;
    }
    
    /**
     * Get a list of the position of the horses
     * @return list with horse positions
     */
    public int[] getHorsePositions(){
        return horsePositions;
    }
}

