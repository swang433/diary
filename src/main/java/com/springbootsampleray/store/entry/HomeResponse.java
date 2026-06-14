package com.springbootsampleray.store.entry;

import lombok.Data;
import java.util.List;

import jakarta.annotation.Nullable;; 

@Data
public class HomeResponse {
    private String ResponseMsg; 
    private int longestStreak; 
    private int currentStreak;
    @Nullable
    private List<HomeDTO> EntryList;  

    public HomeResponse(String Msg, int longStreak, int currStreak, List<HomeDTO> Entries)
    {   
        this.ResponseMsg = Msg; 
        this.longestStreak = longStreak; 
        this.currentStreak = currStreak; 
        this.EntryList = Entries; 
    }
}
