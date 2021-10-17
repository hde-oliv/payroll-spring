package com.hdeoliv.payroll.order;

class OrderNotFoundException extends RuntimeException {

    OrderNotFoundException(Long id) {
        super("could not find order " + id);
    }
}
