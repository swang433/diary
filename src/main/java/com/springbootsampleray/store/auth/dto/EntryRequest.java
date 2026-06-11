package com.springbootsampleray.store.auth.dto;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class EntryRequest {
    @Nullable
    String title;
    @Nullable 
    String content; 
}
