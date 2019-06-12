call mvn clean package
rmdir frontend\npm /s /q
copy "backend\target\com.tend.acd.server*.jar" "..\bin\"  /y
copy "backend\target\com.tend.acd.server*.jar" "..\docker\tend-admin\" /y
ren  "..\docker\tend-admin\*.jar" "com.tend.acd.server.jar"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\docker-compose.yml" "root@192.168.201.110:/mnt/tend/"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\tend-admin" "root@192.168.201.110:/mnt/tend/"
del ..\docker\tend-admin\com.tend.acd.server.jar
ssh -i D:\codes\mykey\linkfuture.ppk root@192.168.201.110 "cd /mnt/tend/; sudo docker-compose build admin; sudo docker-compose push admin"