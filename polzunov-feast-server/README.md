# Polzunov.Feast server

## Requirements
`java 17+`, `docker 24.0.2+`, `docker-compose 1.29.2+`

## How to run
Go in the project directory and run the following:
1. To create a docker-container with database:`docker-compose up`
      - POSTGRES_DB: 'polzunov-feast-db'
      -  POSTGRES_USER: 'polzunov'
      - POSTGRES_PASSWORD: 'feast'
      - port: 5432 
2. To stop created container: `docker-compose stop polzunov-feast-db`
3. To start: `docker-compose start polzunov-feast-db`
4. Create .jar file: `mvn clean install -DskipTests`
5. Execute .jar (will not work if database is down): `java -jar target/polzunov-feast-server-0.0.1-SNAPSHOT.jar`
6. Send http requests on port 8080, e.g. `http://localhost:8080/user/signup`
