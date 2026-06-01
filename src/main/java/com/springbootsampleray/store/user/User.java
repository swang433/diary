package com.springbootsampleray.store.user;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.List;
import jakarta.persistence.CascadeType;
import com.springbootsampleray.store.entry.Entry;


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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entry> entries;

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getCurrStreak() {
        return currStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public LocalDate getLastEntryDate() {
        return lastEntryDate;
    }
}