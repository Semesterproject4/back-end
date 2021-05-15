FROM ubuntu:21.04

ENV DEBIAN_FRONTEND=noninteractive
ENV LC_ALL C.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

RUN dpkg --add-architecture i386 && apt-get update && \
    apt-get -y install python2 python-is-python2 xvfb x11vnc curl gnupg2 socat xdotool fluxbox
RUN curl https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN echo 'deb https://dl.winehq.org/wine-builds/ubuntu/ focal main' |tee /etc/apt/sources.list.d/winehq.list
RUN apt-get update && apt-get -y install winehq-stable winetricks
RUN apt-get -y full-upgrade && apt-get clean

WORKDIR /usr/scr/app
COPY ./Simulation/ .

ENV WINEPREFIX /prefix32
ENV WINEARCH win32
ENV DISPLAY :0
RUN winetricks wine-mono Gecko || echo "workaround about the exit '1' code"

CMD ["./start_server.sh"]
