package com.springbootsampleray.store.exceptions;

public class EntryNotFoundException extends RuntimeException
{
    public EntryNotFoundException(String msg)
    {
        super(msg); 
    }
}
