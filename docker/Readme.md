1. install .aws into home directory of user
2. invoke unleashTheSwarm.sh
3. connect to swarm master on new node
4. compose up the compose.yaml
5. compose scale openmpi=4 


Testruns
mpirun -np 4 --hostfile hostfile /path/to/example/matmult_gpu <größe der matrix> <runs> <anzahl der gpus> <größe eines workpackages>	

<größe der Matrix> 16384 (8192)
<runs> 5
<gpus> 4  
<größe eines workpackages> 1,32,64,128, matrix/16, matrix/8, matrix/4, matrix/2


GPUS 0 1 2 4
Knoten 1 4