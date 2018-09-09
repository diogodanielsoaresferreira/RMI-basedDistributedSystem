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
 * Main Stable program.
 * Registers the stable in the register server and waits for a connection
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    /**
     * Used to check if the service must terminate.
     */
    public static boolean serviceEnd = false;
    
    /**
     * Main stable launcher
     * @param args args
     */
    public static void main(String [] args){
        
        /* get location of the registry service */
        String rmiRegHostName = SimulationParameters.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = SimulationParameters.REGISTRY_PORT;
        
        String nameEntryBase = SimulationParameters.REGISTRY_NAME_ENTRY;
        String nameEntryObject = SimulationParameters.STABLE_NAME_ENTRY;
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
        Stable stable = new Stable(loggerInt);
        StableInterface stableStub = null;
        
        try
        { stableStub = (StableInterface) UnicastRemoteObject.exportObject (stable, SimulationParameters.STABLE_PORT);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable stub generation exception: " + e.getMessage ());
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
        { registerInt.bind (nameEntryObject, stableStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Stable already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Stable object was registered!");
        
        /* Wait for the service to end */
        while(!serviceEnd){
            try {
                synchronized(stable){
                    stable.wait();
                }
            } catch (InterruptedException ex) {
                GenericIO.writelnString("Main thread of stable was interrupted.");
                System.exit(1);
            }
        }
        
        GenericIO.writelnString("Stable finished execution.");
        
        /* Unregister shared region */
        try
        { registerInt.unbind (nameEntryObject);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable unregistration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        } catch (NotBoundException ex) {
          GenericIO.writelnString ("Stable unregistration exception: " + ex.getMessage ());
          ex.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Stable object was unregistered!");
        
        /* Unexport shared region */
        try
        { UnicastRemoteObject.unexportObject (stable, false);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable unexport exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        GenericIO.writelnString ("Stable object was unexported successfully!");
        
    }
    
}
