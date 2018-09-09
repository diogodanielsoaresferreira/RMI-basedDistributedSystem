
package SharedRegion;

import Communication.*;

/**
 * Proxy for the control center shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class ControlCenterProxy implements ISharedRegion {
    
    /**
     * Control center used to process the messages.
     */
    private final ControlCenter cc;
    
    /**
     * Control Center Proxy constructor.
     * @param cc control center to process the messages
     */
    public ControlCenterProxy(ControlCenter cc){
        this.cc = cc;
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
            case CONTROL_CENTER_START_RACE:{
                cc.startTheRace();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CONTROL_CENTER_REPORT_RESULTS:{
                cc.reportResults();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CONTROL_CENTER_SUMMON_HORSES_TO_PADDOCK:{
                cc.summonHorsesToPaddock();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CONTROL_CENTER_END_RACES:{
                cc.endRaces();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case WAIT_FOR_NEXT_RACE:{
                outMessage = new Message(MessageType.RETURN_WAIT_FOR_NEXT_RACE, cc.waitForNextRace(inMessage.getSpectatorId(), inMessage.getSpectatorState()));
                break;
            }
            case CONTROL_CENTER_CHECK_HORSES:{
                cc.goCheckHorses(inMessage.getSpectatorId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CONTROL_CENTER_GO_WATCH_THE_RACE:{
                cc.goWatchTheRace();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case RELAX_A_BIT:{
                cc.relaxABit(inMessage.getSpectatorId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CONTROL_CENTER_PROCEED_TO_PADDOCK:{
                cc.proceedToPaddock(inMessage.getHorseId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case HORSE_OUT_OF_PADDOCK:{
                cc.horseOutOfPaddock();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case AT_THE_FINISH_LINE:{
                cc.atTheFinishLine(inMessage.getHorseId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case ARE_ANY_WINNERS:{
                outMessage = new Message(MessageType.RETURN_ARE_ANY_WINNERS, cc.areThereAnyWinners(inMessage.getWinners(), inMessage.getSpectatorBetList()));
                break;
            }
            case HAVE_I_WON:{
                outMessage = new Message(MessageType.RETURN_HAVE_I_WON, cc.haveIWon(inMessage.getSpectatorId(), inMessage.getWinners(), inMessage.getSpectatorBetList()));
                break;
            }
            case SERVICE_END:{
                cc.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
        }
        return outMessage;
    }
    
}
