package com.springbootsampleray.store.exceptions;

public class AccessDeniedException extends RuntimeException
{
    public AccessDeniedException(String msg)
    {
        super(msg); 
    }
}
