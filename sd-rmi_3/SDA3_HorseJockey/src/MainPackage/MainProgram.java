
package MainPackage;

import Interfaces.*;
import genclass.GenericIO;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Main horses program.
 * Get the shared regions from the register and start horses lifecycles
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class MainProgram {
    
    public static void main(String [] args){
        
        
        RacingTrackInterface racingTrackInt = null;
        ControlCenterInterface controlCenterInt = null;
        StableInterface stableInt = null;
        PaddockInterface paddockInt = null;
        
        /* get location of the generic registry service */
        String rmiRegHostName = SimulationParameters.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = SimulationParameters.REGISTRY_PORT;

        /* look for the remote object by name in the remote host registry */
        String nameEntry = SimulationParameters.REGISTRY_NAME_ENTRY;
        Registry registry = null;
        
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
            stableInt = (StableInterface) registry.lookup (SimulationParameters.STABLE_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Stable is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Stable: " + ex.getMessage () );
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
        
        
        HorseJockey[] horses = new HorseJockey[SimulationParameters.N_HORSE_JOCKEY*SimulationParameters.N_RACES];
        
        for(int i = 0; i < horses.length; i++){
            horses[i] = new HorseJockey(i, i/SimulationParameters.N_HORSE_JOCKEY, paddockInt, racingTrackInt, stableInt, controlCenterInt);
        }
        
        GenericIO.writelnString ("Starting Horse...");
        
        /**
         * Horses lifecycles start.
         */
        for (HorseJockey horse : horses) {
            horse.start();
        }
        
        for (HorseJockey horse : horses) {
            try{
                horse.join();
            }
            catch (InterruptedException ex) {
            }
        }
        
        GenericIO.writelnString ("Horse ended lifecycle.");
        
        try {
            controlCenterInt.serviceEnd();
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while calling service end: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
    }
    
}
