FROM ubuntu:latest

# author
MAINTAINER xubin

# set env
ENV JAVA_HOME /usr/java/jdk/jdk1.8.0_231
ENV APP_ROOT /app
ENV PATH ${PATH}:${JAVA_HOME}/bin

RUN mkdir ${APP_ROOT}
ADD server-jre-8u231-linux-x64.tar.gz /usr/java/jdk/
