# You need to install sshpass to run the script correctly

bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script de Deployment ***${normal}"

export SSHPASS='jantardomaia'


###

echo -e "\n${bold}* Copiar parâmetros de simulação *${normal}"
cp SimulationParameters/SimulationParameters_remote.java SDA3_Registry/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_Logger/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_BettingCenter/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_ControlCenter/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_Paddock/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_RacingTrack/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_Stable/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_Broker/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_HorseJockey/src/MainPackage/SimulationParameters.java
cp SimulationParameters/SimulationParameters_remote.java SDA3_Spectators/src/MainPackage/SimulationParameters.java

echo -e "\n${bold}* Copiar interfaces para o registry *${normal}"
cp SDA3_BettingCenter/src/Interfaces/BettingCenterInterface.java SDA3_Registry/src/Interfaces/
cp SDA3_ControlCenter/src/Interfaces/ControlCenterInterface.java SDA3_Registry/src/Interfaces/
cp SDA3_Logger/src/Interfaces/LoggerInterface.java SDA3_Registry/src/Interfaces/
cp SDA3_Paddock/src/Interfaces/PaddockInterface.java SDA3_Registry/src/Interfaces/
cp SDA3_RacingTrack/src/Interfaces/RacingTrackInterface.java SDA3_Registry/src/Interfaces/
cp SDA3_Stable/src/Interfaces/StableInterface.java SDA3_Registry/src/Interfaces/

###

zip -r SDA3_Registry.zip SDA3_Registry
zip -r SDA3_Logger.zip SDA3_Logger
zip -r SDA3_BettingCenter.zip SDA3_BettingCenter
zip -r SDA3_ControlCenter.zip SDA3_ControlCenter
zip -r SDA3_Paddock.zip SDA3_Paddock
zip -r SDA3_RacingTrack.zip SDA3_RacingTrack
zip -r SDA3_Stable.zip SDA3_Stable
zip -r SDA3_Broker.zip SDA3_Broker
zip -r SDA3_HorseJockey.zip SDA3_HorseJockey
zip -r SDA3_Spectators.zip SDA3_Spectators

###

echo -e "\n${bold}* Cópia do código a executar em cada nó *${normal}"


echo -e "\n${bold}->${normal} A mover Registry e Logger para a máquina ${bold}1${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << !
    put -r SDA3_Registry.zip
    put -r SDA3_Logger.zip
    bye
!

echo -e "\n${bold}->${normal} A mover BettingCenter para a máquina ${bold}2${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws02.ua.pt << !
    put -r SDA3_BettingCenter.zip
    bye
!

echo -e "\n${bold}->${normal} A mover ControlCenter para a máquina ${bold}3${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws03.ua.pt << !
    put -r SDA3_ControlCenter.zip
    bye
!

echo -e "\n${bold}->${normal} A mover Paddock para a máquina ${bold}4${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws04.ua.pt << !
    put -r SDA3_Paddock.zip
    bye
!

echo -e "\n${bold}->${normal} A mover RacingTrack para a máquina ${bold}5${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws05.ua.pt << !
    put -r SDA3_RacingTrack.zip
    bye
!

echo -e "\n${bold}->${normal} A mover Stable para a máquina ${bold}10${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws10.ua.pt << !
    put -r SDA3_Stable.zip
    bye
!

echo -e "\n${bold}->${normal} A mover Broker para a máquina ${bold}7${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws07.ua.pt << !
    put -r SDA3_Broker.zip
    bye
!

echo -e "\n${bold}->${normal} A mover HorseJockey para a máquina ${bold}8${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws08.ua.pt << !
    put -r SDA3_HorseJockey.zip
    bye
!

echo -e "\n${bold}->${normal} A mover Spectators para a máquina ${bold}9${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws09.ua.pt << !
    put -r SDA3_Spectators.zip
    bye
!

###

rm SDA3_Registry.zip
rm SDA3_Logger.zip
rm SDA3_BettingCenter.zip
rm SDA3_ControlCenter.zip
rm SDA3_Paddock.zip
rm SDA3_RacingTrack.zip
rm SDA3_Stable.zip
rm SDA3_Broker.zip
rm SDA3_HorseJockey.zip
rm SDA3_Spectators.zip

###

echo -e "\n${bold}* Compilação do código em cada nó *${normal}"


echo -e "\n${bold}->${normal} A compilar Registry e Logger na máquina ${bold}1${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << EOF
	unzip SDA3_Registry.zip
	rm SDA3_Registry.zip

    javac SDA3_Registry/src/Interfaces/*.java SDA3_Registry/src/MainPackage/*.java SDA3_Registry/src/HelperClasses/*.java SDA3_Registry/src/EntitiesState/*.java
    
    mv SDA3_Registry/src/Interfaces/*.class SDA3_Registry/src/target/Interfaces/
    mv SDA3_Registry/src/MainPackage/*.class SDA3_Registry/src/target/MainPackage/
    mv SDA3_Registry/src/HelperClasses/*.class SDA3_Registry/src/target/HelperClasses/
    mv SDA3_Registry/src/EntitiesState/*.class SDA3_Registry/src/target/EntitiesState/
    
    unzip SDA3_Logger.zip
	rm SDA3_Logger.zip
    
    javac SDA3_Logger/src/Interfaces/*.java SDA3_Logger/src/MainPackage/*.java SDA3_Logger/src/HelperClasses/*.java SDA3_Logger/src/EntitiesState/*.java
    
    mv SDA3_Logger/src/Interfaces/*.class SDA3_Logger/src/target/Interfaces/
    mv SDA3_Logger/src/MainPackage/*.class SDA3_Logger/src/target/MainPackage/
    mv SDA3_Logger/src/HelperClasses/*.class SDA3_Logger/src/target/HelperClasses/
    mv SDA3_Logger/src/EntitiesState/*.class SDA3_Logger/src/target/EntitiesState/

    cd Public

    rm -rf registry
    rm -rf logger

    mkdir -p registry
    mkdir -p logger
    
    cd registry
    mkdir -p classes
    cd ..
    
    cd logger
    mkdir -p classes
    cd ..

    cd ..
    mv SDA3_Registry/src/target/* Public/registry/classes/
    mv SDA3_Logger/src/target/* Public/logger/classes/
    rm -rf SDA3_Registry
    rm -rf SDA3_Logger
EOF

echo -e "\n${bold}->${normal} A compilar BettingCenter na máquina ${bold}2${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws02.ua.pt << EOF
	unzip -o SDA3_BettingCenter.zip
	rm SDA3_BettingCenter.zip

    javac SDA3_BettingCenter/src/Interfaces/*.java SDA3_BettingCenter/src/MainPackage/*.java SDA3_BettingCenter/src/HelperClasses/*.java SDA3_BettingCenter/src/EntitiesState/*.java
    
    mv SDA3_BettingCenter/src/Interfaces/*.class SDA3_BettingCenter/src/target/Interfaces/
    mv SDA3_BettingCenter/src/MainPackage/*.class SDA3_BettingCenter/src/target/MainPackage/
    mv SDA3_BettingCenter/src/HelperClasses/*.class SDA3_BettingCenter/src/target/HelperClasses/
    mv SDA3_BettingCenter/src/EntitiesState/*.class SDA3_BettingCenter/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir -p classes
    cd ..

    mv SDA3_BettingCenter/src/target/* Public/classes/
    rm -rf SDA3_BettingCenter
EOF

echo -e "\n${bold}->${normal} A compilar ControlCenter na máquina ${bold}3${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws03.ua.pt << EOF
	unzip -o SDA3_ControlCenter.zip
	rm SDA3_ControlCenter.zip

    javac SDA3_ControlCenter/src/Interfaces/*.java SDA3_ControlCenter/src/MainPackage/*.java SDA3_ControlCenter/src/HelperClasses/*.java SDA3_ControlCenter/src/EntitiesState/*.java
    
    mv SDA3_ControlCenter/src/Interfaces/*.class SDA3_ControlCenter/src/target/Interfaces/
    mv SDA3_ControlCenter/src/MainPackage/*.class SDA3_ControlCenter/src/target/MainPackage/
    mv SDA3_ControlCenter/src/HelperClasses/*.class SDA3_ControlCenter/src/target/HelperClasses/
    mv SDA3_ControlCenter/src/EntitiesState/*.class SDA3_ControlCenter/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir -p classes
    cd ..

    mv SDA3_ControlCenter/src/target/* Public/classes/
    rm -rf SDA3_ControlCenter
EOF

echo -e "\n${bold}->${normal} A compilar Paddock na máquina ${bold}4${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws04.ua.pt << EOF
	unzip -o SDA3_Paddock.zip
	rm SDA3_Paddock.zip

    javac SDA3_Paddock/src/Interfaces/*.java SDA3_Paddock/src/MainPackage/*.java SDA3_Paddock/src/HelperClasses/*.java SDA3_Paddock/src/EntitiesState/*.java
    
    mv SDA3_Paddock/src/Interfaces/*.class SDA3_Paddock/src/target/Interfaces/
    mv SDA3_Paddock/src/MainPackage/*.class SDA3_Paddock/src/target/MainPackage/
    mv SDA3_Paddock/src/HelperClasses/*.class SDA3_Paddock/src/target/HelperClasses/
    mv SDA3_Paddock/src/EntitiesState/*.class SDA3_Paddock/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_Paddock/src/target/* Public/classes/
    rm -rf SDA3_Paddock
EOF

echo -e "\n${bold}->${normal} A compilar RacingTrack na máquina ${bold}5${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws05.ua.pt << EOF
    unzip -o SDA3_RacingTrack.zip
	rm SDA3_RacingTrack.zip

    javac SDA3_RacingTrack/src/Interfaces/*.java SDA3_RacingTrack/src/MainPackage/*.java SDA3_RacingTrack/src/HelperClasses/*.java SDA3_RacingTrack/src/EntitiesState/*.java
    
    mv SDA3_RacingTrack/src/Interfaces/*.class SDA3_RacingTrack/src/target/Interfaces/
    mv SDA3_RacingTrack/src/MainPackage/*.class SDA3_RacingTrack/src/target/MainPackage/
    mv SDA3_RacingTrack/src/HelperClasses/*.class SDA3_RacingTrack/src/target/HelperClasses/
    mv SDA3_RacingTrack/src/EntitiesState/*.class SDA3_RacingTrack/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_RacingTrack/src/target/* Public/classes/
    rm -rf SDA3_RacingTrack
EOF

echo -e "\n${bold}->${normal} A compilar Stable na máquina ${bold}10${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws10.ua.pt << EOF
    unzip -o SDA3_Stable.zip
	rm SDA3_Stable.zip

    javac SDA3_Stable/src/Interfaces/*.java SDA3_Stable/src/MainPackage/*.java SDA3_Stable/src/HelperClasses/*.java SDA3_Stable/src/EntitiesState/*.java
    
    mv SDA3_Stable/src/Interfaces/*.class SDA3_Stable/src/target/Interfaces/
    mv SDA3_Stable/src/MainPackage/*.class SDA3_Stable/src/target/MainPackage/
    mv SDA3_Stable/src/HelperClasses/*.class SDA3_Stable/src/target/HelperClasses/
    mv SDA3_Stable/src/EntitiesState/*.class SDA3_Stable/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_Stable/src/target/* Public/classes/
    rm -rf SDA3_Stable
EOF


echo -e "\n${bold}->${normal} A compilar Broker na máquina ${bold}7${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws07.ua.pt << EOF
	unzip -o SDA3_Broker.zip
	rm SDA3_Broker.zip

    javac SDA3_Broker/src/Interfaces/*.java SDA3_Broker/src/MainPackage/*.java SDA3_Broker/src/HelperClasses/*.java SDA3_Broker/src/EntitiesState/*.java
    
    mv SDA3_Broker/src/Interfaces/*.class SDA3_Broker/src/target/Interfaces/
    mv SDA3_Broker/src/MainPackage/*.class SDA3_Broker/src/target/MainPackage/
    mv SDA3_Broker/src/HelperClasses/*.class SDA3_Broker/src/target/HelperClasses/
    mv SDA3_Broker/src/EntitiesState/*.class SDA3_Broker/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_Broker/src/target/* Public/classes/
    rm -rf SDA3_Broker
EOF

echo -e "\n${bold}->${normal} A compilar HorseJockey na máquina ${bold}8${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws08.ua.pt << EOF
    unzip -o SDA3_HorseJockey.zip
	rm SDA3_HorseJockey.zip

    javac SDA3_HorseJockey/src/Interfaces/*.java SDA3_HorseJockey/src/MainPackage/*.java SDA3_HorseJockey/src/HelperClasses/*.java SDA3_HorseJockey/src/EntitiesState/*.java
    
    mv SDA3_HorseJockey/src/Interfaces/*.class SDA3_HorseJockey/src/target/Interfaces/
    mv SDA3_HorseJockey/src/MainPackage/*.class SDA3_HorseJockey/src/target/MainPackage/
    mv SDA3_HorseJockey/src/HelperClasses/*.class SDA3_HorseJockey/src/target/HelperClasses/
    mv SDA3_HorseJockey/src/EntitiesState/*.class SDA3_HorseJockey/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_HorseJockey/src/target/* Public/classes/
    rm -rf SDA3_HorseJockey
EOF

echo -e "\n${bold}->${normal} A compilar Spectators na máquina ${bold}9${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws09.ua.pt << EOF
	unzip -o SDA3_Spectators.zip
	rm SDA3_Spectators.zip

    javac SDA3_Spectators/src/Interfaces/*.java SDA3_Spectators/src/MainPackage/*.java SDA3_Spectators/src/HelperClasses/*.java SDA3_Spectators/src/EntitiesState/*.java
    
    mv SDA3_Spectators/src/Interfaces/*.class SDA3_Spectators/src/target/Interfaces/
    mv SDA3_Spectators/src/MainPackage/*.class SDA3_Spectators/src/target/MainPackage/
    mv SDA3_Spectators/src/HelperClasses/*.class SDA3_Spectators/src/target/HelperClasses/
    mv SDA3_Spectators/src/EntitiesState/*.class SDA3_Spectators/src/target/EntitiesState/

    cd Public
    rm -rf classes
    mkdir classes
    cd ..

    mv SDA3_Spectators/src/target/* Public/classes/
    rm -rf SDA3_Spectators
EOF


###

echo -e "\n${bold}* Execução do código em cada nó *${normal}"


echo -e "\n${bold}->${normal} A iniciar e executar Registry e executar Logger na máquina ${bold}1${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << EOF
    cd Public/registry/classes
    nohup rmiregistry -J-Djava.rmi.server.useCodebaseOnly=true 22427 > /dev/null 2>&1 &

    sleep 5
    
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.ServerRegisterRemoteObject > /dev/null 2>&1 &
    cd ../..
    cd logger/classes/
    
    sleep 5

    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 5

echo -e "\n${bold}->${normal} A executar BettingCenter na máquina ${bold}2${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws02.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar ControlCenter na máquina ${bold}3${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws03.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar Paddock na máquina ${bold}4${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws04.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar RacingTrack na máquina ${bold}5${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws05.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar Stable na máquina ${bold}10${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws10.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

# Wait for the shared regions to be launched before lanching the intervening enities

sleep 5

echo -e "\n${bold}->${normal} A executar Broker na máquina ${bold}7${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws07.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar HorseJockey na máquina ${bold}8${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws08.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF

sleep 1

echo -e "\n${bold}->${normal} A executar Spectators na máquina ${bold}9${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws09.ua.pt << EOF
    cd Public/classes/
    nohup java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0403/registry/classes/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    MainPackage.MainProgram > /dev/null 2>&1 &
EOF
