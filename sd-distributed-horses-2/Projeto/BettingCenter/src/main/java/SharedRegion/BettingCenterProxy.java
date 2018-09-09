
package SharedRegion;

import Communication.*;

/**
 * Proxy for the betting center shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class BettingCenterProxy implements ISharedRegion {
    
    /**
     * Betting center used to process the messages.
     */
    private final BettingCenter bc;
    
    /**
     * Betting Center Proxy constructor.
     * @param bc betting center to process the messages
     */
    public BettingCenterProxy(BettingCenter bc){
        this.bc = bc;
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
            case ACCEPT_THE_BETS:{
                bc.acceptTheBets();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case GET_SPECTATOR_BET_LIST:{
                outMessage = new Message(MessageType.RETURN_SPECTATOR_BET_LIST, bc.getSpectatorBetList());
                break;
            }
            case HONOUR_THE_BETS:{
                bc.honourTheBets(inMessage.getWinners());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case PLACE_A_BET:{
                outMessage = new Message(MessageType.RETURN_PLACE_A_BET, bc.placeABet(inMessage.getSpectatorId(), inMessage.getHorsesInRace(), inMessage.getBettingStrategy(), inMessage.getMoney()));
                break;
            }
            case COLLECT_THE_GAINS:{
                outMessage = new Message(MessageType.RETURN_COLLECT_THE_GAINS, bc.collectTheGains(inMessage.getSpectatorId()));
                break;
            }
            case SERVICE_END:{
                bc.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
        }
        return outMessage;
    }
    
}
