package com.springbootsampleray.store.entry;
import lombok.Data;

@Data
public class EntryResponse {
    private String message; 

    public EntryResponse(String msg)
    {
        this.message = msg; 
    }
}
