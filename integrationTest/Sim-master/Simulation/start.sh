#!/bin/sh
Xvfb :0 -screen 0 640x480x8 -nolisten tcp &
export DISPLAY=:0
sleep 10
wine Config1/X20CP1586/ar000loader.exe
