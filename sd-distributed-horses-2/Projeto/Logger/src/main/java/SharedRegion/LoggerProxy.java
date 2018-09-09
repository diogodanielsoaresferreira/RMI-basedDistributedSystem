
package SharedRegion;

import Communication.*;

/**
 * Proxy for the logger shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class LoggerProxy implements ISharedRegion {
    
    /**
     * Logger used to process the messages.
     */
    private final Logger logger;
    
    /**
     * Logger Proxy constructor.
     * @param logger logger to process the messages
     */
    public LoggerProxy(Logger logger){
        this.logger = logger;
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
            case UPDATE_TRACK:
                logger.updateTrack(inMessage.getTrackDistance(), inMessage.getRaceNumber());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case CLEAN_RACE_VARIABLES:
                logger.cleanRaceVariables();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_BROKER_STATE:
                logger.updateBrokerState(inMessage.getBrokerState());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_SPECTATOR_STATE:
                logger.updateSpectatorState(inMessage.getSpectatorId(), inMessage.getSpectatorState());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_HORSE_STATE:
                logger.updateHorseState(inMessage.getHorseId(), inMessage.getHorseState());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_HORSE_IN_RACE:
                logger.updateHorseInRace(inMessage.getHorseId(), inMessage.getIterations(), inMessage.getDistance());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_SPECTATOR_BET:
                logger.updateSpectatorBet(inMessage.getSpectatorId(), inMessage.getBetSelection(), inMessage.getBetAmmount());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case UPDATE_SPECTATOR_MONEY:
                logger.updateSpectatorMoney(inMessage.getSpectatorId(), inMessage.getMoney());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case REPORT_RESULTS:
                logger.reportResults(inMessage.getHorseIds(), inMessage.getHorsePositions());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            case SERVICE_END:
                logger.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            
        }
        return outMessage;
    }
    
}
