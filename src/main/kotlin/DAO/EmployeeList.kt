package DAO

import Employee

class EmployeeList : ArrayList<Employee>() {

    fun addEmployee(emp: Employee): Boolean {
        if (!emp.isValid()) return false


        if (this.any { it.firstName == emp.firstName && it.lastName == emp.lastName }) {
            return false
        }

        this.add(emp)
        return true
    }

    fun deleteEmployeeById(id: String): Boolean {
        return this.removeIf { it.id == id }
    }

    fun getEmployeeById(id: String): Employee? {
        return this.find { it.id == id }
    }

    fun getAllEmployees(): List<Employee> {
        return this.toList()
    }



    fun employeeExists(id: String): Boolean {
        return this.any { it.id == id }
    }

    override fun toString(): String {
        return this.joinToString(separator = "\n") { it.toString() }
    }
}