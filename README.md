# Parcel Delivery Service (Backend)

A robust Spring Boot backend for a parcel delivery application, featuring location-based rider assignment, email notifications, and a secure API.

## 🚀 Features

*   **User Management**:
    *   Role-based access (Client, Rider, Admin).
    *   Secure authentication (Spring Security).
*   **Smart Rider Assignment**:
    *   **Proximity-based**: Assigns the nearest available rider using Haversine distance.
    *   **Load Balancing**: Prioritizes riders with fewer deliveries today if locations are unavailable.
*   **Delivery Lifecycle**:
    *   Status tracking: `PENDING` -> `ASSIGNED` -> `PICKED_UP` -> `DELIVERED`.
    *   Real-time updates via API.
*   **Notifications**:
    *   Email alerts via **Resend** (Assignment & Delivery confirmation).
*   **Tech Stack**:
    *   **Core**: Java 21, Spring Boot 3.
    *   **Database**: H2 (In-Memory, MySQL Mode) with Flyway Migrations.
    *   **Tools**: Lombok, MapStruct, Swagger UI.

## 🛠️ Setup & Run

### Prerequisites
*   Java 21 or higher.
*   Maven (wrapper included).

### Running the App
1.  **Clone/Download** the repository.
2.  **Run with Maven**:
    ```bash
    ./mvnw spring-boot:run
    ```
    *The app starts on port `8080`.*

3.  **Access Swagger UI**:
    *   Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore the API.

4.  **H2 Console**:
    *   URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    *   JDBC URL: `jdbc:h2:mem:perceldb`
    *   User: `sa`
    *   Password: `password`

## 🧪 Testing

The application seeds default data on startup (`DataLoader.java`):

| Role | Email | Password | Note |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@demo.com` | `password` | Can manage riders |
| **Client** | `client@demo.com` | `password` | Can request deliveries |
| **Rider 1** | `john@demo.com` | `password` | Busy |
| **Rider 2** | `jane@demo.com` | `password` | Available |
| **Rider 3** | `doe@demo.com` | `password` | Available |

### Quick Start API Flow
1.  **Login** as a client (or use Basic Auth).
2.  **Request a Delivery** (`POST /api/v1/deliveries/request`).
3.  **Check Email** for assignment notification (if seeded API key is valid).

## 📂 Project Structure

*   `src/main/resources/db/migration`: SQL scripts for schema changes.
*   `com.tritva.percel_delivery.services`: Business logic (Assignment, Lifecycle).
*   `com.tritva.percel_delivery.controller`: REST endpoints.
