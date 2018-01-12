#!/bin/bash
docker-machine create \
  --driver amazonec2 \
  --amazonec2-region us-west-2 \
  --swarm \
  --swarm-discovery="consul://127.0.0.1:8500" \
  --engine-opt="cluster-store=consul://127.0.0.1:8500" \
  --engine-opt="cluster-advertise=eth0:2376" \
  manager

#DISCOVERY production: build consul master
docker $(docker-machine config manager) run -d \
  -p 8500:8500 -h consul \
  progrium/consul \
  -server -bootstrap

#Get private ip
manager_private=$(docker-machine inspect manager --format '{{.Driver.PrivateIPAddress}}')
consul="$manager_private:8500"
registry="$manager_private:5000"


#build swarm master  --swarm-strategy "spread" \
docker-machine create \
  --driver amazonec2 \
  --amazonec2-region us-west-2 \
  --amazonec2-ami ami-3140ba51 \
  --swarm \
  --swarm-master \
  --swarm-discovery="consul://$consul" \
  --engine-opt="cluster-store=consul://$consul" \
  --engine-opt="cluster-advertise=eth0:2376" \
  --engine-insecure-registry $registry
  c0-master

#build new machines  --amazonec2-instance-type g2.8xlarge
NUM_MACHINES=4

until [  $NUM_MACHINES -lt 1 ]; do
     docker-machine create \
	  --driver amazonec2 \
	  --amazonec2-region us-west-2 \
 	  --amazonec2-ami ami-3140ba51 \
	  --swarm \
	  --swarm-strategy "spread" \
	  --swarm-discovery="consul://$consul" \
	  --engine-opt="cluster-store=consul://$consul" \
	  --engine-opt="cluster-advertise=eth0:2376" \
	  --engine-insecure-registry $registry
	  c0-n$NUM_MACHINES
     let NUM_MACHINES-=1
done

docker $(docker-machine config --swarm c0-master) info

