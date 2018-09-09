
package InterveningEntities;

import BettingHelper.Bet;
import MainProgram.SimulationParameters;
import SharedRegions.*;
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
    private final RacingTrack rt;
    
    /**
     * Instance of the control center.
     */
    private final ControlCenter cc;
    
    /**
     * Instance of the stable.
     */
    private final Stable st;
    
    /**
     * Instance of betting center.
     */
    private final BettingCenter bc;
    
    /**
     * Broker constructor
     * 
     * @param rt instance of the racing track
     * @param cc instance of the control center
     * @param bc instance of the betting center
     * @param st instance of the stable
     */
    public Broker(RacingTrack rt, ControlCenter cc, BettingCenter bc, Stable st){
        this.state = BrokerState.OPENING_THE_EVENT;
        this.rt = rt;
        this.cc = cc;
        this.bc = bc;
        this.st = st;
    }
    
    /**
     * Implements the life cycle of the broker.
     */
    @Override
    public void run(){
        
        for(int raceNumber=0; raceNumber<SimulationParameters.N_RACES; raceNumber++){
            
            /**
             * Calculate new track distance.
             */
            rt.calculateTrackDistance(raceNumber);
            st.setRaceNumber(raceNumber);
            
            /** 
             * Calls all horses from the stable.
             */
            st.summonHorsesToPaddock();
            
            /** 
             * Waits for all spectators to go to the paddock.
             */
            cc.summonHorsesToPaddock();
            
            /**
             * Accept the broker bets.
             */
            bc.acceptTheBets();
            
            /** 
             * Notify the horses to start the race.
             */
            rt.startRace();

            /** 
             * Wait for all horses to finish the race.
             */
            cc.startTheRace();
            
            /** 
             * End the race. Clear race variables.
             */
            rt.endRace();
            
            /** 
             * Report results of the race to the logger.
             */
            rt.reportResults();
            
            /** 
             * Notify all the spectators that the race has ended.
             */
            cc.reportResults();
            
            /**
             * Get the winner horses.
             */
            ArrayList<HorseJockey> winners = rt.getTheWinners();
            
            /**
             * Get the bet list.
             */
            ArrayList<Bet> bets = bc.getSpectatorBetList();
            
            /**
             * Checks if there are any winners.
             */
            if(cc.areThereAnyWinners(winners, bets)){
                /**
                 * Honour the bets of spectators.
                 */
                bc.honourTheBets(winners);
            }
            /**
             * Clear the positions of the horses on race.
             */
            rt.clearRacePositions();    
        }
        
        /**
         * Entertain the guests.
         */
        st.entertainTheGuests();
        
        /**
         * Clear the races variables.
         */
        cc.endRaces();
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
