# Parcel Delivery Flow – Example Scenario

## 📦 How the Delivery App Works – Client Perspective

**Scenario:** Client John wants a parcel delivered from his shop on Mombasa Road to a customer in Westlands. Rider Alex will perform the delivery.

### 1️⃣ Step 1: John Hails a Rider

1. **Request:** John opens the app on his phone.
2. **Pinning:** He pins his shop on a map (pickup location) and pins the drop-off location (customer in Westlands).
3. **Dispatch:** He taps **Request Delivery**. The system automatically finds the nearest available rider — Alex — and sends him the delivery request.
4. **Visibility:** John sees a map showing his pickup point, drop-off point, and the rider’s last location.

### 2️⃣ Step 2: Rider Accepts & Pickup

1. **Notification:** Alex gets a notification on his app that a delivery is assigned.
2. **Acceptance:** He taps **Accept** to confirm he will handle it.
3. **Tracking:** Alex travels to John’s shop. On the map, John sees Alex’s last reported location updating every 30–60 seconds (basic live location).
4. **Pickup:** When Alex reaches the shop, he taps **Picked Up** in his app.
5. **Status Update:** John’s app updates: `Status = PICKED UP`.

### 3️⃣ Step 3: Delivery to Customer

1. **Transit:** Alex drives to the customer in Westlands.
2. **Monitoring:** John sees Alex moving along the map — his location updates every 30–60 seconds.
3. **Completion:** When Alex hands over the parcel, he taps **Delivered**.
4. **Status Update:** John sees `Status = DELIVERED`.
5. **Availability:** Alex becomes available for the next delivery.

### 4️⃣ Features / Deliverables – What the Client Sees

| Feature | What the User Sees |
| :--- | :--- |
| **Request Delivery** | Pin pickup & drop-off on map, enter parcel details, tap “Request” |
| **Rider Assignment** | App shows assigned rider’s name (Alex) and estimated distance |
| **Live Location Updates** | Rider’s icon moves on map every 30–60 seconds |
| **Delivery Status** | Updates like PENDING → ASSIGNED → PICKED UP → DELIVERED |
| **Delivery History** | Map and list of past deliveries with dates, status, and routes |
| **Admin Dashboard** | View all riders on a map, check active deliveries, and see last known locations |

### 5️⃣ How Maps & Location Work

- **Pickup & Drop-off Pins:** John taps his shop and customer location on the map.
- **Rider Icon:** Shows where Alex is in relation to John and the destination.
- **Basic Live Location:** Rider updates every 30–60 seconds — movement is “step by step,” not real-time smooth animation.
- **Route Overview:** Optional line from pickup to drop-off (MVP).

> **Think of it like:** “I see Alex moving towards me on the map — I know when my parcel will arrive.”

---

## 🛠️ System Breakdown & Technical Architecture

### 1️⃣ Core Features for MVP

| Feature | Module | Description |
| :--- | :--- | :--- |
| **Request Delivery** | Client App | Enter pickup/drop-off, parcel details, hail rider |
| **Delivery Assignment** | API / Backend | Automatic dispatch to nearest available rider |
| **Rider Acceptance** | Rider App | Accept/decline delivery (optional auto-accept) |
| **Rider Availability** | Backend | Auto-set unavailable when assigned, back to available after delivery |
| **Delivery Lifecycle** | API / Backend | Status: `PENDING` → `ASSIGNED` → `PICKED_UP` → `DELIVERED` |
| **Basic Live Location** | Client / Admin App | Rider location updates every 30–60s, displayed on map |
| **Delivery History** | Client / Admin | List of past deliveries with status and timestamps |

### 2️⃣ System Components

#### A. Mobile App (React Native – Expo)

**1. Client App**
- Request a delivery
- View delivery status & updates
- Map showing pickup, drop-off, and rider’s last known location
- Delivery history list

**2. Rider App**
- View assigned deliveries
- Accept/Decline request (or auto-accept)
- Confirm pickup & delivery
- Rider availability auto-updates
- Send location updates to backend every 30–60s

#### B. Web / Admin Dashboard
- View all active deliveries
- See rider availability & last known location
- Manual reassign if needed
- View delivery history
- Simple reporting for operations

#### C. Backend / API (Spring Boot)
- User management (Admin, Rider, Client)
- **Delivery management endpoints:**
    - `POST /api/v1/deliveries/request` – client requests delivery
    - `GET /api/v1/deliveries/history` – client view delivery history
    - `GET /api/v1/rider/deliveries` – rider view assigned deliveries
    - `PATCH /api/v1/rider/deliveries/{id}/status` – rider updates status (PICKED_UP, DELIVERED)
    - `PATCH /api/v1/rider/location` – rider sends location updates
- Automatic dispatch logic
- Status updates & timestamp recording
- Maps integration for location coordinates (pickup, drop-off, rider last location)

### 3️⃣ Summary Table – Deliverables by Layer

| Layer | Deliverables | Notes |
| :--- | :--- | :--- |
| **Client App** | Delivery request, delivery status, map, history | Mobile (React Native) |
| **Rider App** | Accept delivery, pickup/delivery confirmation, live location updates | Mobile (React Native) |
| **Admin Web** | View all deliveries, rider status, manual reassignment | Web (React) / optional |
| **API / Backend** | REST endpoints, delivery lifecycle, rider assignment, location updates | Spring Boot |

### ✅ Key MVP Characteristics

- **Operational:** Fully operational internal delivery system.
- **Tracking:** Basic live location (step-by-step map).
- **Billing:** No payment or pricing in app (invoicing done by company).
- **Scale:** Works for 20–30 riders.
- **Philosophy:** Simple, reliable, low-cost, easy to scale.
