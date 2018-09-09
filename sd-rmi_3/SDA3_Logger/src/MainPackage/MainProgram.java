package MainPackage;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import Interfaces.*;
import genclass.GenericIO;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Main Logger program.
 * Registers the logger in the register server and waits for connections
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
        
        /* get location of the registry service */
        String rmiRegHostName = SimulationParameters.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = SimulationParameters.REGISTRY_PORT;
        
        String nameEntryBase = SimulationParameters.REGISTRY_NAME_ENTRY;
        String nameEntryObject = SimulationParameters.LOGGER_NAME_ENTRY;
        Registry registry = null;
        RegisterInterface registerInt = null;
        
        /* create and install the security manager */
        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        
        /* Get the RMI server registry */
        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry locate exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }

        
        /**
         * Shared region initialization.
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
        
        Logger logger = new Logger(spectatorIds, horseIds, horsesAgility, horseRaceNumber, brokerState, horseState, spectatorState, spectatorMoney);
        LoggerInterface loggerInt = null;
        
        
        /**
         * Export logger to the registry
         */
        try
        { loggerInt = (LoggerInterface) UnicastRemoteObject.exportObject (logger, SimulationParameters.LOGGER_PORT);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Logger stub generation exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        /* register it with the general registry service */
        try
        {   
            registerInt = (RegisterInterface) registry.lookup (nameEntryBase);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Register lookup exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Register not bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        try
        {   
            registerInt.bind (nameEntryObject, loggerInt);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Logger registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Logger already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Logger object was registered!");
        
        /* Wait for the service to end */
        while(!serviceEnd){
            try {
                synchronized(logger){
                    logger.wait();
                }
            } catch (InterruptedException ex) {
                GenericIO.writelnString("Main thread of logger was interrupted.");
                System.exit(1);
            }
        }
        
        GenericIO.writelnString("Logger finished execution.");
        
        /* Unregister shared region */
        try
        { registerInt.unbind (nameEntryObject);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Logger unregistration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        } catch (NotBoundException ex) {
          GenericIO.writelnString ("Logger unregistration exception: " + ex.getMessage ());
          ex.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Logger object was unregistered!");
        
        /* Unexport shared region */
        try
        { UnicastRemoteObject.unexportObject (logger, false);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Logger unexport exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        GenericIO.writelnString ("Logger object was unexported successfully!");
        
    }
    
}
