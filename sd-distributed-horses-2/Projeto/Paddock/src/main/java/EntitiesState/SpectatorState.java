
package EntitiesState;


/**
 * Enum with the possible states of the spectator.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public enum SpectatorState{
    /**
     * Witing for the broker to announce a race.
     */
    WAITING_FOR_A_RACE_TO_START ("Wai"),

    /**
     * Appraising the horses so he can bet on them.
     */
    APPRAISING_THE_HORSES ("App"),

    /**
     * Placing a bet on the horses.
     */
    PLACING_A_BET ("Pla"),

    /**
     * Watching the race.
     */
    WATCHING_A_RACE ("Wat"),

    /**
     * Collecting the gains from the broker.
     */
    COLLECTING_THE_GAINS ("Col"),

    /**
     * Celebrating after the race.
     */
    CELEBRATING ("Cel");

    private final String description;

    private SpectatorState(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}

