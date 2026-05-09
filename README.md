# Link Analytics & Shortener API

A high-performance, scalable URL shortening and analytics platform built with **Spring Boot** and **Java**. This project
goes beyond basic URL shortening by implementing **Asynchronous Processing** for click tracking and **In-Memory Caching
** to handle high-traffic redirections without database bottlenecks.

##  Tech Stack & Architecture

* **Language:** Java 17+
* **Framework:** Spring Boot (Web, Data JPA, Cache, Async)
* **Database:** Microsoft SQL Server (MSSQL)
* **Boilerplate Reduction:** Lombok
* **Architecture:** 3-Tier Architecture (Controller, Service, Repository)

##  Key Features & Engineering Highlights

* **Base62 Encoding:** Utilizes a custom Base62 algorithm to generate secure, short, and collision-resistant URL codes.
* ** In-Memory Caching (`@Cacheable`):** Frequently accessed short links are cached in RAM. This drastically reduces
  database read operations during URL redirection, allowing the system to scale under heavy load.
* ** Asynchronous Analytics (`@Async`):** Click events (Timestamp, IP Address, User-Agent) are recorded asynchronously.
  The user is redirected to the target URL instantly, while the analytics data is processed and saved to the database in
  a separate background thread.
* **Data Integrity:** Implements robust entity relationships (One-to-Many) between Links and Click Events using
  Hibernate/JPA.

---

##  API Endpoints

### 1. Create a Short Link

Converts a long URL into a short code.

* **URL:** `/api/v1/links`
* **Method:** `POST`
* **Body (JSON):**
  ```json
  {
    "originalUrl": "[https://www.github.com/meminksr](https://www.github.com/meminksr)"
  }

Response (String): Returns the generated short URL (e.g., http://localhost:8080/api/v1/links/aB3x9).

2. Redirect to Original URL
   Redirects the user to the original long URL and asynchronously tracks the click event.

URL: /{shortCode} (e.g., /aB3x9)

Method: GET

Response: HTTP 302 (Found) Redirect to the original URL.

3. Get Link Statistics
   Retrieves total click statistics for a specific short link.

URL: /api/v1/links/{shortCode}/stats

Method: GET

Response (JSON):

JSON
{
"originalUrl": "[https://www.github.com/meminksr](https://www.github.com/meminksr)",
"shortCode": "aB3x9",
"totalClicks": 14
}


---

##  How to Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/meminksr/LinkAnalytics.git](https://github.com/meminksr/LinkAnalytics.git)

Database Configuration:
Ensure MSSQL is running. Update the src/main/resources/application.properties file with your database credentials:

Properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=your_db;encrypt=true;trustServerCertificate=true;
spring.datasource.username=your_username
spring.datasource.password=your_password

3. **Run the Application:**
   Execute the following Maven command or run the `LinkAnalyticsApplication.java` via your IDE:
   ```bash
   mvn spring-boot:run
   ```

---
*Developed as a showcase of scalable backend engineering.*