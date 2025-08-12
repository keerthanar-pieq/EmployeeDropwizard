
import Config.EmployeeConfiguration
import Services.EmployeeManager
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import com.fasterxml.jackson.module.kotlin.KotlinModule
import Resources.AttendanceResource
import Resources.EmployeeResource

class EmployeeApplication : Application<EmployeeConfiguration>() {

    override fun getName(): String = "EmployeeDropwizard"

    override fun initialize(bootstrap: Bootstrap<EmployeeConfiguration>) {
        bootstrap.objectMapper.registerModule(KotlinModule.Builder().build())
    }

    override fun run(config: EmployeeConfiguration, environment: Environment) {
        val resource = EmployeeManager()
        environment.jersey().register(AttendanceResource(resource))
        environment.jersey().register(EmployeeResource(resource))


    }

}

fun main(args: Array<String>) {
    EmployeeApplication().run(*args)
}
