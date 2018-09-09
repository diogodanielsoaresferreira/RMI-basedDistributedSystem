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
 * Main Control Center program.
 * Registers the control center in the register server and waits for a connection
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
        String nameEntryObject = SimulationParameters.CONTROL_CENTER_NAME_ENTRY;
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
        ControlCenter controlCenter = new ControlCenter(loggerInt);
        ControlCenterInterface controlCenterInt = null;
        
        try
        { controlCenterInt = (ControlCenterInterface) UnicastRemoteObject.exportObject (controlCenter, SimulationParameters.CONTROL_CENTER_PORT);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlCenter stub generation exception: " + e.getMessage ());
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
        { registerInt.bind (nameEntryObject, controlCenterInt);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control Center registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Control Center already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Control Center object was registered!");
        
        
        /* Wait for the service to end */
        while(!serviceEnd){
            try {
                synchronized(controlCenter){
                    controlCenter.wait();
                }
            } catch (InterruptedException ex) {
                GenericIO.writelnString("Main thread of control center was interrupted.");
                System.exit(1);
            }
        }
        
        GenericIO.writelnString("Control center finished execution.");
        
        /* Get the other shared regions to send service end message */
        BettingCenterInterface bettingCenterInt = null;
        PaddockInterface paddockInt = null;
        RacingTrackInterface racingTrackInt = null;
        StableInterface stableInt = null;
        
        try
        { bettingCenterInt = (BettingCenterInterface) registry.lookup (SimulationParameters.BETTING_CENTER_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Betting Center lookup exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Betting Center not bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        try
        { paddockInt = (PaddockInterface) registry.lookup (SimulationParameters.PADDOCK_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Paddock lookup exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Paddock not bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        try
        { racingTrackInt = (RacingTrackInterface) registry.lookup (SimulationParameters.RACING_TRACK_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Racing Track lookup exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Racing Track not bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        try
        { stableInt = (StableInterface) registry.lookup (SimulationParameters.STABLE_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable lookup exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Stable not bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        /* Send service end to other shared regions */
        try {
            loggerInt.serviceEnd();
            paddockInt.serviceEnd();
            racingTrackInt.serviceEnd();
            stableInt.serviceEnd();
            bettingCenterInt.serviceEnd();
        } catch (RemoteException ex) {
            GenericIO.writelnString ("Service end exception: " + ex.getMessage ());
            ex.printStackTrace ();
            System.exit (1);
        }
        
        /* Unregister Control Center */
        try
        { registerInt.unbind (nameEntryObject);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control Center unregistration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        } catch (NotBoundException ex) {
          GenericIO.writelnString ("Control Center unregistration exception: " + ex.getMessage ());
          ex.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("Control Center object was unregistered!");
        
        /* Unexport Control Center */
        try
        { UnicastRemoteObject.unexportObject (controlCenter, false);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control Center unexport exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        
        GenericIO.writelnString ("Control Center object was unexported successfully!");
    }
    
}
