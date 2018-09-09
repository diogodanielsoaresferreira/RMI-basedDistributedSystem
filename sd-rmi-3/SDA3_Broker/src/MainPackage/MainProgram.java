
package MainPackage;

import Interfaces.*;
import genclass.GenericIO;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Main broker program.
 * Get the shared regions from the register and start broker lifecycle
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    public static void main(String [] args){
        
        /* get location of the generic registry service */
        String rmiRegHostName = SimulationParameters.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = SimulationParameters.REGISTRY_PORT;

        /* look for the remote object by name in the remote host registry */
        String nameEntry = SimulationParameters.REGISTRY_NAME_ENTRY;
        Registry registry = null;
        
        RacingTrackInterface racingTrackInt = null;
        ControlCenterInterface controlCenterInt = null;
        BettingCenterInterface bettingCenterInt = null;
        StableInterface stableInt = null;
        try
        {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        GenericIO.writelnString ("RMI registry was created!");

        
        /* Look for the other entities in the registry */
        try
        {
            racingTrackInt = (RacingTrackInterface) registry.lookup (SimulationParameters.RACING_TRACK_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }

        try
        {
            controlCenterInt = (ControlCenterInterface) registry.lookup (SimulationParameters.CONTROL_CENTER_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Control Center is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Control Center: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        try
        {
            bettingCenterInt = (BettingCenterInterface) registry.lookup (SimulationParameters.BETTING_CENTER_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Betting Center is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Betting Center: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        try
        {
            stableInt = (StableInterface) registry.lookup (SimulationParameters.STABLE_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Stable is not registered: " + ex.getMessage ());
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Stable: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        GenericIO.writelnString ("Starting broker...");
        
        /**
         * Broker lifecycle start.
         */
        Broker broker = new Broker(racingTrackInt, controlCenterInt, bettingCenterInt, stableInt);
        broker.start();
        try {
            broker.join();
        } catch (InterruptedException ex) {
        }
        
        GenericIO.writelnString ("Broker ended lifecycle.");
        
        try {
            controlCenterInt.serviceEnd();
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while calling service end: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
    }
    
    
}
