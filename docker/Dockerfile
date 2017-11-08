FROM ubuntu:latest

# Install java
RUN DEBIAN_FRONTEND=noninteractive apt-get -qq update \
	&& apt-get -qq -y install openjdk-8-jdk 
	
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
 
EXPOSE 8080

# By default, simply start the app
#CMD java -classpath /myapp/lib/*:/myapp MyServer
