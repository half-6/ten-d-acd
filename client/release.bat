call ../db/merge.bat
cd backend
call mvn -Dtest=ImageControllerTest#BVT test
cd ..
call mvn clean package -DskipTests
rmdir frontend\npm /s /q
copy "backend\target\com.tend.acd.backend*.jar" "..\bin\"  /y
copy "backend\target\com.tend.acd.backend*.jar" "..\docker\tend\" /y
ren  "..\docker\tend\*.jar" "com.tend.acd.backend.jar"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\docker-compose.yml" "root@192.168.201.110:/mnt/tend/"
scp -v -r -i D:\codes\mykey\linkfuture.ppk  "D:\codes\qyotech\ten-d-acd\docker\tend" "root@192.168.201.110:/mnt/tend/"
del ..\docker\tend\com.tend.acd.backend.jar
ssh -i D:\codes\mykey\linkfuture.ppk root@192.168.201.110 "cd /mnt/tend/; sudo docker-compose build web; sudo docker-compose push web"