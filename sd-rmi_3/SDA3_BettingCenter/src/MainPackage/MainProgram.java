package MainPackage;

import Interfaces.*;
import genclass.GenericIO;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Main Betting Center program.
 * Registers the betting center in the register server and waits for a connection
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
        String nameEntryObject = SimulationParameters.BETTING_CENTER_NAME_ENTRY;
        Registry registry = null;
        RegisterInterface registerInt = null;
        
        /* create and install the security manager */
        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        
        
        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry locate exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        /* Localize the logger in the RMI server by its name */
        LoggerInterface loggerInt = null;
        
        try
        { 
            loggerInt = (LoggerInterface) registry.lookup (SimulationParameters.LOGGER_NAME_ENTRY);
        }
        catch (RemoteException e)
        { 
            System.out.println("Logger lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Logger not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit(1);
        }
        
        /* Initialize the shared region */
        BettingCenter bettingCenter = new BettingCenter(loggerInt);
        BettingCenterInterface bettingCenterInt = null;
        
        try
        { bettingCenterInt = (BettingCenterInterface) UnicastRemoteObject.exportObject (bettingCenter, SimulationParameters.BETTING_CENTER_PORT);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Betting Center stub generation exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        /* register it with the general registry service */
        try
        { registerInt = (RegisterInterface) registry.lookup (nameEntryBase);
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
        { registerInt.bind (nameEntryObject, bettingCenterInt);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Betting Center registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Betting Center already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Betting Center object was registered!");
        
        /* Wait for the service to end */
        while(!serviceEnd){
            try {
                synchronized(bettingCenter){
                    bettingCenter.wait();
                }
            } catch (InterruptedException ex) {
                GenericIO.writelnString("Main thread of betting center was interrupted.");
                System.exit(1);
            }
        }
        
        GenericIO.writelnString("Betting center finished execution.");
        
        /* Unregister shared region */
        try
        { registerInt.unbind (nameEntryObject);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Betting Center unregistration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        } catch (NotBoundException ex) {
          GenericIO.writelnString ("Betting Center unregistration exception: " + ex.getMessage ());
          ex.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Betting Center object was unregistered!");
        
        /* Unexport shared region */
        try
        { UnicastRemoteObject.unexportObject (bettingCenter, false);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Betting Center unexport exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        GenericIO.writelnString ("Betting Center object was unexported successfully!");
        
    }
    
}
