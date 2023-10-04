#!/bin/bash
NETWORK=vtvrdy_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=vtvrdy_payara_micro \
  --hostname=vtvrdy_payara_micro \
  vtvrdy_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/vtvrdy_aplikacija_2-1.0.0.war \
  --contextroot vtvrdy_aplikacija_2 \
  --noCluster &

wait
