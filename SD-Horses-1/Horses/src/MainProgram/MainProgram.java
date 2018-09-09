
package MainProgram;

import InterveningEntities.*;
import SharedRegions.*;
import genclass.GenericIO;

/**
 * Main program class:
 * Main function launches all threads: a broker, horse/jockey pairs and spectators.
 * It uses the SimulationParemeters class to get the simulation parameters.
 * The shared regions are also initialized, as well the logger and its parameters.
 * 
 * @author Diogo Ferreira
 * @author Luis Leira
 */

public class MainProgram {

    /**
     *  Main method.
     *
     *  @param args runtime arguments
    */
    public static void main(String[] args) {
        /**
         * Confirm that the number of agilities equals the number of horses.
         */
        assert(SimulationParameters.HORSES_AGILITY.length==SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES);
        
        /**
         * Confirm that the number of spectators equals the number of betting strategies
         * and also equals to the number of items of the array with the money of the spectators.
         */
        assert(SimulationParameters.N_SPECTATORS==SimulationParameters.SPECTATORS_BETTING_STRATEGY.length);
        assert(SimulationParameters.N_SPECTATORS==SimulationParameters.SPECTATORS_MONEY.length);
        
        
        HorseJockey[] horses = new HorseJockey[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        Spectator[] spectators = new Spectator[SimulationParameters.N_SPECTATORS];
        
        
        /**
         * Problem Initialization.
         */
        
        BettingCenter bettingCenter = new BettingCenter();
        ControlCenter controlCenter = new ControlCenter();
        Paddock paddock = new Paddock();
        Stable stable = new Stable();
        RacingTrack racingTrack = new RacingTrack();
        
        
        Broker broker = new Broker(racingTrack, controlCenter, bettingCenter, stable);
        BrokerState brokerState = BrokerState.OPENING_THE_EVENT;
        
        int[] spectatorIds = new int[SimulationParameters.N_SPECTATORS];
        SpectatorState[] spectatorState = new SpectatorState[SimulationParameters.N_SPECTATORS];
        int[] spectatorMoney = SimulationParameters.SPECTATORS_MONEY;
        
        int[] horseIds = new int[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        int[] horsesAgility = SimulationParameters.HORSES_AGILITY;
        int[] horseRaceNumber = new int[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        HorseJockeyState[] horseState = new HorseJockeyState[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        
        
        for(int i = 0; i < horses.length; i++){
            horses[i] = new HorseJockey(i, i/SimulationParameters.N_HORSE_JOCKEY, paddock, racingTrack, stable, controlCenter);
            horseIds[i] = i;
            horseRaceNumber[i] = i/SimulationParameters.N_HORSE_JOCKEY;
            horseState[i] = HorseJockeyState.AT_THE_STABLE;
        }
        
        for(int i = 0; i < spectators.length; i++){
            spectators[i] = new Spectator(i, controlCenter, paddock, bettingCenter, racingTrack);
            spectatorIds[i] = i;
            spectatorState[i] = SpectatorState.WAITING_FOR_A_RACE_TO_START;
        }
        
        Logger logger = new Logger(spectatorIds, horseIds, horsesAgility, horseRaceNumber, brokerState, horseState, spectatorState, spectatorMoney);
        logger.initLog();
        
        bettingCenter.setLogger(logger);
        controlCenter.setLogger(logger);
        paddock.setLogger(logger);
        stable.setLogger(logger);
        racingTrack.setLogger(logger);
        
        /** 
         * Start of Simulation.
         */
        
        broker.start();
        
        for (HorseJockey horse : horses) {
            horse.start();
        }
        
        for (Spectator spectator : spectators) {
            spectator.start();
        }
        
        /**
         * Wait for the end of simulation.
         */
        
        for (HorseJockey horse : horses) {
            try{
                horse.join();
            }
            catch(InterruptedException e){
                GenericIO.writeString("Main Program - One thread of HorseJockey was interrupted.");
                System.exit(1);
            }
        }
        
        for (Spectator spectator : spectators) {
            try{
                spectator.join();
            }
            catch(InterruptedException e){
                GenericIO.writeString("Main Program - One thread of Spectator was interrupted.");
                System.exit(1);
            }
        }
        
        try{
            broker.join();
        }
        catch(InterruptedException e){
            GenericIO.writeString("Main Program - One thread of Broker was interrupted.");
            System.exit(1);
        }
    }
    
}
