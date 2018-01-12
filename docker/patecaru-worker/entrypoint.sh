#!/bin/bash - 
#===============================================================================
#
#          FILE: entrypoint.sh
# 
#         USAGE: ./entrypoint.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Fabian Wrede
#  ORGANIZATION: 
#       CREATED: 24/05/16 18:01
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error

cd /root/Muesli
make
echo HIIIIi
/usr/sbin/sshd -D

