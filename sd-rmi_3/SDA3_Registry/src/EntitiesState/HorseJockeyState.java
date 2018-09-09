
package EntitiesState;


/**
 * Enum with the possible states of the HorseJockey on his lifecycle.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public enum HorseJockeyState{
    /**
     * Horse at the stable.
     */
    AT_THE_STABLE ("Stb"),

    /**
     * Horse at the paddock.
     */
    AT_THE_PADDOCK ("Pad"),

    /**
     * Horse at the start line.
     */
    AT_THE_START_LINE ("SLn"),

    /**
     * Horse currently in the middle of a race.
     */
    RUNNING ("Run"),

    /**
     * Horse reached the end of a race.
     */
    AT_THE_FINISH_LINE ("Fin");

    private final String description;

    private HorseJockeyState(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
