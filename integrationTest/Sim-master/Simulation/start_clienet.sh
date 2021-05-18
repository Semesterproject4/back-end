#!/bin/bash
wine Config1/X20CP1586/ar000loader.exe &
sleep 5
notify-send "Remember to refresh the page" "If it is not loadet yet"
xdg-open http://localhost:81/index.html?visuId=visu
