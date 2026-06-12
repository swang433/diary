package com.springbootsampleray.store.auth.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@JsonPropertyOrder({"message", "title", "content"})
public class EntryReadResponse {
    private String message; 
    @Nullable
    private String title; 
    @Nullable
    private String content; 

    public EntryReadResponse(String msg, String Content, String Title)
    {
        this.message = msg; 
        this.title = Title; 
        this.content = Content; 
    }
}