vehicle-service

A Spring Boot REST API for managing a vehicle catalog. It supports full CRUD operations on vehicles, plus a search endpoint with filtering and pagination, backed by PostgreSQL and packaged with Docker.

Tech stack
Language / runtime: Java, Maven
Framework: Spring Boot 3.1 (Spring Web, Spring Data JPA, Bean Validation, Spring Security)
Database: PostgreSQL 15 (runtime), H2 in-memory (tests)
API docs: OpenAPI 3.0 specification (openapi.yaml)
Tooling: Docker, Docker Compose, Lombok
Features
Create, read, update, and delete vehicles through a REST API
Search vehicles by manufacturer, model, type, description, and price range, or by a single field and value
Paginated search results
Request validation with clear 400 responses, and 404 responses for missing vehicles
Custom exception handling
Unit tests, run automatically during the Docker build
Docker Compose setup that starts the app and its database together
Getting started
Prerequisites
Docker and Docker Compose
(Optional, for running without Docker) JDK and Maven
Run with Docker Compose
bash
git clone https://github.com/Auprety7/vehicle-service.git
cd vehicle-service
docker compose up --build

This starts:

Service	Port	Notes
postgres	5432	PostgreSQL 15, database vehiclesdb
vehicles-service	8080	The Spring Boot API

The database credentials in docker-compose.yaml are for local development only. Do not reuse them in any shared or production environment.

Configuration

The service reads its database settings from environment variables, which Compose sets for you:

Variable	Purpose
SPRING_DATASOURCE_URL	JDBC URL of the database
SPRING_DATASOURCE_USERNAME	Database user
SPRING_DATASOURCE_PASSWORD	Database password
SPRING_JPA_DATABASE_PLATFORM	Hibernate dialect
API

The full specification is in openapi.yaml. You can open it in Swagger Editor to browse it interactively.

Endpoints
Method	Path	Description	Success	Errors
GET	/vehicles	List all vehicles	200	
POST	/vehicles	Create a vehicle	201	400 validation error
GET	/vehicles/{id}	Get a vehicle by ID	200	404 not found
PUT	/vehicles/{id}	Update a vehicle	200	400 validation error, 404 not found
DELETE	/vehicles/{id}	Delete a vehicle	204	404 not found
GET	/vehicles/search	Search with filters and pagination	200	400 invalid parameters
Vehicle model
json
{
  "manufacturer": "Toyota",
  "model": "Camry",
  "year": 2023,
  "type": "sedan",
  "price": 25000.99,
  "description": "A reliable family car."
}
Field	Type	Rules
id	integer	Read-only, assigned by the server
manufacturer	string	
model	string	
year	integer	Minimum 1900
type	string	
price	number	Minimum 0
description	string	
Search

GET /vehicles/search accepts these optional query parameters:

Parameter	Type	Description
manufacturer	string	Filter by manufacturer
model	string	Filter by model
type	string	Filter by vehicle type
description	string	Filter by description
minPrice	number	Minimum price
maxPrice	number	Maximum price
field and value	string	Search a single named field for a value
page	integer	Page number, starting at 0
size	integer	Items per page, minimum 1

The response contains content (the matching vehicles), totalElements, totalPages, and pageNumber.

Authentication

All endpoints require HTTP Basic authentication. The service ships with a single in-memory demo user:

Username	Password	Role
user	password	USER

Example request:

bash
curl -u user:password http://localhost:8080/vehicles

Requests without valid credentials receive 401 Unauthorized. CSRF protection is disabled because the API is stateless and intended for non-browser clients. The demo credentials are hardcoded for local development only. Replace them with a real user store and externalized secrets before any real deployment.

Running tests
bash
mvn test

Tests run against an in-memory H2 database, so no PostgreSQL instance is needed. The Docker build also runs the tests before packaging the application.

Project structure
.
├── src/                  # Application and test source code
├── openapi.yaml          # OpenAPI specification
├── pom.xml               # Maven build and dependencies
├── Dockerfile            # Multi-stage build: test, package, run
└── docker-compose.yaml   # App + PostgreSQL for local development
