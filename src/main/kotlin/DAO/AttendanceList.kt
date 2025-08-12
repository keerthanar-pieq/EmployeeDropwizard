package DAO

import Attendance
import java.time.LocalDate

class AttendanceList : ArrayList<Attendance>() {


    fun addCheckIn(attendance: Attendance): Boolean {
        if (hasAlreadyCheckedIn(attendance.employeeId, attendance.checkIn.toLocalDate())) {
            return false
        }
        this.add(attendance)
        return true
    }

    fun hasAlreadyCheckedIn(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId && it.checkIn.toLocalDate() == date
        }
    }

    fun delete(employeeId: String): Boolean {
        return this.removeIf { it.employeeId == employeeId }
    }

    fun getAll(): List<Attendance> = this.toList()

    fun findAttendanceById(employeeId: String): Attendance? {
        return this.find {
            it.employeeId == employeeId && it.checkOut == null
        }
    }

    override fun toString(): String {
        return if (this.isEmpty()) {
            "No attendance records found."
        } else {
            this.joinToString("\n") { it.toString() }
        }
    }
}