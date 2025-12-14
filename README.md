💰 Expense Tracker Application (Spring Boot + JWT)

A secure, role-based Expense Tracker REST API built using Spring Boot, Spring Security (JWT), JPA/Hibernate, and MySQL.
Designed with clean layering, ownership enforcement, and production-oriented security practices.

🚀 Features

🔐 JWT Authentication & Authorization

👥 Role-based access control (ADMIN, USER)

💸 Expense Management

Create, update, and delete expenses

Ownership-based access (users can only manage their own expenses)

📁 Category Management

Admin-controlled category creation

📄 Pagination & Filtering

Filter expenses by date range and category

🧠 Stateless Security Architecture

🧩 DTO-based API responses

✅ Centralized exception handling

🗄️ Relational database mapping using JPA/Hibernate

🛠️ Tech Stack
Layer	Technology
Backend	Spring Boot
Security	Spring Security + JWT
ORM	JPA / Hibernate
Database	MySQL
Build Tool	Maven
Authentication	BCrypt
API Style	RESTful APIs
🧱 Architecture Overview
Controller
   ↓
Service (Business Logic)
   ↓
Repository (JPA)
   ↓
Database


Security Flow

Client → JWT Filter → SecurityContext → Controller

🔐 Roles & Permissions
Role	Permissions
ADMIN: Manage users, categories, and all expenses
USER	Manage only their own expenses

Ownership is enforced at the service layer, not the controller.

📦 API Endpoints
🔑 Authentication
Method	Endpoint	Description
POST	/api/auth/signup	Register user
POST	/api/auth/login	Login & receive JWT
📁 Categories
Method	Endpoint	Access
GET	/api/categories	Authenticated
POST	/api/categories	ADMIN
PUT	/api/categories/{id}	ADMIN
DELETE	/api/categories/{id}	ADMIN
💸 Expenses
Method	Endpoint	Access
GET	/api/expenses	USER / ADMIN
POST	/api/expenses	USER / ADMIN
PUT	/api/expenses/{id}	Owner / ADMIN
DELETE	/api/expenses/{id}	Owner / ADMIN

Supports:

Pagination

Date range filtering

Category filtering

👤 Users
Method	Endpoint
GET	/api/user
GET	/api/user/{id}
PATCH	/api/user/{id}

⚠️ Note: User APIs should be ADMIN-restricted in production.

🧪 Sample Request (Add Expense)

POST /api/expenses
Header

Authorization: Bearer <JWT_TOKEN>

{
  "amount": 450.75,
  "description": "Lunch with client",
  "category": {
    "id": 1
  }
}

📄 Sample Response (DTO)
{
  "id": 4,
  "amount": 450.75,
  "description": "Lunch with client",
  "date": "2025-12-14",
  "categoryId": 1,
  "categoryName": "Food",
  "userId": 4
}

🔐 Security Highlights

Stateless authentication using JWT

Password hashing with BCrypt

Role-based method security (@PreAuthorize)

Ownership checks at the service layer

JWT claims include email & role

⚠️ Known Improvements (Planned)

Refresh token support

Logout token blacklist

Global exception handler refinement

DTO validation using @Valid

Role hierarchy (ADMIN > USER)

Monetary values using BigDecimal

🧑‍💻 How to Run

Clone the repository

Configure application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=*****
jwt.secret=your-secret-key


Run the application:

mvn spring-boot: run


Test APIs using Postman

🎯 Learning Outcomes

This project demonstrates:

Real-world Spring Security with JWT

Correct handling of lazy loading & DTO mapping

Clean separation of concerns

Ownership-based authorization

Production-oriented backend design

📌 Author

Maheshwar
Java Full Stack Developer
Spring Boot | Security | REST APIs | JPA
