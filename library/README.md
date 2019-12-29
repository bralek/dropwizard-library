# Library

How to start the Library application
---

1. Run `mvn clean install` to build your application
2. Create DB and edit config.yml with DB address and user credentials.
3. To create DB table needed for project run `java -jar target/library-0.0.1-SNAPSHOT.jar db migrate config.yml`
4. Start application with `java -jar target/library-0.0.1-SNAPSHOT.jar server config.yml`
5. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
