import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

 class Attendance(
    val employeeId: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var checkIn: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var checkOut: LocalDateTime? = null
) {
    var workingHours: Pair<Int, Int>? = null


    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun checkOut(providedCheckOut: LocalDateTime): Boolean {
        if (providedCheckOut.isBefore(checkIn)) return false
        checkOut = providedCheckOut
        val duration = Duration.between(checkIn, checkOut)
        workingHours = Pair(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())
        return true
    }

    override fun toString(): String {
        val checkInStr = checkIn.format(formatter)
        val checkOutStr = checkOut?.format(formatter) ?: "N/A"
        val hoursStr = workingHours?.let { "${it.first} hrs ${it.second} mins" } ?: "N/A"
        return "models.Employee Id=$employeeId, CheckIn=$checkInStr, CheckOut=$checkOutStr, WorkingHours=$hoursStr"
    }
}