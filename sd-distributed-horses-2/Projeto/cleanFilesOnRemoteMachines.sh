# You need to install sshpass to run the script correctly

bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script de Limpeza nas máquinas ***${normal}"

export SSHPASS='jantardomaia'


###

echo -e "\n${bold}->${normal} A eliminar Logger da máquina ${bold}1${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << EOF
	rm -rf Logger
EOF

echo -e "\n${bold}->${normal} A eliminar BettingCenter da máquina ${bold}2${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws02.ua.pt << EOF
	rm -rf BettingCenter
EOF

echo -e "\n${bold}->${normal} A eliminar ControlCenter da máquina ${bold}3${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws03.ua.pt << EOF
	rm -rf ControlCenter
EOF

echo -e "\n${bold}->${normal} A eliminar Paddock da máquina ${bold}4${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws04.ua.pt << EOF
	rm -rf Paddock
EOF

echo -e "\n${bold}->${normal} A eliminar RacingTrack da máquina ${bold}5${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws05.ua.pt << EOF
	rm -rf RacingTrack
EOF

echo -e "\n${bold}->${normal} A eliminar Stable da máquina ${bold}6${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws06.ua.pt << EOF
	rm -rf Stable
EOF

echo -e "\n${bold}->${normal} A eliminar Broker da máquina ${bold}7${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws07.ua.pt << EOF
	rm -rf Broker
EOF
echo -e "\n${bold}->${normal} A eliminar Horses da máquina ${bold}8${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws08.ua.pt << EOF
	rm -rf Horses
EOF

echo -e "\n${bold}->${normal} A eliminar Spectators da máquina ${bold}9${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws09.ua.pt << EOF
	rm -rf Spectators
EOF