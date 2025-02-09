
---

# URL Shortener Service

A REST API for shortening long URLs into compact, shareable links. This service uses **Redis** as the persistent storage for managing shortened URLs and their corresponding long URLs.

## Features

- **Shorten URLs**: Convert long URLs into short, unique keys.
- **Redirect Short URLs**: Access the original URL by visiting the shortened link.
- **Delete Short URLs**: Remove a shortened URL from the system.
- **Idempotent Operations**: Creating a shortened URL multiple times with the same input will return the same result.
- **Error Handling**: Clear error messages for invalid requests or missing resources.

---

## Prerequisites

Before running the application, ensure you have the following installed:

- **Docker**: For running the Redis database.
- **Java 17 or higher**: For running the Spring Boot application.
- **Maven**: For building the project.

---

## Setup and Running the Application

### Step 1: Start Redis with Docker

The application uses Redis as the database. You can start a Redis instance using Docker:

```bash
docker run -d --name redis-container -p 6379:6379 redis
```

This command will start a Redis container and expose it on port `6379`.

### Step 2: Build and Run the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/url-shortener.git
   cd url-shortener
   ```

2. Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

---

## API Endpoints

### 1. Shorten a URL

**Endpoint**: `POST /`

**Request Body**:
```json
{
    "url": "https://www.example.com"
}
```

**Response**:
```json
{
    "key": "Y3S9wb",
    "long_url": "https://www.example.com",
    "short_url": "http://localhost/Y3S9wb"
}
```

**Example**:
```bash
curl -X POST http://localhost:8080/ \
-H "Content-Type: application/json" \
-d '{"url": "https://www.example.com"}'
```

---

### 2. Redirect to the Original URL

**Endpoint**: `GET /{key}`

**Response**:
- **302 Found**: Redirects to the original URL.
- **404 Not Found**: If the key does not exist.

**Example**:
```bash
curl http://localhost:8080/Y3S9wb -i
```

---

### 3. Delete a Shortened URL

**Endpoint**: `DELETE /{key}`

**Response**:
- **200 OK**: If the key was successfully deleted.
- **404 Not Found**: If the key does not exist.

**Example**:
```bash
curl -X DELETE http://localhost:8080/Y3S9wb -i
```

---

## Error Handling

The API returns appropriate HTTP status codes and error messages for invalid requests or missing resources.

**Example**:
```bash
curl -X POST http://localhost:8080/ \
-H "Content-Type: application/json" \
-d '{"wrong_key": "https://www.example.com"}' -i
```

**Response**:
```
HTTP/1.1 400 Bad Request
Missing field: url
```

---

## How It Works

1. **URL Shortening**:
   - The application generates a unique hash key for each long URL.
   - The key and URL are stored in Redis for persistence.
   - If the same URL is submitted multiple times, the same key is returned (idempotent operation).

2. **Redirection**:
   - When a user visits the shortened URL, the application looks up the key in Redis and redirects to the original URL.

3. **Deletion**:
   - The application removes the key and its associated URL from Redis.

---

## Bonus: Handling Hash Collisions

To handle potential hash collisions (different URLs generating the same key), the application appends a unique value (e.g., a timestamp) to the input before hashing. If a collision is detected, the process is repeated until a unique key is generated.

---

## Technologies Used

- **Spring Boot**: For building the REST API.
- **Redis**: For persistent storage of URLs and keys.
- **Docker**: For running Redis locally.
- **Maven**: For dependency management and building the project.

