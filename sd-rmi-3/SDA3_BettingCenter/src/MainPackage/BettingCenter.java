package MainPackage;

import EntitiesState.BrokerState;
import EntitiesState.SpectatorState;
import HelperClasses.Bet;
import HelperClasses.BettingStrategy;
import HelperClasses.SerializableHorse;
import java.rmi.RemoteException;
import java.util.ArrayList;
import Interfaces.BettingCenterInterface;
import Interfaces.LoggerInterface;

/**
 * Manages the bets from the spectators to the broker.
 * It contains the logic necessary for a spectator to do a bet and collect the 
 * gains and for a broker to retrieve and honour the bets made.
 * It has a list of the bets done by the spectators, and also a list where are
 * the ID's of all spectators that are waiting for a bet to be retrieved.
 * 
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class BettingCenter implements BettingCenterInterface{
    
    /**
     * Logger class for debugging.
     */
    private final LoggerInterface loggerInt;
    
    /**
     * True if the broker is waiting for bets, false otherwise.
     * If is false, the spectator must wait before doing a bet.
     */
    private boolean waitingForBets = false;
    
    /**
     * Number of spectators that have bet in a race.
     */
    private int spectatorsBet = 0;
    
    /**
     * List of bets of the spectators.
     * Everytime a spectator does a bet, adds him to this list.
     * The broker reads the list and honours the bets that have won money.
     */
    private final ArrayList<Bet> spectatorBetList = new ArrayList<>();
    
    /**
     * ID of the spectators to collect gains.
     * When a spectator comes to retrieve the bets, it adds his id to this list,
     * and waits for the broker to honour the bets.
     */
    private final ArrayList<Integer> spectatorsToRetrieveBet = new ArrayList<>();
    
    /**
     * Number of bets that the broker has accepted in a race.
     */
    private int brokerBets = 0;
    
    /**
     * Bet ammount collected by the broker.
     * Bet ammount that is passed by the broker to the spectator on a winning bet.
     */
    private int betAmmount = -1;
    
    /**
     * Spectator ID to retrieve the bet ammount.
     */
    private int spectatorToRetrieveAmmount = -1;
    
    /**
     * Betting center constructor
     * @param logger logger to send the log messages
     */
    public BettingCenter(LoggerInterface logger){
        this.loggerInt = logger;
    }
    
    /**
     * Method used by the broker to accept all the bets from the spectators.
     * Waits for each spectator to come to the betting center and retrieves
     * the bets made by them.
     * Only returns when all spectators made his bets.
     */
    @Override
    public synchronized void acceptTheBets() throws RemoteException{
        
        loggerInt.updateBrokerState(BrokerState.WAITING_FOR_BETS);
        
        spectatorBetList.clear();
        
        /**
         * Wait for a bet of every spectator, and then block again
         * Only stops when all spectators have bet.
         */
        for(int i=0; i<SimulationParameters.N_SPECTATORS; i++){
            
            /**
             * Notifies all the spectators that the broker is waiting for bets.
             */
            waitingForBets = true;
            notifyAll();
            
            /** 
             * Waits for a bet of a spectator.
             **/
            while(spectatorsBet<=i){
                try{
                    wait();
                } catch (InterruptedException ex) {
                    System.exit(1);
                }
            }
            waitingForBets = false;
            
            /** 
             * Accept the bet and notify the spectator.
             */
            brokerBets += 1;
            notifyAll();
        }
        
    }
    
    /**
     * Get the list of bets from the spectators
     * @return list of bets from the spectators
     */
    @Override
    public synchronized ArrayList<Bet> getSpectatorBetList() throws RemoteException{
        return spectatorBetList;
    }
    
    /**
     * The broker gives the reward to the winning bets.
     * This method is called by the broker
     * Waits for each spectator to come retrieve his bet. When they are waiting
     * on the betting center, saves the ammount of money to give to them.
     * Only returns when all spectators come collect their gains.
     * @param winners list of all the horses that won the race
     */
    @Override
    public synchronized void honourTheBets(ArrayList<SerializableHorse> winners) throws RemoteException{
        
        loggerInt.updateBrokerState(BrokerState.SETTLING_ACCOUNTS);
        
        ArrayList<Bet> winningBets = getWinningBets(winners);
       
        int i=0;
        
        while(i<winningBets.size()){

            /**
             * Get the bet from the spectator and give the money ammount.
             */
            if(spectatorsToRetrieveBet.size()>0){
                int specID = spectatorsToRetrieveBet.get(0);
                for(Bet bet : winningBets){
                    /**
                     * Get the bet from the spectator.
                     */
                    if(specID==bet.getSpectatorID()){ 
                        /**
                         * Get the money ammount of the spectator that won the bet.
                         */
                        betAmmount = (int) (bet.getMoney()+bet.getMoney()/winningBets.size());
                        i++;
                        /**
                         * Notify the spectator that the reward is ready to be collected.
                         */
                        spectatorsToRetrieveBet.remove(0);
                        notifyAll();
                        
                        /**
                         * Wait for spectator to retrieve the bet ammount.
                         */
                        spectatorToRetrieveAmmount = specID;
                        while(spectatorToRetrieveAmmount != -1){
                            try{
                                wait();
                            } catch (InterruptedException ex) {
                                System.exit(1);
                            }
                        }
                        
                        break;
                    }
                }
            }
            else{
                /**
                 * Wait for bet to be collected.
                 */
                try{
                    wait();
                } catch (InterruptedException ex) {
                    System.exit(1);
                }
            }
            
        }
        
    }
    
    
    /**
     * Spectator places a bet.
     * Method used by the spectators to place a bet.
     * It waits for the broker to be ready to receive bets.
     * When the broker is ready, calculates the bet, based on his strategy, 
     * and adds the bet to the bet list.
     * Returns when the broker has accepted the bet.
     * @param id spectator id
     * @param horses list of horses in a race
     * @param strategy spectator strategy
     * @param money spectator money
     * @return bet amount
     */
    @Override
    public synchronized int placeABet(int id, ArrayList<SerializableHorse> horses, BettingStrategy strategy, int money) throws RemoteException{
        
        loggerInt.updateSpectatorState(id, SpectatorState.PLACING_A_BET);
        
        /**
         * Waits by the broker when is ready to receive bets.
         */
        while(!waitingForBets){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        /**
         * Add the bet to the list, and notify the broker.
         */
        Bet bet = BettingStrategy.applyStrategy(id, horses, strategy, money);
        
        spectatorBetList.add(bet);
        spectatorsBet += 1;
        loggerInt.updateSpectatorBet(id, bet.getHorseToBet().getID(), bet.getMoney());
        notifyAll();
        
        
        /** 
         * Waits for the broker to accept the bet.
         */
        while(spectatorsBet != brokerBets){
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        }
        
        /**
         * If the broker has accepted bets from all the spectators,
         * clear number of bets.
         */
        if (brokerBets==SimulationParameters.N_SPECTATORS){
            brokerBets = 0;
            spectatorsBet = 0;
        }
        return bet.getMoney();
    }
    
    /**
     * Spectator collects the gains from the bets.
     * Puts the spectator  in the list of the spectators waiting to retrieve bets.
     * When the broker has retrieved the bet, collects the gains given by the
     * broker and returns them.
     * @param id spectator id
     * @return money
     */
    @Override
    public synchronized int collectTheGains(int id) throws RemoteException{
        loggerInt.updateSpectatorState(id, SpectatorState.COLLECTING_THE_GAINS);
        
        /**
         * Notify the broker that the spectator is ready to retrieve the bet.
         */
        spectatorsToRetrieveBet.add(id);
        notifyAll();
        
        /**
         * If he is not in the list to retrieve bets, was already called.
         * Otherwise, he must wait by his turn.
         */
        while(spectatorsToRetrieveBet.stream().filter((spectatorBet) -> spectatorBet == id).count() != 0 || spectatorToRetrieveAmmount != id){
            /**
             * Wait for broker to honour the bets.
             */
            try{
                wait();
            } catch (InterruptedException ex) {
                System.exit(1);
            }
            
        }
        
        /**
         * Retrieve the bet ammount and notify the broker that the spectator has
         * already retrieved the bet.
         */
        int spectatorBetAmmount = betAmmount;
        loggerInt.updateSpectatorMoney(id, spectatorBetAmmount);
        spectatorToRetrieveAmmount = -1;
        notifyAll();
        
        return spectatorBetAmmount;
    }
    
    /**
     * Get the winning bets of a race.
     * @param raceWinners List of horses that have won the race
     * @return List of the winning bets
     */
    private synchronized ArrayList<Bet> getWinningBets(ArrayList<SerializableHorse> raceWinners) throws RemoteException{
        ArrayList<Bet> winningBets = new ArrayList<>();
        
        /**
         * For each winner horse, checks if there is any bet in that horse.
         */
        spectatorBetList.forEach((bet) -> {
            raceWinners.stream().filter((horse) -> (horse.getID()==bet.getHorseToBet().getID())).forEachOrdered((_item) -> {
                winningBets.add(bet);
            });
        });
        
        return winningBets;
    }
    
    /**
     * Stop the service and shut down the betting center.
     */
    @Override
    public synchronized void serviceEnd() throws RemoteException{
        MainProgram.serviceEnd = true;
        notifyAll();
    }
    
    
}

