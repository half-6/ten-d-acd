# Ten-D ACD System
A web based automatic ultrasound cancer diagnosis system

> ## Architecture
![Ten-D ACD System](doc/tend-architecture.png)


> ## Setup DEV environment
- install JAVA 8.0 +
- install Maven latest
- install Nodejs 10.15.0 +
- install npm 6.5.0 + 
- install MATLAB runtime R2018b (9.5)  
- install PostgreSQL  9.5.3 +
- Add /lib/Image_Recognition.jar (core cancer diagnosis library, built by MATLAB)

> ## How to run local
- under frontend folder
- npm run serve --prefix frontend
- under backend folder
- mvn spring-boot:run
- start http://localhost:3000/

> ## Setup production environment
- install JAVA 8.0 +
- install MATLAB runtime R2018b (9.5)  
- install PostgreSQL  9.5.3 +

> ## How to release to production and run
- under backend folder
- mvn validate
- under application root folder
- mvn package //create release package
- execute table.sql and data.sql on PostgreSQL
- java -jar backend\target\com.tend.acd.backend-0.0.6-SNAPSHOT.jar
- start http://localhost:8080/

> ## How to run as demo
- java -jar backend\target\com.tend.acd.backend-0.0.6-SNAPSHOT.jar --spring.profiles.active=demo

> ## How to run it as backend windows service in windows. 
- download nssm from https://nssm.cc
- create start.bat file with following command  
  ``
  java -jar backend\target\com.tend.acd.backend-0.0.6-SNAPSHOT.jar --spring.profiles.active=demo
  ``
- run "nssm install tend-d-acd" and popup a nssm configuration window.  
- select that "start.bat" on application path on application tab 
- select "service.output.log" file on Output(stdout) I/O tab (optional,create file if not exists)
- select "service.error.log" file on Error(stderr) I/O tab (optional,create file if not exists)
- click "install service" button. 
- start "tend-d-acd" service on windows service manager
- wait 1 min and start http://localhost:8080/

