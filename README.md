# Yapam Project
[![Build Status](https://travis-ci.org/kraddatz/yapam-server.svg?branch=master)](https://travis-ci.org/kraddatz/yapam-server)
[![Coverage Status](https://coveralls.io/repos/github/kraddatz/yapam-server/badge.svg?branch=master)](https://coveralls.io/github/kraddatz/yapam-server?branch=master)

## 1. Contributing
Before contributing, please read the [contribution-guide][contribution-guide]

## 2. Development

### 2.1 Local Database
For local development just spin up a MySQL DB

``docker run -e MYSQL_ROOT_PASSWORD=secret -p 3306:3306 -d mysql:latest``

### 2.2 Gradle
This project uses gradle as build tool

1. start and configure keycloak
2. create a _yapam.yaml_ in the root of the project directory
    ```yaml
    yapam:
      mail:
        host: 
        port: 
        username: 
        password: 
        protocol: 
        test-connection: true
        message-sender: 
    
      datasource:
        url: jdbc:mysql://localhost/yapam?createDatabaseIfNotExist=true&user=root&password=secret
    
      host: localhost:8080
    
    keycloak:
      auth-server-url: "http://localhost:8888"
      realm: 
      resource: 
      principal-attribute: preferred_username
      public-client: true
    ```
3. ``./gradlew bootRun``

## 3. Run, run, run

1. start and configure keycloak
2. create an environment file like this: /path/to/.yapam-server.env
    ```
    YAPAM_MAIL_HOST=
    YAPAM_MAIL_PORT=
    YAPAM_MAIL_USERNAME=
    YAPAM_MAIL_PASSWORD=
    YAPAM_MAIL_PROTOCOL=
    YAPAM_MAIL_TEST-CONNECTION=
    YAPAM_MAIL_MESSAGE-SENDER=
    YAPAM_DATASOURCE_URL=jdbc:mysql://yapam-db/yapam?createDatabaseIfNotExists=true&user=root&password=secret
    YAPAM_HOST=http://localhost:8080
    KEYCLOAK_AUTH-SERVER-URL=http://localhost:8888
    KEYCLOAK_REALM=
    KEYCLOAK_RESOURCE=
    KEYCLOAK_PRINCIPAL-ATTRIBUTE=preferred_username
    KEYCLOAK_PUBLIC-CLIENT=true    
    ```    
3. ``docker run -it -p 8080:8080 --env-file /path/to/.yapam-server.env yapam-server`` 

<!-- referenced links here -->
[contribution-guide]:CONTRIBUTION.md
