#! /usr/bin/bash

TASK="0 * * * * ./script.sh"

crontab -l > temp
echo "$TASK" >> temp

crontab temp
rm temp

echo "Added METAR task"

