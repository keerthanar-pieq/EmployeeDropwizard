## Application.Kt:
        Registers Kotlin  with Jackson (KotlinModule).
        Initializes JDBI  for PostgreSQL and DAOs
        Registers REST resources 
               AttendanceResource
               EmployeeResource
        The main function calls run() to launch the Dropwizard application using  YAML config.
        
## EmployeeConfiguration.kt:
       Maps values from config.yml to Kotlin properties using Jackson annotations.
       Contains a nested DatabaseConfig class :
            url, user, and password required for DB connection.
            
## config.yml:
     Defines Server ports (application on 9090, admin on 8081).
     Logging level.
     Database connection details including uer , url , password.

## EmployeeManager.kt:
     Acts as a bridge between the resource layer (API) and data layer (DAOs).
     Contains logic to:
          Add/delete employees
          Check-in and check-out attendance
          Fetch working hours within a date range
          Validate employee IDs
          Fetch employees currently checked in
     This handles incoming API data and calls DAOs .
     
## EmployeeDAO.kt:
    This class handles database operations related to the employees table:
         Insert a new employee
         Fetch all employees or by ID
         Delete employee by ID
    Uses JDBI to execute SQL queries.

## AttendanceDAO.kt:
    This class contains all attendance related database queries:
         Insert check-in
         Update with check-out
         Fetch all attendance records
         Fetch active (not checked-out) attendance
         Delete attendance by employee ID
         Fetch the latest active attendance for an employee

## EmployeeResource.kt:
     This is a REST controllers for employee:
         HTTP Method	Path 
         GET	/api/v1/employees	Get all employees
         POST	/api/v1/addemployee	Add a new employee
         DELETE	/api/v1/{id}	Delete an employee by ID

## AttendanceResource.kt:
     This is a REST controllers for attendance:
         HTTP Method	Path	
         GET	/api/v1/attendance/	Get all attendance records
         POST	/api/v1/attendance/checkin/{id}	Mark an employee check-in
         DELETE	/api/v1/attendance/{id}	Delete attendance for an employee
         GET	/api/v1/attendance/checkedin	List employees currently checked-in
## Attendance.kt:
     It contains:
         Fields: employeeId, checkIn, checkOut, workingHours
         Uses @JsonFormat for date formatting in JSON.
     Override toString for readable output
## Employee.kt:
      Defines the employee object:
      Fields: firstName, lastName, role, department, reportingTo, and id
      Automatically generates employeeId using UUID 
      isValid() method checks field validity
      
 ## Department.kt:
      Defines employee departments using an enum class:
      Departments: ENGINEERING, HR, FINANCE

## Role.kt:
      Defines roles for employees:
      Roles: MANAGER, DEVELOPER, HR

## build.gradle.kts:
      This is the Gradle Kotlin DSL file for managing project dependencies and build configuration:
      Plugins: Kotlin, Application
      Main class set to ApplicationKt
      Dependencies:
          Dropwizard core
          JDBI 3 (for PostgreSQL access)
          Jackson Kotlin module for JSON mapping

