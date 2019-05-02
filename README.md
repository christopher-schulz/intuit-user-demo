# Intuit Demo - Spring Boot
An example of the intuit demo user service wtih Spring boot.

## Structure
This is organized into a few projects
- parent: contains the parent pom
- auth-service: contains the implementaion of the auth service
- user-api-contract: contains the openapi spec for the user service
- user-service: contains the implementation of the user service

## Setup

### Install Prerequisites
Install java >= 1.8

### Setup Project
```bash
# Generate source from openapi spec
cd user-api-contract
mvn clean install
cd ..

# Run Auth Service
cd auth-service
mvn spring-boot:run

# Also, Run User Service
cd user-service
mvn spring-boot:run
```
