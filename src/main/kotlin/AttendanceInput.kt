import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AttendanceInput(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val checkIn: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val checkOut: LocalDateTime? = null
)