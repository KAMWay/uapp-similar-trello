# Similar to Trello

> ### Description
>Simple REST API for an app similar to Trello.
>

## Tech, Framework and other Dependencies

* Java version: **OpenJDK 17**
* Gradle version: **7.4.1**
* SpringBoot version: **2.7.1**
* Database: **PostgreSQL**, **H2** *(for test)*
* Thymeleaf version: **3.0.4**
* Bootstrap version: **5.x**
* Other: **Docker, Swagger API, JDBC API, FlywayDB, Validation, JUnit 5**

## Building

<details>
<summary>Instructions for building project</summary>

1. Clone this repository.
2. Building project.

**Gradle:**
```shell
./gradlew :build
```
3. Check that the **[similar-Trello]-0.0.1-SNAPSHOT.jar** has been created.
4. **Database Server** is required to run an application **without Docker**.
5. Edit **application.yml** for connect to you **Database**.
```
  datasource:
    url: jdbc:postgresql://localhost:5432/similar-trello
    username: postgres
    password: root
    driver-class-name: org.postgresql.Driver
```

</details>

## Running

<details>
<summary>Instructions for running project</summary>

for **Gradle:**
```shell
./gradlew :bootRun
```

for **Java:** 
```shell
 java -jar similartrello-0.0.1-SNAPSHOT.jar
```
for **Docker:**

```
docker-compose -f docker-compose.yml up -d --build app
```
</details>

## Testing

<details>
<summary>Instructions for testing project</summary>

```shell
./gradlew :test
```

</details>
