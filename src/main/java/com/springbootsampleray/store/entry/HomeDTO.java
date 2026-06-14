package com.springbootsampleray.store.entry;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HomeDTO {
    private String Title; 
    private String Content; 
    private LocalDate EntryDate;

    public HomeDTO(String MyTitle, String MyContent, LocalDate MyEntryDate)
    {
        this.Title = MyTitle; 
        this.Content = MyContent; 
        this.EntryDate = MyEntryDate; 
    }
}
