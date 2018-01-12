if [ "$1" = "-a" ]
then
	sudo docker rmi -f $(sudo docker images -q)
else
	sudo docker rmi $(sudo docker images -q)
fi
