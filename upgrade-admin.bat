SET from=0.0.3
SET to=0.0.4
FOR %%G IN (bin/start-admin-local.bat,bin/start-admin.bat,docker/cloud.yml,docker/docker-compose.yml,server/start.bat,server/pom.xml,server/backend/pom.xml,server/frontend/pom.xml) DO (
   powershell -Command "(gc %%G) -replace '%from%', '%to%' | Out-File %%G"
)
