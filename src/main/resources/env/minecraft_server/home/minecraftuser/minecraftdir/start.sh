#!/bin/bash
echo 'Start from usr/local/bin'
AMOUNT="$(screen -ls | grep 'server' -o | wc -l)"
if [ $AMOUNT -eq 0 ]; then
   /usr/bin/screen -S server -d -m bash -c 'cd /home/minecraftuser/minecraftdir; java -Xmx2048M -Xms1024M -jar /home/minecraftuser/minecraftdir/server.jar'
fi
exit 0