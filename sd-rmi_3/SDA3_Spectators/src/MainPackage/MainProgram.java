package MainPackage;

import Interfaces.*;
import genclass.GenericIO;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Main spectators program.
 * Get the shared regions from the register and start spectators lifecycle
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
        PaddockInterface paddockInt = null;
        try
        {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry locate exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }

        
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
            paddockInt = (PaddockInterface) registry.lookup (SimulationParameters.PADDOCK_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Paddock is not registered: " + ex.getMessage ());
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Paddock: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("Starting spectators...");
        
        Spectator[] spectators = new Spectator[SimulationParameters.N_SPECTATORS];
        
        for(int i = 0; i < spectators.length; i++){
            spectators[i] = new Spectator(i, controlCenterInt, paddockInt, bettingCenterInt, racingTrackInt);
        }
        
        /**
         * Spectators lifecycle start.
         */
        for (Spectator spectator : spectators) {
            spectator.start();
        }
        
        for (Spectator spectator : spectators) {
            try{
                spectator.join();
            }catch (InterruptedException ex) {
            }
        }
        
        GenericIO.writelnString ("Spectator ended lifecycle.");
        
        try {
            controlCenterInt.serviceEnd();
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while calling service end: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
    }
    
}
