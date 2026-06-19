package com.springbootsampleray.store.exceptions;

public class InvalidCredentialsException extends RuntimeException
{
    public InvalidCredentialsException(String msg)
    {
        super(msg);
    }
}
