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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")

@Getter
@Setter
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entry> entries;
}