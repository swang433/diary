package com.springbootsampleray.store.exceptions;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String msg)
    {
        super(msg); //rename and invoke constructor of the parent class
    }
}
