# Ten-D ACD System Exporter
Exporter is design to import/export all images and data for Ten-D ACD System

> ## Setup DEV environment
- install JAVA 8.0 +

> ## How to export   
app.image.dir is the upload dir for Ten-D ACD System    
postgreSQL.connection is the DB connection string  
``
java -jar com.tend.acd.exporter-0.0.1-SNAPSHOT.jar export --postgreSQL.connection="jdbc:postgresql://192.168.201.110:5432/tend?user=postgres&password=qazwsx123" --app.image.dir=D:\\codes\\qyotech\\ten-d-acd\\client\\backend\\target\\classes\\static\\uploads
``  
After job completed, under same folder with the app, you will find <timestamp> folder with all images and data scripts

> ## How to import  
app.image.dir is the upload dir for Ten-D ACD System    
postgreSQL.connection is the DB connection string  
backup.dir is the backup <timestamp> folder you got from export  
``
java -jar com.tend.acd.exporter-0.0.1-SNAPSHOT.jar import --postgreSQL.connection="jdbc:postgresql://192.168.201.110:5432/tend?user=postgres&password=qazwsx123" --app.image.dir=D:\\codes\\qyotech\\ten-d-acd\\client\\backend\\target\\classes\\static\\uploads --backup.dir=D:\\codes\\qyotech\\ten-d-acd\\exporter\\target\\classes\\201904111357
``  
