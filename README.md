# E-Commerce Management System

This project is a back-end E-Commerce Management System developed as a solution for the Amex technical assessment. It provides a robust and modular backend for managing users, products, carts, and orders. The system is built using Spring Boot, following a modular monolithic architecture, to ensure logical separation of concerns and maintainability.

## ✨ Features

-   **User Management:** Secure user registration and login endpoints.
-   **Product Catalog:** Browse products by category, search for specific products, and view a paginated list of all products.
-   **Inventory Management (Admin):**
    -   Create, update, and manage product inventory.
    -   Mark products as out of stock.
    -   View detailed inventory information (via product catalog endpoints).
-   **Shopping Cart:** Add, remove, and update the quantity of items in a user's cart.
-   **Order Processing:** Place orders from the cart, view order history, and see specific order details.
-   **Real-time Stock Management:** A key feature where the system ensures thread-safe and atomic stock updates, crucial for handling concurrent order placements for limited stock products.

## ⚙️ Technologies Used

* **Java 11+**: The core programming language.
* **Spring Boot 3**: The framework for building the application.
* **Spring Data JPA**: For database interaction and persistence.
* **MySQL**: The relational database used to store application data.
* **Lombok**: To reduce boilerplate code (getters, setters, constructors).
* **JUnit 5 & Mockito**: For writing unit tests and mocking dependencies.
* **Maven**: The build automation and dependency management tool.

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 11 or higher installed.
* Apache Maven installed.
* A running MySQL database instance.

### Database Setup

1.  **Create a database:** Create a new database in your MySQL instance.
    ```sql
    CREATE DATABASE products;
    ```
2.  **Configure `application.properties`:** Update the `src/main/resources/application.properties` file with your MySQL database credentials.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/amex_ecommerce?useSSL=false&serverTimezone=UTC
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```
    (Note: `ddl-auto=update` is set for development convenience. For production, `validate` or `none` is recommended.)

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/excess30/ecommerce.git](https://github.com/excess30/ecommerce.git)
    cd ecommerce
    ```
2.  **Build the project:**
    ```bash
    mvn clean install
    ```
3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080` by default.

## ✅ Meeting the Assessment Requirements

This project has been specifically designed to fulfill all the requirements outlined in the assessment document. Here's a breakdown of how each sub-task has been addressed:

### 1. Microservices Architecture and REST API Implementation

* **Modular Design:** While this is a single Spring Boot application, it is designed with a clear separation of concerns, with dedicated `UserService`, `ProductService`, `OrderService`, and `CartService` layers. This modularity allows for potential future decomposition into a true microservices architecture.
* **REST API Endpoints:** All required endpoints for `Customer facing` and `Admin` profiles have been implemented in their respective controller classes (`AuthController`, `ProductController`, `CartController`, `OrderController`).
* **Database:** A MySQL database is used for data persistence.

### 2. Unit Testing and Agile Development

* **JUnit 5 Unit Tests:** A comprehensive suite of unit tests using JUnit 5 and Mockito has been written for each service layer (`UserServiceTest`, `ProductServiceTest`, `OrderServiceTest`, `CartServiceTest`).
* **Agile Approach:** The project was developed iteratively, with a focus on building core functionality first and then adding robust test cases and features. This approach is reflected in the commit history.
* **Concurrent Stock Management:** This critical real-time scenario has been fully addressed:
    * The `ProductServiceImpl.decreaseStock()` method is thread-safe, utilizing the `synchronized` keyword and `@Transactional` annotation to ensure atomic and consistent stock updates.
    * Specific unit tests in `ProductServiceTest` and `OrderServiceTest` simulate concurrent requests using `ExecutorService` and `CountDownLatch` to verify that the system correctly handles limited stock, allowing only the available quantity to be sold and causing subsequent orders to fail gracefully.
