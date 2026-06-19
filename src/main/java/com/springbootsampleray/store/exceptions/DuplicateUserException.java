package com.springbootsampleray.store.exceptions;

public class DuplicateUserException extends RuntimeException
{
    public DuplicateUserException(String msg)
    {
        super(msg);
    }
}
