package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.UserRepo;
import com.springbootsampleray.store.user.User;

@Service
public class EntryService //TODO: possibly need to throw exception here for the controller to catch it
{   
    private final EntryRepo ERepo;
    private final UserRepo URepo; 

    // constructor injection; can now call URepo throughout this class
    public EntryService(UserRepo URepo, EntryRepo ERepo)
    {
        this.URepo = URepo;
        this.ERepo = ERepo;  
    }

    public void saveEntry(String username, String title, String content)
    //TODO: needs to update last entry date for streak re-calculation
    {
        User currUser = URepo.findByUsername(username);
        if (currUser == null)
        {   
            throw new RuntimeException("User not found. ");
        }
        //maps to ID = 0 since the DB generates it
        Entry new_entry = new Entry(0, title, content, currUser, LocalDate.now());  
        //saves to db here
        ERepo.save(new_entry); 
    }

    public List<Entry> getEntriesByUser(long userID)
    {
        User currUser = URepo.findById(userID).orElseThrow(() -> 
        new RuntimeException("User not found"));
        return currUser.getEntries(); 
    }

    public Optional<Entry> findEntry(long entryId)
    {
        return ERepo.findById(entryId); 
    }

    //TODO: the two edit functions both hit the DB, needs optimizing eventually
    public void editContent(long entryId, String newContent)
    {
        Entry thisEntry = ERepo.findById(entryId)
        .orElseThrow(() -> new RuntimeException("Entry not found. ")); 
        thisEntry.setContent(newContent);
        ERepo.save(thisEntry); 
    }

    public void editTitle(long entryId, String newTitle)
    {
        Entry thisEntry = ERepo.findById(entryId)
        .orElseThrow(() -> new RuntimeException("Entry not found. ")); 
        thisEntry.setTitle(newTitle);
        ERepo.save(thisEntry); 
    }

    public void deleteEntry(long entryId)
    {
        if (!ERepo.existsById(entryId))
        {
            throw new RuntimeException("Entry not found"); 
        }
        ERepo.deleteById(entryId);
    }
}
