# Polzunov.Feast server

## Requirements
`java 17+`, `docker 24.0.2+`, `docker-compose 1.29.2+`

If you only use `dev` mode, docker and docker-compose are not required.

## How to run
### Dev mode
This mode uses in-memory database, hence you won't need docker and the data will be lost when you restart the app.

Go to the project directory and do the following:
1. Create .jar file: `./mvnw clean install -DskipTests`
2. Execute .jar: `java -Dspring.profiles.active=dev -jar target/polzunov-feast-server-0.0.1-SNAPSHOT.jar`

Now you can send requests on port 8080, e.g. http://localhost:8080/event.
To access the database console go to the http://localhost:8080/h2-console.
- JDBC URL: `jdbc:h2:mem:polzunov-feast-db`
- User name: `polzunov`
- Password: `feast`

### Default mode
This mode uses postgresql database stored in docker container, the data will be present after the app restarts.

**Note:** access tokens will become invalid after the app restarts.

Go to the project directory and do the following:
1. Create a docker container with the database: `docker-compose up`
2. Create .jar file: `./mvnw clean install -DskipTests`
3. Execute .jar (will not work if the database is down): `java -jar target/polzunov-feast-server-0.0.1-SNAPSHOT.jar`

Container properties:
- POSTGRES_DB: `polzunov-feast-db`
- POSTGRES_USER: `polzunov`
- POSTGRES_PASSWORD: `feast`
- port: `5432`

### Prod mode
This mode has the following restrictions:
1. Admins (including root) cannot create or delete entities, only update
2. Root cannot create, delete or update admins

This mode uses postgresql database stored in docker container, the data will be present after the app restarts.

**Note:** access tokens will become invalid after the app restarts.

Go to the project directory and do the following:
1. Create a docker container with the database: `docker-compose up`
2. Create .jar file: `./mvnw clean install -DskipTests`
3. Execute .jar (will not work if the database is down): `java -Dspring.profiles.active=prod -jar target/polzunov-feast-server-0.0.1-SNAPSHOT.jar`

Container properties:
- POSTGRES_DB: `polzunov-feast-db`
- POSTGRES_USER: `polzunov`
- POSTGRES_PASSWORD: `feast`
- port: `5432`

## How perform admins' operations. REMOVE THIS SECTION BEFORE PROD
Send sign in request with username and password `root`. Response will contain root's token.
Use this token to do everything admins and root can.