package com.springbootsampleray.store.user;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Version;
import java.util.List;
import jakarta.persistence.CascadeType;
import com.springbootsampleray.store.entry.Entry;

import lombok.Data;


@Entity
@Table(name = "users")

@Data
public class User
{
    @Id //primary key of the user class
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 
    
    @Column(unique = true, nullable = false)
    private String username; 

    @Column(nullable = false)
    private String password; 

    private int currStreak; 
    private int longestStreak; 
    private LocalDate lastEntryDate; 

    @Version
    private Long version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Entry> entries;
}