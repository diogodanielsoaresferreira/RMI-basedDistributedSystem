package MainPackage;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Interfaces.RegisterInterface;


/**
 *  This data type defines a generic functionality to register remote objects in the local registry service.
 *  Communication is based in Java RMI.
 */

public class RegisterRemoteObject implements RegisterInterface
{
  /**
   *  Name of local host
   *
   *    @serialField rmiRegHostName
   */

   private String rmiRegHostName;

  /**
   *  Port number where the local registry service is listening
   *
   *    @serialField sconi
   */

   private int rmiRegPortNumb = 1099;
   
   /**
    * Number of unbinds allowed in registered. After this number of unbinds, service shuts down.
    */
   private int posssibleUnbinds = 6;

  /**
   *  Instantiation of a registering object.
   *
   *    @param rmiRegHostName name of local host
   *    @param rmiRegPortNumb port number where the local registry service is listening
     * @param possibleUnbinds number of unbinds allowed to this server register
   */

   public RegisterRemoteObject (String rmiRegHostName, int rmiRegPortNumb, int possibleUnbinds)
   {
     if ((rmiRegHostName == null) || ("".equals (rmiRegHostName)))
        throw new NullPointerException ("RegisterRemoteObject: null pointer parameter on instantiation!");
     this.rmiRegHostName = rmiRegHostName;
     if ((rmiRegPortNumb >= 4000) && (rmiRegPortNumb <= 65535))
        this.rmiRegPortNumb = rmiRegPortNumb;
     if(possibleUnbinds>0)
         this.posssibleUnbinds = possibleUnbinds;
   }

  /**
   *  Binds a remote reference to the specified name in this registry.
   *
   *    @param name the name to associate with the remote reference
   *    @param ref reference to the remote object
   *
   *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                            service fails
   *    @throws AlreadyBoundException if the name is already in use
   */

   @Override
   public void bind (String name, Remote ref) throws RemoteException, AlreadyBoundException
   {
     Registry registry;

     if ((name == null) || (ref == null))
        throw new NullPointerException ("RegisterRemoteObject: null pointer parameter(s) on on bind!");
     registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
     registry.bind (name, ref);
   }

 /**
   *  Removes the binding for the specified name in this registry.
   *
   *    @param name the name associated with the remote reference
   *
   *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                            service fails
   *    @throws NotBoundException if the name is not in use
   */

   @Override
   public synchronized void unbind (String name) throws RemoteException, NotBoundException
   {
     Registry registry;

     if ((name == null))
        throw new NullPointerException ("RegisterRemoteObject: null pointer parameter(s) on unbind!");
     registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
     registry.unbind (name);
     
     posssibleUnbinds--;
     if(posssibleUnbinds<=0){
        ServerRegisterRemoteObject.serviceEnd = true;
        notifyAll();
     }
   }

  /**
   *  Replaces the binding for the specified name in this registry with the supplied remote reference.
   *  If a previous binding for the specified name exists, it is discarded.
   *
   *    @param name the name to associate with the remote reference
   *    @param ref reference to the remote object
   *
   *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                            service fails
   */

   @Override
   public void rebind (String name, Remote ref) throws RemoteException
   {
     Registry registry;

     if ((name == null) || (ref == null))
        throw new NullPointerException ("RegisterRemoteObject: null pointer parameter(s) on rebind!");
     registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
     registry.rebind (name, ref);
   }
   
   
}
