# Project Structure Overview

##  Application.kt
- Registers Kotlin with Jackson.
- Initializes JDBI for PostgreSQL and sets up DAOs.
- Registers REST resources:
  - `AttendanceResource`
  - `EmployeeResource`
- The `main` function calls `run()` to launch the Dropwizard application using the YAML config.

---

##  EmployeeConfiguration.kt
- Maps values from `config.yml` to Kotlin properties using Jackson annotations.
- Contains a nested `DatabaseConfig` class with:
  - `url`, `user`, and `password` – required for DB connection.

---

##  config.yml
- Defines server ports:
  - Application: `9090`
  - Admin: `8081`
- Sets logging levels.
- Configures database connection:
  - `user`, `password`, and `url` (with timezone settings).

---

##  EmployeeManager.kt
Acts as a bridge between the **resource layer (REST API)** and the **data layer (DAOs)**.

Contains business logic to:
- Add/delete employees.
- Check-in and check-out attendance.
- Fetch working hours between date ranges.
- Validate employee IDs.
- Fetch currently checked-in employees.

---

##  EmployeeDAO.kt
Handles database operations for the `employees` table.

Functions include:
- Inserting new employees.
- Fetching all employees or a specific one by ID.
- Deleting an employee by ID.

Uses **JDBI** for SQL operations.

---

##  AttendanceDAO.kt
Handles all attendance-related database operations.

Functions include:
- Inserting check-ins.
- Updating records with check-outs.
- Fetching all attendance records.
- Finding active (not yet checked-out) records.
- Deleting attendance by employee ID.
- Finding latest active attendance for an employee.
- Fetching records between date ranges for reporting.

---

## 🌐 EmployeeResource.kt
REST controller for **Employee APIs**.

| HTTP Method | Path                      | Description              |
|-------------|---------------------------|--------------------------|
| `GET`       | `/api/v1/employees`       | Get all employees        |
| `POST`      | `/api/v1/addemployee`     | Add a new employee       |
| `DELETE`    | `/api/v1/{id}`            | Delete employee by ID    |

---

## 🌐 AttendanceResource.kt
REST controller for **Attendance APIs**.

| HTTP Method | Path                                     | Description                         |
|-------------|------------------------------------------|-------------------------------------|
| `GET`       | `/api/v1/attendance/`                    | Get all attendance records          |
| `POST`      | `/api/v1/attendance/checkin/{id}`        | Mark check-in for an employee       |
| `POST`      | `/api/v1/attendance/checkout/{id}`       | Mark check-out for an employee      |
| `DELETE`    | `/api/v1/attendance/{id}`                | Delete attendance for an employee   |
| `GET`       | `/api/v1/attendance/checkedin`           | List employees currently checked-in |
| `GET`       | `/api/v1/attendance/workinghours?from=...&to=...` | Get working hours for employees in a date range |

---

##  Attendance.kt
Model class representing attendance data.

- Fields:
  - `employeeId`
  - `checkIn`
  - `checkOut`
  - `workingHours` (calculated as hours and minutes)
- Uses `@JsonFormat` for date-time formatting in JSON.
- Overrides `toString()` for readable logging output.
- Includes logic to calculate working hours between check-in and check-out.

---

##  Employee.kt
Model class representing an employee.

- Fields:
  - `firstName`, `lastName`, `role`, `department`, `reportingTo`, and `id`
- Automatically generates `employeeId` using UUID.
- Includes `isValid()` method to validate employee fields.

---

##  Department.kt
Defines available departments using Kotlin enum:

- `ENGINEERING`
- `HR`
- `FINANCE`

Includes utility function `fromId(id: Int)` for mapping from DB values.

---

##  Role.kt
Defines employee roles using enum:

- `MANAGER`
- `DEVELOPER`
- `HR`

Includes utility function `fromId(id: Int)` to map from DB values.

---

##  build.gradle.kts
Kotlin DSL build file for Gradle.

- **Plugins**: Kotlin, Application
- **Main Class**: `ApplicationKt`
- **Dependencies**:
  - Dropwizard Core (`io.dropwizard`)
  - JDBI 3 (PostgreSQL support)
  - PostgreSQL JDBC Driver
  - Jackson Kotlin Module (JSON serialization)
  - Jackson JSR310 Module (Java time support)
- **Run Config**:
  - Uses the command:  
    ```bash
    ./gradlew run
    ```
    with arguments:  
    ```
    server src/main/resources/config.yml
    ```

---

