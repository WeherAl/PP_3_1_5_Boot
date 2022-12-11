package ru.kata.spring.boot_security.demo.util;

import java.util.NoSuchElementException;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

}
