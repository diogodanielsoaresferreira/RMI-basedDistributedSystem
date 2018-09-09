
package SharedRegion;

import Communication.*;

/**
 * Stable proxy for the logger shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class StableProxy implements ISharedRegion {
    
    /**
     * Stable used to process the messages.
     */
    private final Stable st;
    
    /**
     * Stable Proxy constructor.
     * @param st padock to process the messages
     */
    public StableProxy(Stable st){
        this.st = st;
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
            case SET_RACE_NUMBER:{
                st.setRaceNumber(inMessage.getRaceNumber());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case STABLE_SUMMON_HORSES_TO_PADDOCK:{
                st.summonHorsesToPaddock();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case ENTERTAIN_THE_GUESTS:{
                st.entertainTheGuests();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case PROCEED_TO_STABLE:{
                st.proceedToStable(inMessage.getHorseId(), inMessage.getRaceNumber(), inMessage.getHorseState());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case HORSE_OUT_OF_STABLE:{
                st.horseOutOfStable();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case SERVICE_END:{
                st.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            
        }
        return outMessage;
    }
    
}
