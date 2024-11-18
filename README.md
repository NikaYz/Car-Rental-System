# Car Rental System

A **Car Rental System** developed in **Java** with **MySQL** for database management. The system supports **Admin** and **User** roles to manage rental records, customers, drivers, and car bookings.

- **Admin**: View all rental records, add/delete employees, manage drivers and cars, and control car rentals.
- **User**: Book a car rental and view their rental records.

## Features

- **Admin**:
  - View all rental records
  - Add and delete employees
  - Manage car and driver information
  - View and manage all rental records
- **User**:
  - Book new car rentals
  - View personal rental records

## Technologies Used

- **Programming Language**: Java
- **Database**: MySQL
- **JDBC**: For connecting Java to MySQL

## Dependencies

- **MySQL**: Ensure MySQL is installed and running on your machine.
- **JDBC Driver**: MySQL Connector/J (downloadable from [MySQL official website](https://dev.mysql.com/downloads/connector/j/))

## Setup & Installation

### 1. Create the Database and Tables in MySQL
Run the following SQL commands to set up the `car_rental` database and create the required tables (`users`, `customers`, `drivers`, `carInfo`, `rentalinfo`):

```sql
CREATE DATABASE car_rental;
USE car_rental;

CREATE TABLE users (
    user_id INT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(30)
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    email VARCHAR(50) NOT NULL,
    phoneno BIGINT NOT NULL UNIQUE
);

CREATE TABLE drivers (
    driver_id INT,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    phone_no BIGINT UNIQUE,
    car_type VARCHAR(30) NOT NULL,
    car_model VARCHAR(30) NOT NULL,
    car_no INT NOT NULL,
    working_in_company BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (driver_id, car_no)
);

CREATE TABLE carInfo (
    id INT UNIQUE,
    type VARCHAR(10) PRIMARY KEY,
    seater SMALLINT NOT NULL,
    description TEXT,
    rent_per_day INT NOT NULL
);

CREATE TABLE rentalinfo (
    customer_id INT,
    driver_id INT,
    car_type VARCHAR(10),
    rental_start DATE NOT NULL,
    rental_end DATE NOT NULL,
    total_amount INT,
    PRIMARY KEY (customer_id, driver_id, rental_start)
);
```
Run the mysql command in sample.sql in bin folder to insert inputs for drivers and car info inside the database folder. One could insert their own data rows also in the table. 
### 2. Update Database Credentials
In the `CarRentalSystem.java` file, update the following fields with your MySQL username, password, and database URL:

```java
String url = "jdbc:mysql://localhost:3306/car_rental";
String user = "your_mysql_user";
String password = "your_mysql_password";
```

### 3. Add JDBC Driver
Download the MySQL JDBC driver (mysql-connector-java) from the MySQL official website and add it to your Java project.

### 4. Compile and Run the Application
After setting up the database and credentials, compile and run the program using the following commands:

```bash
javac CarRentalSystem.java
java CarRentalSystem
```

### 5. Logging In
#### Admin:
- Username and password are required for login. Admin has access to all rental records and can manage employees, drivers, and car rentals.

#### User:
- After logging in as a user, you can view your personal rental records and book new rentals.

## How the System Works

### Admin Operations:
- Admin can manage users by adding and deleting employees.
- Admin can manage driver information (e.g., add/remove drivers, view driver details).
- Admin can manage car information (e.g., add/remove cars, view car details).
- Admin can view all rental records and manage them (e.g., change rental details, view rental history).

### User Operations:
- Users can book a car rental by selecting the car type, start and end date for the rental.
- Users can view their own rental records (past and upcoming).

## Example Workflow
1. Admin logs in and views all rental records, manages employees, drivers, and cars.
2. Admin can add/delete employee accounts, and update car/driver information.
3. Users log in to see their own car rental history.
4. Users can book a new rental by providing details such as car type, rental dates, etc.

## Conclusion
This Car Rental System allows both admins and users to perform their respective roles effectively. Admins can manage users, rental records, cars, and drivers, while users can book rentals and view their own rental history. The system demonstrates how to use JDBC to connect a Java application to a MySQL database for storing and retrieving data.
