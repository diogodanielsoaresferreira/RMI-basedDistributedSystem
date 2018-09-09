# You need to install sshpass to run the script correctly

bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script de finalização do registry ***${normal}"

export SSHPASS='jantardomaia'

echo -e "\n${bold}->${normal} A matar o processo rmiregistry na máquina ${bold}1${normal}"
sshpass -e ssh -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << EOF
   bash -c "ps -ef | grep sd0403 | grep java | awk '{print \\\$2}' | xargs kill" &
EOF
