
package MainPackage;


import EntitiesState.SpectatorState;
import HelperClasses.*;
import Stubs.*;
import java.util.ArrayList;


/**
 * Spectator thread:
 * Implements the life-cycle of a spectator and stores his internal variables
 * and his state during his lifecycle.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Spectator extends Thread {
    /**
     * Identifier of the spectator.
     */
    private final int id;
    
    /**
     * Betting strategy.
     */
    private final BettingStrategy bettingStrategy;
    
    /**
     * Current state of the spectator.
     */
    private SpectatorState state;
    
    /**
     * The money that the spectator has at the moment.
     */
    private int money;
    
    /**
     * Instance of the Control Center.
     */
    private final ControlCenter cc;
    
    /**
     * Instance of the Paddock.
     */
    private final Paddock pd;
    
    /**
     * Instance of the Betting Center.
     */
    private final BettingCenter bc;
    
    /**
     * Instance of the Racing Track.
     */
    private final RacingTrack rt;

    /**
     * Spectator constructor
     * 
     * @param id spectator id
     * @param cc instance of the ControlCenter
     * @param pd instance of the Paddock
     * @param bc instance of the BettingCenter
     * @param rt instance of the RacingTrack
     */
    public Spectator(int id, ControlCenter cc, Paddock pd, BettingCenter bc, RacingTrack rt){
        assert(money>0);
        
        this.id = id;
        this.money = SimulationParameters.SPECTATORS_MONEY[id];
        this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
        this.bettingStrategy = SimulationParameters.SPECTATORS_BETTING_STRATEGY[id];
        this.cc = cc;
        this.pd = pd;
        this.bc = bc;
        this.rt = rt;
    }
    
    /**
     * Implements the life-cycle of the spectator.
     */
    @Override
    public void run(){
        /**
         * Checks if there is a next race. If is true,
         * waits for all horses to be on paddock.
         */
        while(cc.waitForNextRace(id, state)){
            
            /**
             * When all spectators are on paddock, notify broker.
             */
            cc.goCheckHorses(id);
            
            /**
             * When all spectators are on paddock, notify horses.
             * Wait for all horses to be on the start line.
             */
            pd.goCheckHorses();
            
            /**
             * Waits by the broker to be ready to receive bets.
             * Sends a list of the horses in race for the spectator to be ready
             * to bet on the best horse.
             */
            bc.placeABet(id, rt.getHorsesInRace(), bettingStrategy, money);
            
            /**
             * Warn all horses that he will be watching the race.
             */
            rt.goWatchTheRace(id);
            
            /**
             * Spectator goes to watch the race and waits for the results of the broker.
             */
            cc.goWatchTheRace();
            
            /**
             * Get the winning horses.
             */
            ArrayList<SerializableHorse> winners = rt.getTheWinners();
            
            /**
             * Get the list of bets of the spectators.
             */
            ArrayList<Bet> bets = bc.getSpectatorBetList();
            
            /**
             * If the spectator won, collect the gains given by the broker.
             */
            if(cc.haveIWon(id, winners, bets)){
                bc.collectTheGains(id);
            }
            
            /**
             * Spectator signals that he is no longer paying attention to the race.
             * He is ready for another one.
             */
            rt.spectatorEndRace();
        }
        
        /**
         * Spectator relaxes after a race.
         */
        cc.relaxABit(id);
    }
    
    /**
     * Get the spectator state
     * 
     * @return spectator state
     */
    public SpectatorState getSpectatorState(){
        return this.state;
    }
    
    /**
     * Get the spectator money
     * 
     * @return current money of the spectator
     */
    public int getMoney(){
        return money;
    }
    
    /**
     * Get the ID of the spectator
     * 
     * @return ID of the spectator
     */
    public int getID(){
        return this.id;
    }
    
    /**
     * Add money to the spectator.
     * @param money money to add to the spectator
     */
    public void addMoney(int money){
        this.money += money;
        if(this.money<0)
            this.money = 0;
    }
    
    /**
     * Get he betting strategy of the spectator.
     * @return betting spectator
     */
    public BettingStrategy getBettingStrategy(){
        return this.bettingStrategy;
    }
    
    
    /**
     * Set the spectator state
     * @param state new state of the spectator
     */
    public void setSpectatorState(SpectatorState state){
        this.state = state;
    }
    
}
