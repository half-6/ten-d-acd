call mvn clean package -DskipTests
copy "target\com.tend.acd.*.jar" "..\bin\"  /y
copy "target\com.tend.acd.*.jar" "..\docker\tend-exporter\" /y
ren  "..\docker\tend-exporter\*.jar" "com.tend.acd.exporter.jar"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\docker-compose.yml" "root@192.168.201.110:/mnt/tend/"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\tend-exporter" "root@192.168.201.110:/mnt/tend/"
del ..\docker\tend-exporter\com.tend.acd.exporter.jar
ssh -i D:\codes\mykey\linkfuture.ppk root@192.168.201.110 "cd /mnt/tend/; sudo docker-compose build exporter; sudo docker-compose push exporter"
