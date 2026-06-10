package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.List; 
import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.UserRepo;
import com.springbootsampleray.store.user.User;

@Service
public class EntryService //TODO: possibly need to throw exception here for the controller to catch it
{   
    private final EntryRepo entry_repository;
    private final UserRepo user_repository; 

    // constructor injection; can now call user_repository throughout this class
    public EntryService(UserRepo URepo, EntryRepo ERepo)
    {
        this.user_repository = URepo;
        this.entry_repository = ERepo;  
    }

    public void saveEntry(long userID, String title, String content)
    {
        User currUser = user_repository.findById(userID).orElseThrow(() -> 
        new RuntimeException("User not found"));
        //maps to ID = 0 since the DB generates it
        Entry new_entry = new Entry(0, title, content, currUser, LocalDate.now());  
        //saves to db here
        entry_repository.save(new_entry); 
    }

    public List<Entry> getEntriesByUser(long userID)
    {
        User currUser = user_repository.findById(userID).orElseThrow(() -> 
        new RuntimeException("User not found"));
        return currUser.getEntries(); 
    }
}
