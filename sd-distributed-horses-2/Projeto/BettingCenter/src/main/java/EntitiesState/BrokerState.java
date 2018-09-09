
package EntitiesState;

/**
 * Enum with the possible states of the broker on his lifecycle.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
    
public enum BrokerState  { 
    /**
     * Opening the event.
     */
    OPENING_THE_EVENT ("Open"),

    /**
     * Announcint the next race.
     */
    ANNOUNCING_NEXT_RACE ("Anno"),

    /**
     * Waiting for bets from the spectators.
     */
    WAITING_FOR_BETS ("Wait"),

    /**
     * Supervising the race.
     */
    SUPERVISING_THE_RACE ("Supe"),

    /**
     * Settling accounts with the winner spectators.
     */
    SETTLING_ACCOUNTS ("Sett"),

    /**
     * Playig host at the bar.
     */
    PLAYING_HOST_AT_THE_BAR ("Play");

    private final String description;

    private BrokerState(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
