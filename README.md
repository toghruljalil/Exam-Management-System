# Exam Management System

Exam Management System is a JavaFX desktop application designed to help academic departments manage exam planning processes. It provides separate interfaces for administrators and coordinators, stores data in PostgreSQL, and supports course, student, classroom, exam schedule, seating, Excel, and PDF related operations.

## Repository Information

Recommended repository name:

```text
exam-management-system-javafx
```

Recommended repository description:

```text
A JavaFX and PostgreSQL based desktop application for managing courses, students, classrooms, exam schedules, and academic exam planning exports.
```

## Features

- Role-based login system
- Admin panel for system management
- Coordinator panel for department-level operations
- Course management
- Student management
- Classroom management
- Exam schedule generation
- Seating arrangement support
- PostgreSQL database integration
- Excel import support for course and student lists
- Excel export support for generated exam schedules
- PDF generation support
- JavaFX FXML based desktop user interface

## Technologies Used

- Java 23
- JavaFX 23.0.1
- Maven
- PostgreSQL
- PostgreSQL JDBC Driver
- Apache POI
- Apache PDFBox
- ControlsFX
- Log4j

## Project Structure

```text
.
|-- pom.xml
|-- proje_db
|-- README.md
|-- src
|   `-- main
|       |-- java
|       |   |-- Main.java
|       |   |-- LoginController.java
|       |   |-- AdminController.java
|       |   |-- CordinatorController.java
|       |   |-- SinavProgramiOlusturucu.java
|       |   |-- PDFOlusturucu.java
|       |   `-- model classes
|       `-- resources
|           |-- Login.fxml
|           |-- AdminPanel.fxml
|           |-- CordinatorPanel.fxml
|           `-- styles
|               `-- dashboard.css
`-- target
```

## Main Modules

### Login

The login module authenticates users from the PostgreSQL database. After successful login, users are redirected to the correct panel according to their role.

Supported roles include:

- Admin
- Coordinator

### Admin Panel

The admin panel is used for system-level management operations. It provides administrative access to the application's core data and user-related workflows.

### Coordinator Panel

The coordinator panel is used for department-level academic operations. Coordinators can manage courses, students, classrooms, and exam planning data.

### Exam Scheduler

The exam scheduler creates exam plans by using course, student, classroom, date, duration, and conflict information. It helps departments create organized exam programs while considering classroom capacity and scheduling constraints.

### Export Operations

The project includes export functionality for generated exam schedules and related outputs. Apache POI is used for Excel operations, and Apache PDFBox is used for PDF generation.

## Database

The project uses PostgreSQL. A database dump file is included in the repository as:

```text
proje_db
```

The database includes tables for:

- Courses
- Classrooms
- Users
- Students
- Exams

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE "Sinav_Yonetim_Sistemi";
```

Import the provided database dump:

```bash
psql -U postgres -d Sinav_Yonetim_Sistemi -f proje_db
```

If you are using Windows PowerShell and `psql` is available in your PATH, you can run:

```powershell
psql -U postgres -d Sinav_Yonetim_Sistemi -f .\proje_db
```

## Database Configuration

Before running the application, make sure the PostgreSQL connection settings match your local environment.

Example connection configuration:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/Sinav_Yonetim_Sistemi";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

Important: do not commit real database passwords to a public repository. Use local configuration or environment variables for production-ready projects.

## Requirements

- Java JDK 23 or newer
- Maven
- PostgreSQL
- JavaFX compatible IDE setup

Recommended IDEs:

- IntelliJ IDEA
- Eclipse
- Visual Studio Code with Java extensions

## Installation

Clone the repository:

```bash
git clone https://github.com/your-username/exam-management-system-javafx.git
```

Go to the project directory:

```bash
cd exam-management-system-javafx
```

Install Maven dependencies:

```bash
mvn clean install
```

## Running the Application

Run the application from the main class:

```text
src/main/java/Main.java
```

You can also run the project from your IDE by opening the Maven project and starting the `Main` class.

## Maven Dependencies

The main dependencies are defined in `pom.xml`:

- `javafx-controls`
- `javafx-fxml`
- `javafx-swing`
- `postgresql`
- `controlsfx`
- `poi`
- `poi-ooxml`
- `pdfbox`
- `log4j-api`
- `log4j-core`

## Usage Overview

1. Start PostgreSQL.
2. Create and import the database.
3. Update database credentials if necessary.
4. Run the JavaFX application.
5. Log in with a user stored in the database.
6. Use the admin or coordinator panel according to the user role.
7. Manage courses, students, classrooms, and exam schedules.
8. Export generated outputs when needed.

## Security Notes

- Avoid storing real passwords directly in source code.
- Do not upload local IDE settings or database connection metadata to public repositories.
- Keep generated files such as Excel, PDF, logs, and build outputs out of version control unless they are intentionally needed.

## GitHub Upload Notes

Before pushing this project to GitHub, check that the repository does not include:

- Local IDE configuration files
- Build output folders
- Generated Excel or PDF files
- Log files
- Real database passwords
