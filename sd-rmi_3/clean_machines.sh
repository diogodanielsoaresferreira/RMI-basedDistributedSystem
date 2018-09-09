# You need to install sshpass to run the script correctly

bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script de Limpeza das Máquinas ***${normal}"

export SSHPASS='jantardomaia'


###

echo -e "\n${bold}->${normal} A eliminar Logger da máquina ${bold}1${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << EOF
	rm -rf SDA3_Registry
	rm -rf SDA3_Logger
	cd Public
	rm -rf registry
	rm -rf logger
EOF

echo -e "\n${bold}->${normal} A eliminar BettingCenter da máquina ${bold}2${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws02.ua.pt << EOF
	rm -rf SDA3_BettingCenter
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar ControlCenter da máquina ${bold}3${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws03.ua.pt << EOF
	rm -rf SDA3_ControlCenter
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar Paddock da máquina ${bold}4${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws04.ua.pt << EOF
	rm -rf SDA3_Paddock
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar RacingTrack da máquina ${bold}5${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws05.ua.pt << EOF
	rm -rf SDA3_RacingTrack
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar Stable da máquina ${bold}10${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws10.ua.pt << EOF
	rm -rf SDA3_Stable
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar Broker da máquina ${bold}7${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws07.ua.pt << EOF
	rm -rf SDA3_Broker
	cd Public
	rm -rf classes
EOF
echo -e "\n${bold}->${normal} A eliminar Horses da máquina ${bold}8${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws08.ua.pt << EOF
	rm -rf SDA3_HorseJockey
	cd Public
	rm -rf classes
EOF

echo -e "\n${bold}->${normal} A eliminar Spectators da máquina ${bold}9${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws09.ua.pt << EOF
	rm -rf SDA3_Spectators
	cd Public
	rm -rf classes
EOF