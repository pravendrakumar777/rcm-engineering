# rcm-engineering
This Application have a full functionalities of Employee Management System.

To Export MySQL Dump Data Via Command Prompt
$ cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
$ mysqldump -u root -p rcm_db > D:\db\your_dump.sql
$ Enter Your Password

RCM ENGINEERING APPLICATION DEPLOY ON JENKINS CREDENTIALS & DETAILS

Jenkins Credentials Details
Username : rcmengineering
Genkins Password : 546985e10ada46e1bd639266a4de635b

Jenkins URL : http://localhost:8080/
http://192.168.0.101:8080/github-webhook/

You can now run:
Dev app → http://localhost:8081
UAT app → http://localhost:8082
Prod app → http://localhost:8083

Build & Run Project :

Dev :
mvn clean package -Pdev -DskipTests
java -jar target/rcm-engineering-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --server.port=8081

Uat :
mvn clean package -Puat -DskipTests
java -jar target/rcm-engineering-0.0.1-SNAPSHOT.jar --spring.profiles.active=uat --server.port=8082

Prod :
mvn clean package -Pprod -DskipTests
java -jar target/rcm-engineering-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --server.port=8083

Docker Deployment Dewtails

Application Build with Docker 
From Gitbash Application root
$ mvn clean package -DskipTests
$ docker build -t rcm-engineering-app .

From Powershell terminal
$ docker images
Run with Docker
$ docker run -d -p 8081:8081 --name rcm-engineering rcm-engineering-app
Check Logs
$ docker logs -f rcm-engineering
