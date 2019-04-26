call mvn clean package
copy "backend\target\com.tend.acd.backend-0.0.12-SNAPSHOT.jar" "..\bin\com.tend.acd.backend-0.0.12-SNAPSHOT.jar" /y
"D:\tools\winscp517\pscp" -v -pw bbh2gd "backend\target\com.tend.acd.backend-0.0.12-SNAPSHOT.jar" "cyokin@192.168.201.110:/mnt/tend/tend/com.tend.acd.backend.jar"
"D:\tools\winscp517\plink" -v -ssh -t cyokin@192.168.201.110 -pw bbh2gd "cd /mnt/tend/; sudo docker-compose build; sudo docker tag cyokin/tend cyokin/tend:0.0.12; sudo docker push cyokin/tend:0.0.12"
