
package SharedRegion;

import Communication.*;

/**
 * Paddock proxy for the logger shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class PaddockProxy implements ISharedRegion {
    
    /**
     * Paddock used to process the messages.
     */
    private final Paddock pd;
    
    /**
     * Paddock Proxy constructor.
     * @param pd padock to process the messages
     */
    public PaddockProxy(Paddock pd){
        this.pd = pd;
    }
    
    /**
     * Process and reply a message.
     * @param inMessage message received
     * @param scon communication channel
     * @return message to be replied
     */
    @Override
    public Message processAndReply(Message inMessage, ServerComm scon){
        Message outMessage = null;
        
        switch(inMessage.getType()){
            case PADDOCK_CHECK_HORSES:{
                pd.goCheckHorses();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case PADDOCK_PROCEED_TO_PADDOCK:{
                pd.proceedToPaddock();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case PADDOCK_PROCEED_TO_START_LINE:{
                pd.proceedToStartLine(inMessage.getHorseId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case HORSE_OUT_OF_START_LINE:{
                pd.horseOutOfStartLine();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case SERVICE_END:{
                pd.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            
        }
        return outMessage;
    }
    
}
