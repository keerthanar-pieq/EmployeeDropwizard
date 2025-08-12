package Resources

import Services.EmployeeManager
import Employee
import EmployeeInput
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class EmployeeResource(private val manager: EmployeeManager = EmployeeManager()) {


    @GET
    @Path("/employees")
    fun getAll(): Response {
        val allEmployees = manager.getAllEmployees()
        return Response.ok(allEmployees).build()
    }

    @POST
    @Path("/addemployee")
    fun addEmployee(input: EmployeeInput): Response {
        val created: Employee? = manager.addEmployee(
            input.firstName,
            input.lastName,
            input.role.name,
            input.department.name,
            input.reportingTo
        )
        return if (created != null) {
            Response.status(Response.Status.CREATED)
                .entity(created)
                .build()
        } else {
            Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Failed to create employee"))
                .build()
        }
    }



    @DELETE
    @Path("/{id}")
    fun deleteById(@PathParam("id") id: String): Response {
        return if (manager.deleteEmployeeById(id)) {
            Response.ok(mapOf("message" to "models.Employee deleted for id $id")).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "No employee found with id $id"))
                .build()
        }
    }
}