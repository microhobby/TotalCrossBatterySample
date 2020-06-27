ARG BASE_IMAGE=torizon/arm32v7-debian-wayland-base

## --------- BUILD
FROM matheuscastello/binutils-deb AS BuildTCNILib

## install libgpiod and dependencies
RUN apt-get -y update && apt-get install -y --no-install-recommends \
	libudev-dev \
	&& apt-get clean && apt-get autoremove && rm -rf /var/lib/apt/lists/*

COPY src/main/native /native
WORKDIR /native

RUN make -j8

## --------- DEPLOY
FROM $BASE_IMAGE:latest AS Deploy

## install libgpiod and dependencies
RUN apt-get -y update && apt-get install -y \
    libsdl2-2.0-0 \
    libfontconfig \
	libdbus-1-dev \
    libudev1 \
	libinput10 \
    libibus-1.0-5 \
	libgpiod-dev \
	&& apt-get clean && apt-get autoremove && rm -rf /var/lib/apt/lists/*

## solve the mimes
RUN apt-get -y update && apt-get --reinstall install shared-mime-info

## copy artifacts
COPY --from=BuildTCNILib /native/libTCNIUdev.so /usr/lib/arm-linux-gnueabihf/

## copy app package
COPY target/install/linux_arm /project

## permissions
RUN usermod -a -G gpio torizon

## xorg
ENV DISPLAY=:0
ENV SDL_VIDEODRIVER=wayland

# execute
WORKDIR /project
CMD ["./BatteryStateSample"]
