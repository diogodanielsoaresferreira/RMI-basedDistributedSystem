package MainPackage;

import SharedRegion.Logger;
import SharedRegion.LoggerProxy;
import java.net.SocketTimeoutException;
import Communication.ServerComm;
import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import SharedRegion.ServiceProvider;

/**
 * Main Logger program.
 * Initializes the service provider and waits for a connection
 * in a communication channel.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    /**
     * Used to check if the service must terminate.
     */
    public static boolean serviceEnd = false;
    
    /**
     * Main control center launcher
     * @param args args
     */
    public static void main(String [] args){
        /**
         * Communication channels.
         */
        ServerComm scon, sconi;
        ServiceProvider sp;
        
        /**
         * Parameters initialization.
         */
        int[] spectatorIds = new int[SimulationParameters.N_SPECTATORS];
        int[] horseIds = new int[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        int[] horsesAgility = SimulationParameters.HORSES_AGILITY;
        int[] horseRaceNumber = new int[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        BrokerState brokerState = BrokerState.OPENING_THE_EVENT;
        HorseJockeyState[] horseState = new HorseJockeyState[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        SpectatorState[] spectatorState = new SpectatorState[SimulationParameters.N_SPECTATORS];
        int[] spectatorMoney = SimulationParameters.SPECTATORS_MONEY;
        
        for(int i = 0; i < SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES; i++){
            horseIds[i] = i;
            horseRaceNumber[i] = i/SimulationParameters.N_HORSE_JOCKEY;
            horseState[i] = HorseJockeyState.AT_THE_STABLE;
        }
        
        for(int i = 0; i < SimulationParameters.N_SPECTATORS; i++){
            spectatorIds[i] = i;
            spectatorState[i] = SpectatorState.WAITING_FOR_A_RACE_TO_START;
        }
        
        /**
         * Shared region and proxy initialization.
         */
        Logger logger = new Logger(spectatorIds, horseIds, horsesAgility, horseRaceNumber, brokerState, horseState, spectatorState, spectatorMoney);
        logger.initLog();
        
        LoggerProxy loggerInt = new LoggerProxy(logger);
        
        /**
         * Start listening on the communication channel.
         */
        scon = new ServerComm(SimulationParameters.LOGGER_PORT);
        scon.start();
        
        /**
         * While the service is not terminated, accept connections and send them
         * to the service provider.
         */
        while(!serviceEnd){
            try {
                sconi = scon.accept();
                sp = new ServiceProvider(sconi, loggerInt);
                sp.start();
            } catch (SocketTimeoutException ex) {
            }
            
        }
    }
    
}
