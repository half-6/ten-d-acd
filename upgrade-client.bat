SET from=0.0.17
SET to=0.0.18
FOR %%G IN (README.MD,bin/start-local.bat,bin/start.bat,docker/docker-compose.yml,client/start.bat,client/start-demo.bat,client/pom.xml,client/backend/pom.xml,client/frontend/pom.xml) DO (
   powershell -Command "(gc %%G) -replace '%from%', '%to%' | Out-File %%G"
)
