
package SharedRegion;

import Communication.Message;
import Communication.ServerComm;

/**
 * This interface represents a shared region interface
 * It has a method to process and reply messages.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public interface ISharedRegion {
    /**
     * Process and reply a message
     * @param inMessage message to be processed
     * @param scon communication channel
     * @return message to be replied
     */
    public Message processAndReply(Message inMessage, ServerComm scon);
}
