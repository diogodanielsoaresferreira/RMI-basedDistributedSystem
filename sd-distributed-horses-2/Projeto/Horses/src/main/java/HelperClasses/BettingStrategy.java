
package HelperClasses;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class that contains the strategy for the spectator to bet on a race.
 * It contains an enum with the possible betting strategies by the spectator
 * and the application of those strategies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public enum BettingStrategy{
    /**
     * Strategy with low risk and low bets.
     * Always bets on the horse with the higher agility,
     * and bets 25% of his money.
     */
    LOW_RISK_AND_LOW_BETS,
    
    /**
     * Strategy with high risk and low bets.
     * Always bets on the second horse with the higher agility,
     * and bets 25% of his money.
     */
    HIGH_RISK_AND_LOW_BETS,
    
    /**
     * Strategy with low risk and high bets.
     * Always bets on the horse with the higher agility,
     * and bets 50% of his money.
     */
    LOW_RISK_AND_HIGH_BETS,
    
    /**
     * Strategy with high risk and high bets.
     * Always bets on the second horse with the higher agility,
     * and bets 50% of his money.
     */
    HIGH_RISK_AND_HIGH_BETS,
    
    /**
     * Strategy with horse chosen randomly, and low bets.
     * Bets on a random horse 25% of his money.
     */
    RANDOM_AND_LOW_BETS,
    
    /**
     * Strategy with horse chosen randomly, and high bets.
     * Bets on a random horse 50% of his money.
     */
    RANDOM_AND_HIGH_BETS;
    
    /**
     * Apply betting strategy of a spectator to return a new bet,
     * based on the horses currently on the race and on his current money.
     * Method called by the spectator to get a new bet, based on his strategy.
     * 
     * @param spectatorID ID of the spectator that did the bet
     * @param horses horses on the race
     * @param bettingStrategy betting strategy of the spectator
     * @param money current money of the spectator
     * @return a bet with the horse to bet and the money to bet
     */
    public static Bet applyStrategy(int spectatorID, ArrayList<SerializableHorse> horses, BettingStrategy bettingStrategy, int money){
        
        if(horses.isEmpty())
            return null;
        
        ArrayList<SerializableHorse> horsesOrdered = (ArrayList<SerializableHorse>)horses.clone();
        
        horsesOrdered.sort(Comparator.comparing(horse -> horse.getAgility()));
        
        SerializableHorse horseChosen;
        int moneyPercentage;
        
        switch (bettingStrategy) {
            case LOW_RISK_AND_HIGH_BETS:
                moneyPercentage = 50;
                horseChosen = horsesOrdered.get(horsesOrdered.size()-1);
                break;
            case LOW_RISK_AND_LOW_BETS:
                moneyPercentage = 25;
                horseChosen = horsesOrdered.get(horsesOrdered.size()-1);
                break;
            case HIGH_RISK_AND_LOW_BETS:
                moneyPercentage = 25;
                if(horses.size()>1){
                    horseChosen = horsesOrdered.get(horsesOrdered.size()-2);
                }
                else
                    horseChosen = horsesOrdered.get(horsesOrdered.size()-1);
                break;
            case HIGH_RISK_AND_HIGH_BETS:
                moneyPercentage = 50;
                if(horses.size()>1){
                    horseChosen = horsesOrdered.get(horsesOrdered.size()-2);
                }
                else
                    horseChosen = horsesOrdered.get(horsesOrdered.size()-1);
                break;
            case RANDOM_AND_LOW_BETS:
                moneyPercentage = 25;
                horseChosen = horsesOrdered.get((int)(Math.random()*horsesOrdered.size()));
                break;
            default:
                moneyPercentage = 50;
                horseChosen = horsesOrdered.get((int)(Math.random()*horsesOrdered.size()));
                break;
        }
        
        return new Bet(horseChosen, ((int) Math.round(moneyPercentage*money*0.01)), spectatorID);
    }
    
}
