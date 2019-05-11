set /P version=Enter version(default is 0.0.1): || SET "version=0.0.1"
echo your application version is %version%
set /P port=Enter http port(default is 8081): || SET "port=8081"
echo you http port is %port%
set /P tend.postgreSQL.host=Enter database host(default is localhost): || SET "tend.postgreSQL.host=localhost"
echo your database host is %tend.postgreSQL.host%
set /P tend.postgreSQL.port=Enter database port(default is 5432): || SET "tend.postgreSQL.port=5432"
echo your database port is %tend.postgreSQL.port%
set /P tend.postgreSQL.user=Enter database user(default is postgres): || SET "tend.postgreSQL.user=postgres"
echo your database user is %tend.postgreSQL.user%
set /P tend.postgreSQL.password=Enter database password(default is qazwsx123): || SET "tend.postgreSQL.password=qazwsx123"
echo your database password is %tend.postgreSQL.password%

java -jar com.tend.acd.server.backend-%version%-SNAPSHOT.jar --server.port=%port% --spring.profiles.active=env
