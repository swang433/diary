package com.springbootsampleray.store.user;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")

public class User
{
    @Id //primary key of the user class
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 
    
    @Column(unique = true, nullable = false)
    private String userName; 

    @Column(nullable = false)
    private String password; 

    private int currStreak; 
    private int longestStreak; 
    private LocalDate lastEntryDate; 
}