FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p ${ANDROID_HOME}/cmdline-tools

RUN wget -q \
    https://dl.google.com/android/repository/commandlinetools-linux-15859902_latest.zip \
    -O /tmp/cmdline-tools.zip

RUN unzip -q /tmp/cmdline-tools.zip \
    -d ${ANDROID_HOME}/cmdline-tools

RUN mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools \
    ${ANDROID_HOME}/cmdline-tools/latest

ENV PATH=${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}

RUN yes | sdkmanager --licenses || true

RUN sdkmanager \
    "platform-tools" \
    "platforms;android-35" \
    "build-tools;35.0.0"

WORKDIR /workspace
