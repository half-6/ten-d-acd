# Ten-D ACD System Exporter
Export is design to export all images and data for Ten-D ACD System

> ## Setup DEV environment
- install JAVA 8.0 +

> ## How to export   
image.path is the upload path for Ten-D ACD System  
``
java -jar com.tend.acd.exporter-0.0.8-SNAPSHOT.jar --spring.profiles.active=demo --image.path=D:\\tend\\static\\uploads
``  
After job completed, under same folder with the app, you will find <timestamp>.sql along with static folder with all images

