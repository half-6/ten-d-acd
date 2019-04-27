FROM openjdk:8u151-jre

MAINTAINER Cyokin Zhang

ADD matlab.txt /mcr-install/matlab.txt

RUN apt-get update && \
	apt-get install -y curl wget unzip xorg

# Install MATLAB runtime
RUN \
	cd /mcr-install && \
	wget -nv http://ssd.mathworks.com/supportfiles/downloads/R2018b/deployment_files/R2018b/installers/glnxa64/MCR_R2018b_glnxa64_installer.zip && \
	unzip MCR_R2018b_glnxa64_installer.zip && \
	mkdir /opt/mcr && \
	./install -inputFile /mcr-install/matlab.txt && \
	cd / && \
	rm -rf mcr-install

ADD com.tend.acd.backend.jar /opt/com.tend.acd.backend.jar
ADD Image_Recognition.jar /opt/lib/Image_Recognition.jar

ENV LD_LIBRARY_PATH=/opt/mcr/v95/runtime/glnxa64/:/opt/mcr/v95/bin/glnxa64/:/opt/mcr/v95/sys/os/glnxa64/:/opt/mcr/v95/sys/opengl/lib/glnxa64/

EXPOSE 8080

CMD [ "java", "-jar", "/opt/com.tend.acd.backend.jar","--spring.profiles.active=env"]