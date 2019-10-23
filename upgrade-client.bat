SET from=1.9.0
SET to=1.9.0.1
FOR %%G IN (README.MD,bin/start-local.bat,bin/start.bat,docker/docker-compose.yml,docker/tend/docker-compose.yml,client/start.bat,client/start-demo.bat,client/pom.xml,client/backend/pom.xml,client/frontend/pom.xml) DO (
   powershell -Command "(gc %%G) -replace '%from%', '%to%' | Out-File %%G -Encoding Default"
)
