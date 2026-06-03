package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.List; 
import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.UserRepo;
import com.springbootsampleray.store.user.User;

@Service
public class EntryService 
{   
    private final EntryRepo entry_repository;
    private final UserRepo user_repository; 

    // constructor injection; can now call user_repository throughout this class
    public EntryService(UserRepo URepo, EntryRepo ERepo)
    {
        this.user_repository = URepo;
        this.entry_repository = ERepo;  
    }

    public void saveEntry(long userId, String title, String content)
    {
        User currUser = user_repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Entry new_entry = new Entry(userId, title, content, currUser, LocalDate.now()); 
        entry_repository.save(new_entry); //saves to db here
    }

    public List<Entry> updateEntries(long userID)
    {
        User currUser = user_repository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        return currUser.getEntries(); 
    }
}
