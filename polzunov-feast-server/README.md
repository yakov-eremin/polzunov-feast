# Polzunov.Feast server

## Requirements
`java 17+`, `docker 24.0.2+`, `docker-compose 1.29.2+`

If you only use `dev` mode, docker and docker-compose are not required.

## Open API
To view open api copy contents of `/src/main/resources/openapi/spec/openapi.yaml` into the [swgger editor](https://editor.swagger.io/).

## How to run
**Note:** if you are on Windows and using the command prompt, run it as administrator and use `.\mvnw ...` instead of `./mvnw ...`.

<!--TODO remove before prod-->
To access secured endpoint see ['How to access secured endpoints'](#how-to-access-secured-endpoints).

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

<!--TODO remove before prod-->
## How to access secured endpoints
- User: sign up a new user and use returned authorization token.
- Root: sign in with email=`root@root.root` and password=`root123!`, use returned authorization token.
