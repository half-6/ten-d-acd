call mvn clean package
copy "backend\target\com.tend.acd.backend*.jar" "..\bin\"  /y
copy "backend\target\com.tend.acd.backend*.jar" "..\docker\tend\" /y
ren  "..\docker\tend\*.jar" "com.tend.acd.backend.jar"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\db.sql" "root@192.168.201.110:/mnt/tend/"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\docker-compose.yml" "root@192.168.201.110:/mnt/tend/"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\tend" "root@192.168.201.110:/mnt/tend/"
del ..\docker\tend\com.tend.acd.backend.jar
ssh -i D:\codes\mykey\linkfuture.ppk root@192.168.201.110 "cd /mnt/tend/; sudo -S docker-compose build web; sudo -S docker-compose push web"