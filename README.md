# Library Management System📚

This repository contains a Library Management System project implemented in Java. The system is designed to manage library operations and managing user accounts.

## Features

- **User Management**: Create, update, delete, and view user accounts.
- **Book Management**: Add, update, delete, and view books in the library.
- **Search Functionality**: Search for books and users based on various criteria.
- **Role-Based Access Control**: Implement security with user roles and permissions.

## Software Architecture

The Library Management System is designed using a layered architecture to separate concerns and improve maintainability. The main architectural components are:

### 1. Presentation Layer (UI Layer)
This layer is responsible for interacting with the users. It includes the user interface components such as forms, buttons, and dialogs. The presentation layer captures user input and displays the results.

### 2. Business Logic Layer (Service Layer)
This layer contains the business logic of the application. It processes user inputs from the presentation layer, performs necessary validations, and interacts with the data access layer to perform CRUD operations.

### 3. Data Access Layer (Repository Layer)
This layer is responsible for interacting with the database. It includes Data Access Objects (DAOs) that provide methods to perform CRUD operations on the database.

### 4. Database Layer
This layer includes the database schema and the actual database. It stores all the data related to users and books.

## Design Patterns

The project uses several design patterns to ensure a robust and maintainable codebase:

- **Singleton Pattern**: Used for database connection management to ensure a single instance of the database connection throughout the application.
- **Factory Pattern**: Used to create instances of various objects such as users and books, encapsulating the instantiation logic.
- **Observer Pattern**: Used to notify various components of changes in the system.
