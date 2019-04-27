FROM openjdk:8u151-jre

MAINTAINER Cyokin Zhang

RUN apt-get update && \
	apt-get install -y curl wget unzip xorg

ADD com.tend.acd.server.jar /opt/com.tend.acd.server.jar

EXPOSE 8080

CMD [ "java", "-jar", "/opt/com.tend.acd.server.jar","--spring.profiles.active=env"]