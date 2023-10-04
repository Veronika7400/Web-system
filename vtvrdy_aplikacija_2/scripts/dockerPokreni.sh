#!/bin/bash
NETWORK=vtvrdy_mreza_1

echo "ZAUSTAVI"
docker stop vtvrdy_payara_micro
echo "OBRISI"
docker rm vtvrdy_payara_micro
echo "PRIPREMI"
./scripts/pripremiSliku.sh
echo "POKRENI"
./scripts/pokreniSliku.sh