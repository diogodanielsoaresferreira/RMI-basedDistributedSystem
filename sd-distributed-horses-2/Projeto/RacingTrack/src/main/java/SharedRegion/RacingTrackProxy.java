
package SharedRegion;

import Communication.*;

/**
 * Racing Track proxy for the logger shared region.
 * Implements the ISharedRegion interface, and listens to the requests,
 * processes them and replies.
 * @author Diogo Ferreira
 * @author Luís Leira
 */
public class RacingTrackProxy implements ISharedRegion {
    
    /**
     * Racing Track used to process the messages.
     */
    private final RacingTrack rt;
    
    /**
     * Racing Track Proxy constructor.
     * @param rt racing track to process the messages
     */
    public RacingTrackProxy(RacingTrack rt){
        this.rt = rt;
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
            case CALCULATE_TRACK_DISTANCE:{
                rt.calculateTrackDistance(inMessage.getTrackDistance());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case RACING_TRACK_START_RACE:{
                rt.startRace();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case END_RACE:{
                rt.endRace();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case RACING_TRACK_REPORT_RESULTS:{
                rt.reportResults();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case CLEAR_RACE_POSITIONS:{
                rt.clearRacePositions();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case RACING_TRACK_GO_WATCH_THE_RACE:{
                rt.goWatchTheRace(inMessage.getSpectatorId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case SPECTATOR_END_RACE:{
                rt.spectatorEndRace();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case ADD_HORSE_TO_RACE:{
                rt.addHorseToRace(inMessage.getHorse());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case RACING_TRACK_PROCEED_TO_START_LINE:{
                rt.proceedToStartLine(inMessage.getHorseId());
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            case GET_THE_WINNERS:{
                outMessage = new Message(MessageType.RETURN_WINNERS, rt.getTheWinners());
                break;
            }
            case HAS_FINISHED_LINE_BEEN_CROSSED:{
                outMessage = new Message(MessageType.RETURN_HAS_FINISHED_LINE_BEEN_CROSSED, rt.hasFinishedLineBeenCrossed(inMessage.getDistance()));
                break;
            }
            case MAKE_A_MOVE:{
                int [] updateParameters = rt.makeAMove(inMessage.getHorseId());
                outMessage = new Message(MessageType.RETURN_MAKE_A_MOVE, updateParameters[0], updateParameters[1]);
                break;
            }
            case GET_HORSES_IN_RACE:{
                outMessage = new Message(MessageType.RETURN_HORSES_IN_RACE, rt.getHorsesInRace());
                break;
            }
            case SERVICE_END:{
                rt.serviceEnd();
                outMessage = new Message(MessageType.STATUS_OK);
                break;
            }
            
        }
        return outMessage;
    }
    
}
