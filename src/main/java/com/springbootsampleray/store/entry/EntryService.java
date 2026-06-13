package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.UserRepo;
import com.springbootsampleray.store.user.User;

import java.time.temporal.ChronoUnit;

@Service
public class EntryService 
{   
    private final EntryRepo ERepo;
    private final UserRepo URepo; 
    private final StreakService SService; 

    // constructor injection; can now call URepo throughout this class
    public EntryService(UserRepo URepo, EntryRepo ERepo, StreakService SService)
    {
        this.URepo = URepo;
        this.ERepo = ERepo;  
        this.SService = SService; 
    }

    public void saveEntry(String username, String title, String content)
    {
        User currUser = URepo.findByUsername(username);
        if (currUser == null)
        {   
            throw new RuntimeException("User not found. ");
        }
        //maps to ID = 0 since the DB generates it
        LocalDate DateNow = LocalDate.now();
        Entry new_entry = new Entry(0, title, content, currUser, DateNow);  

        //first entry ever of current user
        if (currUser.getLastEntryDate() == null)
        {
            SService.updateStreak(currUser, 1);
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) == 1)
        {
            //TODO: update streaks++ here via streak service
            SService.updateStreak(currUser, currUser.getCurrStreak() + 1 );
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) > 1)
        {
            //reset streak to 1 here
            SService.updateStreak(currUser, 1);
        }

        if (currUser.getCurrStreak() > currUser.getLongestStreak())
        {
            currUser.setLongestStreak(currUser.getCurrStreak());
        }

        ERepo.save(new_entry); 
        URepo.save(currUser); 
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

    //TODO: minor optimization: the two edit functions both hit the DB
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
