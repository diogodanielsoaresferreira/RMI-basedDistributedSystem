
package Stubs;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageType;
import EntitiesState.*;

/**
 * Logger stub. Class used to communicate with the logger
 * using TCP communication channels.
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class Logger {
    
    /**
     * Name of the computational system where it is located the server.
     */
    private final String serverHostName;

    /**
     * Number of server listening port.
     */
    private final int serverPortNumb;
    
    /**
     *  Stub instatiation.
     *
     *    @param hostName Name of the computational system where it is located the server.
     *    @param port Number of server listening port.
     */
    public Logger (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }
    
    /**
     * Cleans the variables of the last race.
     */
    public void cleanRaceVariables(){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.CLEAN_RACE_VARIABLES);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Update the current Track distance and the race number
     * @param trackDistance distance of the current track
     * @param raceNumber new race number
     */
    public void updateTrack(int trackDistance, int raceNumber){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_TRACK, trackDistance, raceNumber);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the state of the broker
     * @param state new broker state
     */
    public void updateBrokerState(BrokerState state){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_BROKER_STATE, state);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the state of the spectator
     * @param id id of the spectator
     * @param state new spectator state
     */
    public void updateSpectatorState(int id, SpectatorState state){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_SPECTATOR_STATE, id, state);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the state of a horse/jockey
     * @param id id of the horse/jockey
     * @param state new horse/jockey state
     */
    public void updateHorseState(int id, HorseJockeyState state){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_HORSE_STATE, id, state);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the horse variables in a race
     * @param id id of the horse
     * @param iteration number of iteration that the horse has made
     * @param distance distance that the horse has travelled
     */
    public void updateHorseInRace(int id, int iteration, int distance){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_HORSE_IN_RACE, id, distance, iteration);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the spectator money
     * @param id id of the spectator
     * @param money new money ammount
     */
    public void updateSpectatorMoney(int id, int money){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_SPECTATOR_MONEY, id, money);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Updates the bet of a new spectator.
     * The bet ammount will be automatically gathered from the spectator money
     * @param id id of the spectator
     * @param betSel bet selection of the spectator
     * @param betAmmount ammount of the bet that the spectator has made
     */
    public void updateSpectatorBet(int id, int betSel, int betAmmount){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.UPDATE_SPECTATOR_BET, id, betSel, betAmmount);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
    
    /**
     * Report the results of the race.
     * @param horseIds id of the horses in the race
     * @param horsePositions position of the horses in the race
     */
    public void reportResults(int[] horseIds, int[] horsePositions){
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        while(!com.open()){
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException ex) {
            }
        }
        Message msg = new Message(MessageType.REPORT_RESULTS, horseIds, horsePositions);
        com.writeObject(msg);
        Message inMessage = (Message) com.readObject();
        com.close ();
    }
}
