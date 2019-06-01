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

#### Run tests
``./gradlew test``

#### Run application locally
``./gradlew bootRun``

#### Build application (assemble fatjar) 
``./gradlew assemble``

#### Build application (assemble fatjar and test)
``./gradlew build``

### 2.3 Code Analysis

1. ``docker run docker run -d --name sonarqube -p 9000:9000 sonarqube``
2. ``./gradlew sonar``

<!-- referenced links here -->
[contribution-guide]:CONTRIBUTION.md
