FROM openjdk:8u151-jre

MAINTAINER Cyokin Zhang

RUN apt-get update && \
	apt-get install -y curl wget unzip xorg

WORKDIR /opt/

ADD com.tend.acd.exporter.jar /opt/com.tend.acd.exporter.jar