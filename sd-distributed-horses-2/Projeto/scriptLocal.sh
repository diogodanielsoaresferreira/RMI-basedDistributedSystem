bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script Local ***${normal}"

###

echo -e "\n${bold}* Copiar parâmetros de simulação *${normal}"
cp SimulationParameters/local_SimulationParameters.java BettingCenter/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Broker/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java ControlCenter/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Horses/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Logger/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Paddock/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java RacingTrack/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Spectators/src/main/java/MainPackage/SimulationParameters.java
cp SimulationParameters/local_SimulationParameters.java Stable/src/main/java/MainPackage/SimulationParameters.java


###

echo -e "\n${bold}* Compilação do código em cada nó *${normal}"

echo -e "\n${bold}->${normal} A compilar Logger"
cd Logger/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar BettingCenter"
cd BettingCenter/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar ControlCenter"
cd ControlCenter/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar Paddock"
cd Paddock/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar RacingTrack"
cd RacingTrack/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar Stable"
cd Stable/
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar Broker"
cd Broker
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar Horses"
cd Horses
javac $(find . -name '*.java')
cd ..

echo -e "\n${bold}->${normal} A compilar Spectators"
cd Spectators
javac $(find . -name '*.java')
cd ..


###

echo -e "\n${bold}* Execução do código em cada nó *${normal}"

echo -e "\n${bold}->${normal} A executar Logger"
cd Logger/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar BettingCenter"
cd BettingCenter/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar ControlCenter"
cd ControlCenter/src/main/java
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar Paddock"
cd Paddock/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar RacingTrack"
cd RacingTrack/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar Stable"
cd Stable/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

# Wait for the shared regions to be launched before lanching the intervening enities

sleep 1

echo -e "\n${bold}->${normal} A executar Broker"
cd Broker/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar Horses"
cd Horses/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

echo -e "\n${bold}->${normal} A executar Spectators"
cd Spectators/src/main/java/
java -cp $(pwd) MainPackage/MainProgram &
cd ../../../..

wait

echo -e "\n${bold}->${normal} A execução terminou"

