#!/bin/sh
if [ $1 ]; then
    sleep $1
fi
xdotool search "ARsim" windowactivate --sync key --clearmodifiers space
