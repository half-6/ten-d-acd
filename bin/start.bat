set /P version=Enter version(default is 1.0.2): || SET "version=1.0.2"
echo your application version is %version%
set /P port=Enter http port(default is 8080): || SET "port=8080"
echo you http port is %port%
set /P tend.postgreSQL.host=Enter database host(default is localhost): || SET "tend.postgreSQL.host=localhost"
echo your database host is %tend.postgreSQL.host%
set /P tend.postgreSQL.port=Enter database port(default is 5432): || SET "tend.postgreSQL.port=5432"
echo your database port is %tend.postgreSQL.port%

SET image.recognition.path=D:\codes\qyotech\ten-d-acd\client\backend\lib\Image_Recognition.jar
java -jar com.tend.acd.backend-%version%.jar --server.port=%port% --spring.profiles.active=env
