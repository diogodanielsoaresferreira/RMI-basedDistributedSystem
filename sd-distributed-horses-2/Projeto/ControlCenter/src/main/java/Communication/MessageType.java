
package Communication;

/**
 * Defines the message types used on the messaging system.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public enum MessageType {
    
    /**
     * When a shared region answers with an OK to a request made by an entity.
     */
    STATUS_OK,
    
    /**
     * Calculate the distance of a new track.
     */
    CALCULATE_TRACK_DISTANCE,
    
    /**
     * Signal the racing track to start the race.
     */
    RACING_TRACK_START_RACE,
    
    /**
     * Signal the end of the current race.
     */
    END_RACE,
    
    /**
     * Signal the racing track that the results will be reported.
     */
    RACING_TRACK_REPORT_RESULTS,
    
    /**
     * Clear the race positions of last race.
     */
    CLEAR_RACE_POSITIONS,
    
    /**
     * Set the number of a new race.
     */
    SET_RACE_NUMBER,
    
    /**
     * Signal the stable to summon all horses to paddock.
     */
    STABLE_SUMMON_HORSES_TO_PADDOCK,
    
    /**
     * Signal the control center to summon all horses to padock.
     */
    CONTROL_CENTER_SUMMON_HORSES_TO_PADDOCK,
    
    /**
     *Broker is ready to accept the bets.
     */
    ACCEPT_THE_BETS,
    
    /**
     * Signal the control center to start the race.
     */
    CONTROL_CENTER_START_RACE,
    
    /**
     * Signal the control center that the results of the race will be reported.
     */
    CONTROL_CENTER_REPORT_RESULTS,
    
    /**
     * Broker will entertain the guests.
     */
    ENTERTAIN_THE_GUESTS,
    
    /**
     * Signal to end all races.
     */
    END_RACES,
    
    /**
     * Signal control center to end all races.
     */
    CONTROL_CENTER_END_RACES,
    
    /**
     * Spectator is waiting for the next race.
     */
    WAIT_FOR_NEXT_RACE,
    
    /**
     * Spectator on the control center will check horses.
     */
    CONTROL_CENTER_CHECK_HORSES,
    
    /**
     * Spectator will check the horses to the paddock.
     */
    PADDOCK_CHECK_HORSES,
    
    /**
     * Spectator is going to watch the race.
     */
    CONTROL_CENTER_GO_WATCH_THE_RACE,
    
    /**
     * Spectator has arrived to watch the race.
     */
    RACING_TRACK_GO_WATCH_THE_RACE,
    
    /**
     * Spectator has left the race.
     */
    SPECTATOR_END_RACE,
    
    /**
     * Spectator relaxes at the end of all races.
     */
    RELAX_A_BIT,
    
    /**
     * Horse proceeds to stable.
     */
    PROCEED_TO_STABLE,
    
    /**
     * Horse is added to the race.
     */
    ADD_HORSE_TO_RACE,
    
    /**
     * Signal control center that horse proceeds to paddock.
     */
    CONTROL_CENTER_PROCEED_TO_PADDOCK,
    
    /**
     * Horse proceeds to paddock.
     */
    PADDOCK_PROCEED_TO_PADDOCK,
    
    /**
     * Horse is out of paddock.
     */
    HORSE_OUT_OF_PADDOCK,
    
    /**
     * Horse is out of stable.
     */
    HORSE_OUT_OF_STABLE,
    
    /**
     * Horse goes out of paddock and proceeds to start line.
     */
    PADDOCK_PROCEED_TO_START_LINE,
    
    /**
     * Horse arrives at the racing track and proceeds to the start line.
     */
    RACING_TRACK_PROCEED_TO_START_LINE,
    
    /**
     * Horse is not on the start line anymore.
     */
    HORSE_OUT_OF_START_LINE,
    
    /**
     * Horse is at the finish line.
     */
    AT_THE_FINISH_LINE,
    
    /**
     * Get the winners from a race.
     */
    GET_THE_WINNERS,
    
    /**
     * Get the list of bets fro all spectators.
     */
    GET_SPECTATOR_BET_LIST,
    
    /**
     * Return the list of all the horse winners.
     */
    RETURN_WINNERS,
    
    /**
     * Return the list of all the bets done by the spectators.
     */
    RETURN_SPECTATOR_BET_LIST,
    
    /**
     * Ask if there are any winners.
     */
    ARE_ANY_WINNERS,
    
    /**
     * Answer to if there are any winners.
     */
    RETURN_ARE_ANY_WINNERS,
    
    /**
     * Broker honours the spectators bets.
     */
    HONOUR_THE_BETS,
    
    /**
     * Ask if the finish line has been crossed by a horse.
     */
    HAS_FINISHED_LINE_BEEN_CROSSED,
    
    /**
     * Answers if the finish line has been crossed by a horse.
     */
    RETURN_HAS_FINISHED_LINE_BEEN_CROSSED,
    
    /**
     * Horse makes a move.
     */
    MAKE_A_MOVE,
    
    /**
     * Answers if there is a next race.
     */
    RETURN_WAIT_FOR_NEXT_RACE,
    
    /**
     * Ask for all horses in the race.
     */
    GET_HORSES_IN_RACE,
    
    /**
     * Return a list of all horses in the race.
     */
    RETURN_HORSES_IN_RACE,
    
    /**
     * Place a bet on a horse.
     */
    PLACE_A_BET,
    
    /**
     * Collect the gains obtained by the bets in a race.
     */
    COLLECT_THE_GAINS,
    
    /**
     * Ask if i have won any bet.
     */
    HAVE_I_WON,
    
    /**
     * Answers to have i won any bet.
     */
    RETURN_HAVE_I_WON,
    
    /**
     * Returns the new distance and iterations made on a move.
     */
    RETURN_MAKE_A_MOVE,
    
    /**
     * Returns a bet made by a spectator.
     */
    RETURN_PLACE_A_BET,
    
    /**
     * Returns the gains made by a spectator with a bet.
     */
    RETURN_COLLECT_THE_GAINS,
    
    /**
     * Notify logger to update track.
     */
    UPDATE_TRACK,
    
    /**
     * Notify logger to clean the race variables.
     */
    CLEAN_RACE_VARIABLES,
    
    /**
     * Notify logger to update the broker state.
     */
    UPDATE_BROKER_STATE,
    
    /**
     * Notify logger to update the spectator state.
     */
    UPDATE_SPECTATOR_STATE,
    
    /**
     * Notify logger to update the horse state.
     */
    UPDATE_HORSE_STATE,
    
    /**
     * Notify logger to update the horses in race.
     */
    UPDATE_HORSE_IN_RACE,
    
    /**
     * Notify logger to update the bets from the spectators.
     */
    UPDATE_SPECTATOR_BET,
    
    /**
     * Notify logger to update the money from the spectators.
     */
    UPDATE_SPECTATOR_MONEY,
    
    /**
     * Notify logger to report the results from a race.
     */
    REPORT_RESULTS,
    
    /**
     * Horse notifies control center that he has ended his race day.
     */
    END_HORSE_RACE,
    
    /**
     * Control center notifies shared regions that his service has ended.
     */
    SERVICE_END
}
