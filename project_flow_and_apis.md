# SkillSync - Microservices Architecture & API Documentation

## 🏗 System Architecture Overview

SkillSync is built on a distributed microservices architecture using **Spring Cloud**. The system ensures high availability, scalability, and loose coupling between services.

### Core Ecosystem
- **Eureka Server**: Acts as the Service Registry. All microservices register themselves here.
- **Config Server**: Centralized repository for configuration files across all environments.
- **API Gateway (Port 8080)**: The single entry point for all client requests. It handles:
  - Routing to individual services.
  - JWT Authentication/Security.
  - Rate limiting and request logging.

---

## 🌊 Functional Flow

### 1. Authentication & Onboarding
1.  **OTP Verification**: Users enter their email to receive a 6-digit OTP (via `auth-service` & `NotificationService`).
2.  **Registration**: Once verified, users register as a `LEARNER` or `MENTOR`.
3.  **Login**: Users authenticate with credentials to receive a JWT.
4.  **Profile Setup**:
    - **Learners** update their interests in `LearnerService`.
    - **Mentors** apply for mentor status via `MentorService`, providing experience and skills. Admin approval is required.

### 2. Mentorship & Sessions
1.  **Skill Discovery**: Learners browse skills via `SkillService`.
2.  **Finding Mentors**: Mentors are searched based on skills.
3.  **Booking a Session**: Learners book slots from the mentor's schedule via `SessionService`.
4.  **Payment**: Booking triggers a payment request in `PaymentService` (Razorpay). Upon successful payment, the session is confirmed.
5.  **Notifications**: `NotificationService` sends email confirmations to both parties.

### 3. Collaboration & Feedback
1.  **Group Studies**: Users can create or join study groups via `GroupService`.
2.  **Reviews**: After a session, learners provide ratings and feedback via `ReviewService`.

---

## 🚀 Key API Endpoints

### 🔐 Auth Service (`/api/v1/auth`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/send-otp` | Sends a 6-digit OTP to user email |
| POST | `/verify-otp` | Verifies the OTP |
| POST | `/register` | Completes user registration |
| POST | `/login` | Authenticates user and returns JWT |
| POST | `/refresh` | Rotates the JWT using refresh token |
| GET | `/google-login` | Initiates Google OAuth2 login |

### 🎓 Learner Service (`/api/v1/learners`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/profile/{userId}` | Fetches detailed learner profile |
| PUT | `/profile/{userId}` | Updates learner profile/interests |

### 👨‍🏫 Mentor Service (`/api/v1/mentors`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/apply` | User applies to become a mentor |
| GET | `/applications` | (Admin) Lists pending applications |
| PUT | `/{id}/approve` | (Admin) Approves a mentor |
| GET | `/profile/{userId}`| Fetches full mentor profile & skills |
| POST | `/self/skills` | Mentor adds a skill to their profile |

### 🛠 Skill Service (`/api/v1/skills`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/` | Fetches all available skills |
| GET | `/category/{id}` | Fetches skills by category |
| POST | `/` | (Admin) Creates a new skill |

### 📅 Session Service (`/api/v1/sessions`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/book` | Initiates a session booking |
| GET | `/mentor/{id}` | Gets all sessions for a mentor |
| GET | `/learner/{id}` | Gets all sessions for a learner |

### 💳 Payment Service (`/api/v1/payments`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/create-order` | Generates a Razorpay Order ID |
| POST | `/verify` | Verifies Razorpay payment signature |

### 👥 Group Service (`/api/v1/groups`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/` | Creates a new study group |
| POST | `/{id}/join` | User joins a group |
| GET | `/{id}/members` | Lists all members of a group |

### 🔔 Notification Service
*(Mostly internal via OpenFeign/Kafka)*
- **Goal**: Handles Email and Push notifications.

---

## 🛠 Tech Stack
- **Backend**: Spring Boot, Spring Cloud, Spring Security (JWT)
- **Database**: Each service has its own dedicated SQL/NoSQL DB (Database-per-Service pattern).
- **Security**: Zero Trust Architecture using SecurityUtils to extract user ID from JWT.
- **Communication**: OpenFeign (Synchronous) and Event-driven (Asynchronous).
