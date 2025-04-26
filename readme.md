# Product Management System

This is a REST API application based on Spring Boot 3.2 and MySQL 8 for product management.

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Getting Started](#2-getting-started)
3. [API Documentation](#3-api-documentation)
4. [Testing with Postman](#4-testing-with-postman)
5. [Database Information](#5-database-information)
6. [Technical Details](#6-technical-details)

## 1. Project Overview

### 1.1 Project Structure

The project is divided into two main parts:

- **Product Service**: Provides REST API for product management
- **OAuth2 Authorization Server**: Provides authentication and authorization functions

### 1.2 Technology Stack

- Java 21
- Spring Boot 3.2.3
- Spring Security with OAuth 2.1
- Spring Data JPA
- Flyway database migration
- MySQL 8 database
- Lombok for simplified development
- Gradle build tool

### 1.3 Key Features

- **Product Management**
  - Search products with pagination and full-text search
  - Retrieve, create, update, and delete products
  - MySQL full-text search integration for efficient queries

- **Security**
  - OAuth 2.1 authentication and authorization
  - JWT token-based security
  - Role-based access control

## 2. Getting Started

### 2.1 Prerequisites

- JDK 21 or higher
- MySQL 8 database
- Gradle
- Lombok plugin (for IDE development)
- Postman (for API testing)

### 2.2 Installation Steps

1. **Clone the repository**

2. **Create MySQL database**

```sql
CREATE DATABASE product CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Configure database connection**
   - Modify settings in `product-service/src/main/resources/application.yml`

4. **Start the OAuth2 server**

```bash
cd oauth-service
gradle bootRun
```

5. **Start the product service**

```bash
cd product-service
gradle bootRun
```

## 3. API Documentation

### 3.1 API Endpoints

All API endpoints are prefixed with `/api` at the server level.

#### 3.1.1 Product API

| Method | Endpoint                | Description                                 |
|--------|-------------------------|---------------------------------------------|
| POST   | `/products/search`      | Search products with pagination & full-text |
| GET    | `/products/{id}`        | Get product details by ID                   |
| POST   | `/products`             | Create new product                          |
| PUT    | `/products/{id}`        | Update existing product                     |
| DELETE | `/products/{id}`        | Delete product                              |

#### 3.1.2 Authentication API

| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/auth/test`            | Test authentication status       |
| GET    | `/auth/userinfo`        | Get current user information     |

### 3.2 Request/Response Examples

#### 3.2.1 Search Products

Request:

```json
{
  "searchTerm": "phone",
  "page": 0,
  "size": 10,
  "useFulltext": true
}
```

#### 3.2.2 Create Product

Request:

```json
{
  "productCode": "P101",
  "name": "New Smart Phone",
  "description": "Latest smart phone with high-performance processor",
  "price": 4999.00,
  "stock": 50
}
```

## 4. Testing with Postman

The project includes Postman files to simplify API testing.

### 4.1 Importing the Collection

1. Open Postman
2. Click "Import" in the upper left corner
3. Select `tangrd-product.postman_collection.json`
4. The collection will be imported with all preconfigured requests

### 4.2 Setting Up Environment

The project includes a ready-to-use environment file with preconfigured variables.

#### 4.2.1 Importing the Environment

1. Open Postman
2. Click "Import" in the upper left corner
3. Select `Local.postman_environment.json`
4. The environment will be imported with all necessary variables

#### 4.2.2 Environment Variables

| Variable Name    | Default Value           | Description                           |
|------------------|------------------------|---------------------------------------|
| product-service  | http://localhost:8800/api | Base URL for the product service API  |
| oauth-service    | http://localhost:9000  | Base URL for the OAuth server         |

#### 4.2.3 Using the Environment

1. Select "Local" environment from the dropdown (top-right corner)
2. All requests automatically use these variables
3. To modify variables:
   - Go to "Environments" section
   - Select "Local"
   - Edit values
   - Click "Save"

### 4.3 Authentication

The collection is configured with OAuth 2.0 authentication:

1. Select any request in the collection
2. Go to the "Authorization" tab
3. Click "Get New Access Token"
4. Login with username: `user`, password: `password`
5. Click "Use Token"

### 4.4 Using Full-Text Search

Enable MySQL's full-text search for efficient queries:

```json
{
  "searchTerm": "phone",
  "page": 0,
  "size": 10,
  "useFulltext": true
}
```

The `useFulltext` parameter leverages MySQL's built-in full-text search capabilities, which are significantly faster than regular LIKE queries for large datasets.

## 5. Database Information

### 5.1 Database Schema

The project uses Flyway for database migration management. Key tables:

- `products`: Stores product information with full-text indexing

### 5.2 Migration Scripts

Initialization scripts are located in:
- `product-service/src/main/resources/db/migration`

## 6. Technical Details

### 6.1 Project Structure

```
├── product-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.tangrd.product
│   │   │   │       ├── config         # Configuration classes
│   │   │   │       ├── controller     # Controller layer
│   │   │   │       ├── dto            # Data Transfer Objects
│   │   │   │       ├── entity         # Entity classes
│   │   │   │       ├── exception      # Exception handling
│   │   │   │       ├── oauth2         # OAuth2 related classes
│   │   │   │       ├── repository     # Data access layer
│   │   │   │       └── service        # Service layer
│   │   │   └── resources
│   │   │       ├── db/migration       # Flyway migration scripts
│   │   │       └── application.yml    # Application configuration
│   │   └── test
├── oauth-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.tangrd.oauth
│   │   │   └── resources
│   │   │       └── application.yml    # OAuth server configuration
```
