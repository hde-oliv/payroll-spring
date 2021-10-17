package com.hdeoliv.payroll.employee;

class EmployeeNotFoundException extends RuntimeException {
    
    EmployeeNotFoundException(Long id) {
        super("could not find employee " + id);
    }
}
