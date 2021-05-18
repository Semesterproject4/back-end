#!/bin/sh
Xvfb :0 -screen 0 1024x768x24 &
fluxbox &
x11vnc -noxrecord &
socat tcp-listen:4841,reuseaddr,fork tcp:localhost:4840 &
./restart.sh 10 &
wine Config1/X20CP1586/ar000loader.exe
