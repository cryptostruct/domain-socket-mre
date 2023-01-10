#!/bin/bash

DIR="$(dirname -- $(readlink -f ${BASH_SOURCE}))"
RUNTIME=1m

trap 'jobs -p | xargs kill' EXIT

echo "launch client/server and keep them running for ${RUNTIME}"

java -jar ${DIR}/server/target/server.jar &
sleep 2 
java -jar ${DIR}/client/target/client.jar &
sleep ${RUNTIME}
