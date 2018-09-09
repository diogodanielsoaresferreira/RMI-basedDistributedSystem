package MainPackage;

import EntitiesState.BrokerState;
import HelperClasses.*;
import Interfaces.*;
import genclass.GenericIO;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Broker thread:
 * Implements the life-cycle of the broker and stores the variables referent to
 * the broker and his current state.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */

public class Broker extends Thread {

    /**
     * Current broker state.
     */
    private BrokerState state;
    
    /**
     * Instance of racing track.
     */
    private final RacingTrackInterface racingTrackInt;
    
    /**
     * Instance of the control center.
     */
    private final ControlCenterInterface controlCenterInt;
    
    /**
     * Instance of the stable.
     */
    private final StableInterface stableInt;
    
    /**
     * Instance of betting center.
     */
    private final BettingCenterInterface bettingCenterInt;
    
    /**
     * Broker constructor
     * 
     * @param rt instance of the racing track
     * @param cc instance of the control center
     * @param bc instance of the betting center
     * @param st instance of the stable
     */
    public Broker(RacingTrackInterface rt, ControlCenterInterface cc, BettingCenterInterface bc, StableInterface st){
        this.state = BrokerState.OPENING_THE_EVENT;
        this.racingTrackInt = rt;
        this.controlCenterInt = cc;
        this.bettingCenterInt = bc;
        this.stableInt = st;
    }
    
    /**
     * Implements the life cycle of the broker.
     */
    @Override
    public void run(){
        
        try {
            for(int raceNumber=0; raceNumber<SimulationParameters.N_RACES; raceNumber++){
            
                /**
                 * Calculate new track distance.
                 */
                racingTrackInt.calculateTrackDistance(raceNumber);
                setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
                stableInt.setRaceNumber(raceNumber);
            
                /** 
                 * Calls all horses from the stable.
                 */
                stableInt.summonHorsesToPaddock();

                /** 
                 * Waits for all spectators to go to the paddock.
                 */
                controlCenterInt.summonHorsesToPaddock();

                /**
                 * Accept the broker bets.
                 */
                bettingCenterInt.acceptTheBets();
                setBrokerState(BrokerState.WAITING_FOR_BETS);

                /** 
                 * Notify the horses to start the race.
                 */
                racingTrackInt.startRace();
                setBrokerState(BrokerState.SUPERVISING_THE_RACE);

                /** 
                 * Wait for all horses to finish the race.
                 */
                controlCenterInt.startTheRace();

                /** 
                 * End the race. Clear race variables.
                 */
                racingTrackInt.endRace();

                /** 
                 * Report results of the race to the logger.
                 */
                racingTrackInt.reportResults();

                /** 
                 * Notify all the spectators that the race has ended.
                 */
                controlCenterInt.reportResults();

                /**
                 * Get the winner horses.
                 */
                ArrayList<SerializableHorse> winners = racingTrackInt.getTheWinners();

                /**
                 * Get the bet list.
                 */
                ArrayList<Bet> bets = bettingCenterInt.getSpectatorBetList();

                /**
                 * Checks if there are any winners.
                 */
                if(controlCenterInt.areThereAnyWinners(winners, bets)){
                    /**
                     * Honour the bets of spectators.
                     */
                    bettingCenterInt.honourTheBets(winners);
                    setBrokerState(BrokerState.SETTLING_ACCOUNTS);
                }
                /**
                 * Clear the positions of the horses on race.
                 */
                racingTrackInt.clearRacePositions();    
            }

            /**
             * Entertain the guests.
             */
            stableInt.entertainTheGuests();
            setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);

            /**
             * Clear the races variables.
             */
            controlCenterInt.endRaces();
            
        } catch (RemoteException ex) {
            GenericIO.writelnString ("Remote exception: " + ex.getMessage ());
            ex.printStackTrace ();
            System.exit (1);
        }
            
    }
    
    /**
     * Get the broker state
     * 
     * @return broker state
     */
    public BrokerState getBrokerState(){
        return this.state;
    }
    
    /**
     * Set the broker state
     * @param state broker state
     */
    public void setBrokerState(BrokerState state){
        this.state = state;
    }
    
}
