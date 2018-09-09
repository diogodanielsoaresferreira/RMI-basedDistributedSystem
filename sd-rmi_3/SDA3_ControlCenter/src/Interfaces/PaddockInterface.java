
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Paddock interface to implement the stub for other entities.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface PaddockInterface extends Remote {
    
    /**
     * Spectator goes to the paddock check the horses.
     * Only returns when all horses are on the start line.
     * @throws java.rmi.RemoteException
     */
    public void goCheckHorses() throws RemoteException;
    
    /**
     * Horse/Jockey goes to paddock and waits for all spectators to reach the paddock.
     * @throws java.rmi.RemoteException
     */
    public void proceedToPaddock() throws RemoteException;
    
    /**
     * Horse proceed to start line.
     * @param id horse id
     * @throws java.rmi.RemoteException
     */
    public void proceedToStartLine(int id) throws RemoteException;
    
    /**
     * Horse in not on the start line anymore.
     * @throws java.rmi.RemoteException
     */
    public void horseOutOfStartLine() throws RemoteException;
    
    /**
     * Stop the service and shuts down the shared region.
     * @throws java.rmi.RemoteException
     */
    public void serviceEnd() throws RemoteException;
    
}
