package Resources

import AttendanceInput
import Services.EmployeeManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PathParam
import javax.ws.rs.DELETE
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

@Path("/api/v1/attendance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AttendanceResource(private val manager: EmployeeManager)
 {

    @GET
    @Path("/")
    fun getAll(): Response {
        val all = manager.getAllAttendance().map { attendance ->
            mapOf(
                "employeeId" to attendance.employeeId,
                "checkIn" to attendance.checkIn.format(formatter),
                "checkOut" to attendance.checkOut?.format(formatter),
                "workingHours" to attendance.workingHours?.let {
                    mapOf("hours" to it.first, "minutes" to it.second)
                }
            )
        }
        return Response.ok(all).build()
    }

     @POST
     @Path("/checkin/{employeeId}")
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     fun checkIn(
         @PathParam("employeeId") employeeId: String,
         attendanceInput: AttendanceInput
     ): Response {
         return try {

             if (!manager.isValidEmployeeId(employeeId)) {
                 return Response.status(Response.Status.BAD_REQUEST)
                     .entity(mapOf("error" to "Invalid employeeId: $employeeId")).build()
             }


             val checkInTime = attendanceInput.checkIn ?: LocalDateTime.now()
             val success = manager.checkIn(employeeId, checkInTime)

             if (success) {
                 Response.status(Response.Status.CREATED)
                     .entity(
                         mapOf(
                             "employeeId" to employeeId,
                             "checkIn" to checkInTime.format(formatter)
                         )
                     )
                     .build()
             } else {
                 Response.status(Response.Status.BAD_REQUEST)
                     .entity(mapOf("error" to "Check-in failed for employeeId $employeeId"))
                     .build()
             }
         } catch (e: Exception) {
             e.printStackTrace()
             Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity(mapOf("error" to "Server error: ${e.message}"))
                 .build()
         }
     }

     @POST
     @Path("/checkout/{employeeId}")
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     fun checkOut(
         @PathParam("employeeId") employeeId: String,
         attendanceInput: AttendanceInput
     ): Response {
         return try {
             if (!manager.isValidEmployeeId(employeeId)) {
                 return Response.status(Response.Status.BAD_REQUEST)
                     .entity(mapOf("error" to "Invalid employeeId: $employeeId")).build()
             }

             val checkOutTime = attendanceInput.checkOut ?: LocalDateTime.now()
             val result = manager.checkOut(employeeId, checkOutTime)

             if (result.first) {
                 val (hours, minutes) = result.second ?: Pair(0, 0)
                 Response.status(Response.Status.OK)
                     .entity(
                         mapOf(
                             "employeeId" to employeeId,
                             "checkOut" to checkOutTime.format(formatter),
                             "hours" to hours,
                             "minutes" to minutes
                         )
                     )
                     .build()
             } else {
                 Response.status(Response.Status.BAD_REQUEST)
                     .entity(mapOf("error" to "Check-out failed for employeeId $employeeId"))
                     .build()
             }
         } catch (e: Exception) {
             e.printStackTrace()
             Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity(mapOf("error" to "Server error: ${e.message}"))
                 .build()
         }
     }


     @DELETE
    @Path("/{employeeId}")
    fun deleteAttendance(@PathParam("employeeId") employeeId: String): Response {
        return if (manager.deleteAttendanceById(employeeId)) {
            Response.ok(mapOf("message" to "models.Attendance deleted for $employeeId")).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "No attendance found for employeeId $employeeId")).build()
        }
    }

    @GET
    @Path("/checkedin")
    fun getCheckedInEmployees(): Response {
        return try {
            val employees = manager.getCheckedInEmployees()

            val result = employees.map { employee ->
                mapOf(
                    "employeeId" to employee.id,
                    "name" to "${employee.firstName} ${employee.lastName}",
                    "department" to employee.department.name,
                    "role" to employee.role.name
                )
            }

            Response.ok(result).build()
        } catch (e: Exception) {
            e.printStackTrace()
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to "Server error: ${e.message}")).build()
        }
    }


    @GET
    @Path("/workinghours")
    fun getWorkingHoursBetweenDates(
        @QueryParam("from") from: String?,
        @QueryParam("to") to: String?
    ): Response {
        return try {
            if (from.isNullOrBlank() || to.isNullOrBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mapOf("error" to "Both 'from' and 'to' dates are required in format yyyy-MM-dd"))
                    .build()
            }

            val fromDate = LocalDate.parse(from)
            val toDate = LocalDate.parse(to)

            val result = manager.getWorkingHoursBetweenDates(fromDate, toDate).map { (employee, hours) ->
                mapOf(
                    "employeeId" to employee.id,
                    "name" to "${employee.firstName} ${employee.lastName}",
                    "department" to employee.department.name,
                    "role" to employee.role.name,
                    "hoursWorked" to "${hours.first}h ${hours.second}m"
                )
            }

            Response.ok(result).build()
        } catch (e: Exception) {
            e.printStackTrace()
            Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid date format. Use yyyy-MM-dd")).build()
        }
    }
}
