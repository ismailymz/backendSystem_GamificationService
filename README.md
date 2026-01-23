# Gamification Backend Service
This repository contains a backend system developed as part of the **Backend Systems – Portfolio 03**.
The project is implemented using **Java, Quarkus, RESTEasy**, and follows the **Hexagonal Architecture**.

---
# Project Aim

The backend motivates drivers to drive safer and more efficiently by awarding points and achievements based on submitted trip reports.
Trip data is provided by an external system, processed by business rules, and stored for later evaluation and ranking.

---
## Technical Overview

- **Language:** Java (JDK 21)
- **Framework:** Quarkus (RESTEasy Reactive)
- **Architecture:** Hexagonal Architecture
- **Persistence:** JPA (Hibernate) with H2 database
- **Build Tool:** Maven
- **API Style:** REST (including filtering, paging, and HATEOAS)
- **Authentication:** HTTP Basic Authentication
- **Tests:** Unit tests, persistence tests, and API integration tests
- **Containerization:** Docker
---
## Prerequisites
To run the project locally or in a container, the following tools are required:
- **Docker**
- **Java 21** (only required if running without Docker)
- **Maven 3.9+** (only required if running without Docker)
---

## Running the Application with Docker (Recommended)

### 1. To build the project and execute all tests, run the following command in the project root:
From the project root directory:
```bash
mvn clean verify

```
```bash

docker build -t gamification-service . 
```
```bash
docker run --rm -p 8080:8080 --name gamification-backend gamification-service 
```
The application will be accessible at `http://localhost:8080`.
---


## Running the Application without Docker
### 1. Build the project
From the project root directory:
```bash
mvn clean verify
```
### 2. Run the application
```bash
mvn quarkus:dev
```
The application will be accessible at `http://localhost:8080`.
---



# PROJECT MEMBERS

- **Member 1:** Erdil Kunt (Matrikelnummer: 5124080 )
- **Member 2:** Erkin Caliskan (Matrikelnummer:  5123096 )
- **Member 3:** Ismail Yilmaz (Matrikelnummer:  5123090 )