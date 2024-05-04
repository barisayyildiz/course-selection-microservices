# Course Selection Application

The Course Selection Application is a microservice-based system designed to streamline the process of course selection for students and management of courses by professors. It follows a microservice architecture and utilizes Kafka brokers for inter-service communication.

## Architecture
![image](https://github.com/barisayyildiz/course-selection-microservices/assets/37713845/33cd0e86-1530-4991-ac2d-0db96e844a01)


## Tech Stack
- Java
- Spring Boot
- Kafka
- Redpanda
- Docker
- Redpanda
- PostgreSQL
- Elasticsearch, Logstash, Kibana (ELK Stack) with Filebeat

## Services
###  course-service
The Course Service handles everything about courses: names, codes, professors, capacity, and enrollment numbers. To keep things efficient, it maintains a separate professor table. Plus, it uses a smart approach called the **Saga pattern** to smoothly manage enrollments and drops in sync with the Student Service.

#### Endpoints
- GET /api/courses: Retrieves all courses with pagination support.
- GET /api/courses/search: Searches for courses by name.

### professor-service
The Professor Service takes care of everything related to professors: signing up, logging in, updating profiles, and managing the courses they teach. It ensures secure access to protected endpoints by implementing authentication using JWT tokens.

#### Endpoints
- POST /auth/professor/signup: Registers a new professor.
- POST /auth/professor/login: Logs in a professor and returns a JWT token.
- GET /api/professor: Retrieves the profile information of the logged-in professor.
- PUT /api/professor: Updates the profile information of the logged-in professor.
- GET /api/professor/courses: Retrieves the list of courses taught by the professor.
- POST /api/professor/course: Adds a new course taught by the professor.
- PUT /api/professor/course/{id}: Updates information about a specific course taught by the professor.
- DELETE /api/professor/course/{id}: Deletes a course taught by the professor.

### student-service
In the Students Service, students manage their course enrollments and drops with ease. Just like the Professor Service, it uses JWT tokens for user authentication, ensuring a secure login experience.

#### Endpoints:
- POST /auth/professor/signup: Registers a new student.
- POST /auth/professor/login: Logs in a student and returns a JWT token.
- GET /: Retrieves the information of the currently logged-in student.
- PUT /: Updates the profile information of the currently logged-in student.
- POST /enroll: Enrolls the student in a course specified by the course ID.
- POST /drop: Drops the course specified by the course ID from the student's enrollment.

### apigateway
The API Gateway, acts as the entry point for all incoming requests and efficiently distributes them to the corresponding services based on predefined routes. Leveraging Spring Cloud's gateway library, it provides robust routing capabilities.

### eureka-server
Additionally, there's Eureka service for service registry.

### log-monitoring
The Log Monitoring setup ensures that application logs are collected, processed, and made accessible for analysis using a combination of Filebeat, Logstash, Elasticsearch, and Kibana.

#### Components
- **Filebeat:** Responsible for reading logs from Docker containers and forwarding them to Logstash.
- **Logstash:** Receives logs from Filebeat, processes them, and sends them to Elasticsearch for indexing.
- **Elasticsearch:** Stores the processed logs in an indexed format, making them searchable and analyzable.
- **Kibana:** Provides a user-friendly interface for visualizing and analyzing the logs stored in Elasticsearch.

## Getting Started
### Dependencies
- Docker

```
git clone https://github.com/barisayyildiz/course-selection-microservices.git
cd ./course-selection-microservices
docker compose up
```

### Swagger
- Course Service: http://localhost:8000/swagger-ui/index.html
- Professor Service: http://localhost:8001/swagger-ui/index.html
- Student Service: http://localhost:8002/swagger-ui/index.html

### Kibana
http://localhost:5601


### Redpanda UI
http://localhost:30092
