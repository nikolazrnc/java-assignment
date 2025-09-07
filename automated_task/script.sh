#!/bin/bash

SUBSCRIPTION_URL="http://localhost:8080/subscriptions?active=true"
ADD_METAR_URL="http://localhost:8080/airport/"

subscriptions=$(curl -s -X GET "$SUBSCRIPTION_URL" -H "Accept: application/json")
#echo "$subscriptions"

METAR_DATA_URL="https://tgftp.nws.noaa.gov/data/observations/metar/stations/"

for icaoCode in $(echo "$subscriptions" | jq -r '.[].icaoCode'); do
	result=$(curl -sS -w "%{http_code}" "${METAR_DATA_URL}${icaoCode}.TXT")
	status_code="${result: -3}"
	metar_data="${result::-3}"
	if [ "$status_code" -eq 200 ]; then
		createdAt=$(echo "$metar_data" | head -n 1)
		createdAt=$(date -u -d "$createdAt" +"%Y-%m-%dT%H:%M:%SZ")
		#echo "$createdAt"
		data=$(echo "$metar_data" | tail -n +2)
		payload=$(jq -n --arg createdAt "$createdAt" \
		--arg data "$data" '{createdAt: $createdAt, data: $data}')
		#echo "$payload"

		metar_response=$(curl -s -X POST "${ADD_METAR_URL}${icaoCode}/METAR" \
-H "Content-Type: application/json" -d "$payload")
#echo "$metar_response"

	fi
done
