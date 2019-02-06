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
- npm run serve --prefix frontend
- mvn spring-boot:run -f backend/pom.xml
- start http://localhost:3000/

> ## Setup production environment
- install JAVA 8.0 +
- install MATLAB runtime R2018b (9.5)  
- install PostgreSQL  9.5.3 +

> ## How to release to production and run
- mvn package //create release package
- execute table.sql and data.sql on PostgreSQL
- java -jar backend\target\com.tend.acd.backend-0.0.2-SNAPSHOT.jar
- start http://localhost:8080/

> ## How to run as demo
- java -jar backend\target\com.tend.acd.backend-0.0.2-SNAPSHOT.jar --spring.profiles.active=demo
