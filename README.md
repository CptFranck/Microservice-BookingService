# 🎟️ Microservice - Booking Service

The **Booking Service** is a core component of the microservice architecture.  
It manages **booking operations**, persists data via **MySQL**, communicates asynchronously with other services via **Kafka**, and exposes **REST APIs** documented with **OpenAPI**.

---

## 🚀 Overview

This service is responsible for handling bookings for events and venues.  
It provides REST endpoints to create, update, and retrieve bookings, and publishes events to Kafka to notify other services (e.g., Inventory Service) about changes in availability.

---

## ⚙️ Key Features

- **Spring Boot 3.5.6** – Core microservice framework  
- **Spring Data JPA** – ORM and database management  
- **MySQL** – Relational data storage  
- **Apache Kafka** – Event-driven communication between services  
- **SpringDoc / OpenAPI** – API documentation and Swagger UI  
- **Java 21** – Runtime language  

---

## 🧩 Architecture Integration

The Booking Service is part of a **5-repository microservice ecosystem**:

1. **Common** – Shared DTOs and utilities used across services.  
2. **Booking Service** – Manages bookings, emits Kafka events for inventory updates.  
3. **Inventory Service** – Tracks venue and event stock, consumes booking events to maintain consistency.  
4. **Order Service** – Handles customer orders and coordinates with booking and inventory services.  
5. **API Gateway** – Central entry point for all external clients, routes requests and handles authentication.

The Booking Service mainly communicates with **Inventory** and **Order** services via **Kafka** and exposes its REST APIs through the **API Gateway**.
