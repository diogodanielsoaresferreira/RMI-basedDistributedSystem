
package MainPackage;

import EntitiesState.BrokerState;
import EntitiesState.HorseJockeyState;
import EntitiesState.SpectatorState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import Interfaces.LoggerInterface;

/**
 * Logger for the program.
 * Writes the logs to a file specified on the SimulationParameters class.
 * Also has the intervining entities and the shared regions needed to print the logs.
 * 
 * @author Diogo Ferreira
 * @author Luis Leira
 */
public class Logger implements LoggerInterface {
    
    /**
     * Distance of the current track.
     */
    private int trackDistance;
    
    /**
     * Current race number.
     */
    private int raceNumber;
    
    /**
     * List with the IDs of all the spectators.
     */
    private final int[] spectatorIds;
    
    /**
     * List with the IDs of all the horses.
     */
    private final int[] horseIds;
    
    /**
     * List with the agilities of all the horses.
     */
    private final int[] horsesAgility;
    
    /**
     * Race number of the horses.
     */
    private final int[] horseRaceNumber;
    
    /**
     * Current broker state.
     */
    private BrokerState brokerState;
    
    /**
     * Current horses state.
     */
    private final HorseJockeyState[] horseState;
    
    /**
     * Current spectators state.
     */
    private final SpectatorState[] spectatorState;
    
    /**
     * Money of all the spectators.
     */
    private final int[] spectatorMoney;
    
    /**
     * Number of iteration of a horse in a race.
     */
    private final int[] horseNumberOfIterations;
    
    /**
     * Distance travelled by the horse in a race.
     */
    private final int[] horseDistance;
    
    /**
     * Horse track position in the current race.
     */
    private final int[] horseTrackPosition;
    
    /**
     * Bet selection of the spectators.
     */
    private final int[] spectatorBetSelection;
    
    /**
     * Bet ammount of the spectators.
     */
    private final int[] spectatorBetAmmount;
    
    /**
     * Logger constructor
     * 
     * @param spectatorIds id's of all spectators
     * @param horseIds id's of all horses
     * @param horsesAgility agility of the horses
     * @param horseRaceNumber race number of the horses
     * @param brokerState State of the broker
     * @param horseState List with state of the horses
     * @param spectatorState List with state of the spectators
     * @param spectatorMoney Money of all the spectators
     */
    public Logger(int[] spectatorIds, int[] horseIds, int[] horsesAgility, int[] horseRaceNumber,
            BrokerState brokerState, HorseJockeyState[] horseState, SpectatorState[] spectatorState, int[] spectatorMoney){
        this.raceNumber = 0;
        this.trackDistance = -1;
        this.spectatorIds = spectatorIds;
        this.horseIds = horseIds;
        this.horsesAgility = horsesAgility;
        this.horseRaceNumber = horseRaceNumber;
        this.brokerState = brokerState;
        this.horseState = horseState;
        this.spectatorState = spectatorState;
        this.spectatorMoney = spectatorMoney;
        this.horseNumberOfIterations = new int[horseIds.length];
        this.horseDistance = new int[horseIds.length];
        this.horseTrackPosition = new int[horseIds.length];
        
        for(int i=0; i<horseIds.length; i++){
            horseNumberOfIterations[i] = 0;
            horseDistance[i] = 0;
            horseTrackPosition[i] = -1;
        }
        
        this.spectatorBetAmmount = new int[spectatorIds.length];
        this.spectatorBetSelection = new int[spectatorIds.length];
        
        for(int i=0; i<spectatorIds.length; i++){
            spectatorBetAmmount[i] = -1;
            spectatorBetSelection[i] = -1;
        }
        this.initLog();
    }
    
    /**
     * Cleans the log file and logs the description of the problem and the header
     * of the log file.
     */
    private synchronized void initLog(){
        
        try {
            FileWriter fw = new FileWriter(SimulationParameters.FILE_NAME);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Afternoon At The Race Track - Description of the internal state of the problem");
            bw.newLine();
            
            bw.write("Stat");

            for (int i=0; i<spectatorIds.length; i++) {
                bw.write(" ST" + String.format("%02d", spectatorIds[i]));
                bw.write("  AM" + String.format("%02d", spectatorIds[i]));
            }

            bw.write(" RN");

            for (int i=0; i<horseIds.length; i++) {
                if (horseRaceNumber[i]==raceNumber){
                    bw.write(" ST" + String.format("%02d", horseIds[i]));
                    bw.write(" Len" + String.format("%02d", horseIds[i]));
                }
               
            }

            bw.newLine();
            
            bw.write("RN");
            bw.write(" Dist");
            for (int i=0; i<spectatorIds.length; i++) {
                bw.write(" BS" + String.format("%02d", spectatorIds[i]));
                bw.write(" BA" + String.format("%02d", spectatorIds[i]));
            }

            for (int i=0; i<horseIds.length; i++) {
                if (horseRaceNumber[i]==raceNumber){
                    bw.write(" OD" + String.format("%02d", horseIds[i]));
                    bw.write(" N" + String.format("%02d", horseIds[i]));
                    bw.write(" PS" + String.format("%02d", horseIds[i]));
                    bw.write(" SD" + String.format("%02d", horseIds[i]));
                }
            }
            bw.newLine();
            
            bw.close();
            fw.close();
        } catch (IOException ex) {
            System.exit(1);
        }
        
    }
    
    /**
     * Logs the current state of the entities and the state of the race,
     * to a file specified in the simulation parameters.
     */
    private synchronized void printLog(){
        try{
            FileWriter fw = new FileWriter(SimulationParameters.FILE_NAME, true);
        
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(brokerState.toString());
        
            for (int i=0; i<spectatorIds.length; i++) {
                bw.write("  " + spectatorState[i]);
                bw.write("  " + String.format("%-4d", spectatorMoney[i]));
            }
            
            bw.write("  "+(raceNumber+1));
            
            for(int i=0; i<horseIds.length; i++){
                if (horseRaceNumber[i]==raceNumber){
                    bw.write("  " + horseState[i]);
                    bw.write("   " + String.format("%2d", horsesAgility[i]));
                }
            }
            bw.newLine();
            
            if(raceNumber != -1)
                bw.write(String.format("%02d", raceNumber+1));
            else
                bw.write("- ");
            
            if(trackDistance != -1)
                bw.write(" "+String.format("%4d", trackDistance)+" ");
            else
                bw.write("  -   ");
            
            for(int i=0; i<spectatorIds.length; i++){
                if(spectatorBetSelection[i]==-1 || raceNumber==-1){
                    bw.write("   - -    ");
                }
                else{
                    bw.write(String.format("%4d", spectatorBetSelection[i]) + " ");
                    bw.write(String.format("%-4d", spectatorBetAmmount[i]) + " ");
                }
                
            }
            bw.write(" ");
            
            double allAgilities = 0;
            for(int i=0; i<horsesAgility.length; i++)
                if (horseRaceNumber[i]==raceNumber){
                    allAgilities += horsesAgility[i];
                }
            
            for(int i=0; i<horseIds.length; i++){
                if (horseRaceNumber[i]==raceNumber){
                    bw.write(String.format("%2.2f", 1/(horsesAgility[i]/allAgilities)));
                    if(horseNumberOfIterations[i] != 0){
                        bw.write(" "+String.format("%-3d", horseNumberOfIterations[i]));
                        bw.write(" "+String.format("%-4d", horseDistance[i]));
                    }
                    else{
                        bw.write(" -   -   ");
                    }
                    if(horseTrackPosition[i] == -1)
                        bw.write(" -    ");
                    else
                        bw.write(" "+String.format("%-4d", horseTrackPosition[i])+" ");
                }
            }
            
            bw.newLine();
            bw.close();
            fw.close();
        }
        catch(IOException e){
            System.exit(1);
        }
    }
    
    /**
     * Update the current Track distance and the race number
     * @param trackDistance distance of the current track
     * @param raceNumber new race number
     */
    @Override
    public synchronized void updateTrack(int trackDistance, int raceNumber) throws RemoteException{
        this.trackDistance = trackDistance;
        this.raceNumber = raceNumber;
        
    }
    
    /**
     * Cleans the variables of the last race.
     */
    @Override
    public synchronized void cleanRaceVariables() throws RemoteException{
        /**
         * Clean the bets of the spectators.
         */
        for(int i=0; i<spectatorBetSelection.length; i++){
            spectatorBetSelection[i] = -1;
            spectatorBetAmmount[i] = -1;
        }
    }
    
    
    /**
     * Updates the state of the broker
     * @param state new broker state
     */
    @Override
    public synchronized void updateBrokerState(BrokerState state) throws RemoteException{
        this.brokerState = state;
        this.printLog();
    }
    
    /**
     * Updates the state of the spectator
     * @param id id of the spectator
     * @param state new spectator state
     */
    @Override
    public synchronized void updateSpectatorState(int id, SpectatorState state) throws RemoteException{
        for(int i=0; i<spectatorIds.length; i++)
            if(spectatorIds[i]==id && spectatorState[i] != state){
                spectatorState[i] = state;
                this.printLog();
            }
    }
    
    /**
     * Updates the state of a horse/jockey
     * @param id id of the horse/jockey
     * @param state new horse/jockey state
     */
    @Override
    public synchronized void updateHorseState(int id, HorseJockeyState state) throws RemoteException{
        for(int i=0; i<horseIds.length; i++)
            if(horseIds[i]==id)
                horseState[i] = state;
        this.printLog();
    }
    
    /**
     * Updates the horse variables in a race
     * @param id id of the horse
     * @param iteration number of iteration that the horse has made
     * @param distance distance that the horse has travelled
     */
    @Override
    public synchronized void updateHorseInRace(int id, int iteration, int distance) throws RemoteException{
        for(int i=0; i<horseIds.length; i++)
            if(horseIds[i]==id){
                horseNumberOfIterations[i] = iteration;
                horseDistance[i] = distance;
                horseState[i] = HorseJockeyState.RUNNING;
            }
        this.printLog();
    }
    
    /**
     * Updates the bet of a new spectator.
     * The bet ammount will be automatically gathered from the spectator money
     * @param id id of the spectator
     * @param betSel bet selection of the spectator
     * @param betAmmount ammount of the bet that the spectator has made
     */
    @Override
    public synchronized void updateSpectatorBet(int id, int betSel, int betAmmount) throws RemoteException{
        for(int i=0; i<spectatorIds.length; i++)
            if(spectatorIds[i]==id){
                spectatorMoney[i] -= betAmmount;
                spectatorBetAmmount[i] = betAmmount;
                spectatorBetSelection[i] = betSel;
            }
        printLog();
    }
    
    /**
     * Updates the spectator money
     * @param id id of the spectator
     * @param money new money ammount
     */
    @Override
    public synchronized void updateSpectatorMoney(int id, int money) throws RemoteException{
        for(int i=0; i<spectatorIds.length; i++)
            if(spectatorIds[i]==id)
                spectatorMoney[i] += money;
        printLog();
    }
    
    /**
     * Report the results of the race.
     * @param horseIds id of the horses in the race
     * @param horsePositions position of the horses in the race
     */
    @Override
    public synchronized void reportResults(int[] horseIds, int[] horsePositions) throws RemoteException{
        
        for(int i=0; i<this.horseIds.length; i++){
            for(int j=0; j<horseIds.length; j++){
                if(this.horseIds[i]==horseIds[j])
                    this.horseTrackPosition[i] = horsePositions[j];
            }
            
        }
        printLog();
    }
    
    /**
     * Stop the service and shut down the shared region.
     */
    @Override
    public synchronized void serviceEnd() throws RemoteException{
        MainProgram.serviceEnd = true;
        notifyAll();
    }
    
}

