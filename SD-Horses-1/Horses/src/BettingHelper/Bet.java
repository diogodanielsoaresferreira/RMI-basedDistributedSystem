
package BettingHelper;

import InterveningEntities.HorseJockey;

/**
 * Class that represents a bet from a spectator.
 * It stores the spectator ID that has made the bet, the money ammount of
 * the bet and the horse/jockey on which the spectator is betting.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class Bet {
    
    /**
     * Horse that the spectator will bet on.
     */
    private final HorseJockey horseToBet;
    
    /**
     * Money ammount that the spectator will bet on the horse.
     */
    private final int money;
    
    /**
     * ID of the spectator that does the bet.
     */
    private final int spectatorID;
    
    
    /**
     * Constructor
     * @param horseToBet horse to bet on by the spectator
     * @param money money that the spectator will bet
     * @param spectatorID ID of the spectator that does the bet
     */
    public Bet(HorseJockey horseToBet, int money, int spectatorID) {
        assert(money>=0);
        
        this.horseToBet = horseToBet;
        this.money = money;
        this.spectatorID = spectatorID;
    }
    
    /**
     * Get the horse that the spectator is betting on.
     * @return horse to bet
     */
    public HorseJockey getHorseToBet() {
        return horseToBet;
    }
    
    /**
     * Get the money of the bet on a horse by a spectator.
     * @return money from the spectator to bet on a horse
     */
    public int getMoney() {
        return money;
    }

    /**
     * Get the ID of the spectator that did the bet.
     * @return ID of the spectator
     */
    public int getSpectatorID(){
        return spectatorID;
    }

}
