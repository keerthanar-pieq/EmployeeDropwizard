import Department
import Role

data class EmployeeInput(
    val firstName: String,
    val lastName: String,
    val role: Role,
    val department: Department,
    val reportingTo: String? = null
)