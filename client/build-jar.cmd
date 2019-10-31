cd backend
call mvn -Dtest=ImageControllerTest#BVT test
cd ..
call mvn clean package -DskipTests
rmdir frontend\npm /s /q
copy "backend\target\com.tend.acd.backend*.jar" "..\bin\"  /y