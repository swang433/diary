package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import com.springbootsampleray.store.user.User;


@Entity
@Table(name = "entries")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long Id; 


    @Getter
    @Setter
    @Column(columnDefinition =  "TEXT")
    private String title; 

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String content; 

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate Date; 

    //protected empty constructor for loading form DB
    protected Entry() {}

    public Entry(long entryID, String entryTitle, String entryContent, User entryUser, LocalDate entryDate)
    {
        this.Id = entryID; 
        this.title = entryTitle; 
        this.content = entryContent; 
        this.user = entryUser; 
        this.Date = entryDate; 
    }
}
