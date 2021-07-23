package com.example.food.exceptions;

public class OutsideHoursException extends RuntimeException {
    public OutsideHoursException(String message) {
        super(message);
    }
}
