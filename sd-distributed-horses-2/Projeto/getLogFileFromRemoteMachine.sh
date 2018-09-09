bold=$(tput bold)
normal=$(tput sgr0)
echo "${bold}*** Script de Recolha do Log ***${normal}"

export SSHPASS='jantardomaia'


###

echo -e "\n${bold}* Recolha do log no nó *${normal}"

echo -e "\n${bold}->${normal} A recolher o ficheiro de log da máquina ${bold}1${normal}"
sshpass -e sftp -o StrictHostKeyChecking=no sd0403@l040101-ws01.ua.pt << !
	cd Logger/src/main/java/MainPackage/
	get -r log.txt
	bye
!