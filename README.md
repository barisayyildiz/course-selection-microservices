# Description
This project implements a microservices-based system for course selection in a university including services for students, courses and professors. All the components runs in a distributed kubernetes cluster.
#### Tech Stack
- Java
- Spring Boot
- PostgreSQL
- Kafka
- Docker
- Kubernetes
- Elasticsearch
- Kibana
- Prometheus
- Grafana

## Architecture

![image](https://github.com/barisayyildiz/course-selection-microservices/assets/37713845/b94788b6-7e19-4d1c-b3c9-bc3fb6848f83)

## Services

### Course Service
- [ ] Courses will be stored in a PostgreSQL database
- [ ] It will consume Kafka and update courses based on the CRUD operations on the professor service
- [ ] It will expose endpoints for Professor service to use so that it can display the related courses on it's UI

### Student Service
- [ ] Students information will be stored in a PostgreSQL database
- [ ] It will consume Kafka and update courses based on the CRUD operations on the other services
- [ ] It will have a UI where students can enroll to courses
- [ ] Students will be able to see course capacities in real time (?)
- [ ] Saga pattern will be implemented for the transactions
- [ ] Authorization and authentication will be implemented

### Professor Service
- [ ] Professor information will be stored in a PostgreSQL database
- [ ] It will consume Kafka and update courses based on the CRUD operations on the other services
- [ ] It will have a UI where professors can add, update or drop a course
- [ ] Authorization and authentication will be implemented

### Notification Service
- [ ] Students will be notified if one of their selected course is dropped
- [ ] Professors will be informed if one of their courses hit the maximum capacity

