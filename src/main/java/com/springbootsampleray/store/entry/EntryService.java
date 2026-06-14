package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

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
        int currStreak = currUser.getCurrStreak(); 

        //first entry ever of current user
        if (currUser.getLastEntryDate() == null)
        {
            SService.updateStreak(currUser, 1);
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) == 1)
        {
            SService.updateStreak(currUser, currStreak + 1 );
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) > 1)
        {
            //reset streak to 1 here
            SService.updateStreak(currUser, 1);
        }
        else
        {
            SService.updateStreak(currUser, currStreak); //needs to update last entry date as well 
        }

        if (currStreak > currUser.getLongestStreak())
        {
            currUser.setLongestStreak(currStreak);
        }

        ERepo.save(new_entry); 
        URepo.save(currUser); 
    }

    public Optional<Entry> findEntry(long entryId)
    {
        return ERepo.findById(entryId); 
    }

    public void edit(long entryId, String newTitle, String newContent)
    {
        Entry thisEntry = ERepo.findById(entryId)
        .orElseThrow(() -> new RuntimeException("Entry not found. ")); 

        if (newTitle != null)
        {
            thisEntry.setTitle(newTitle);    
        }
        if (newContent != null)
        {   
            thisEntry.setContent(newContent);
        } 
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
    
    //TODO: possibly add fetch mechanism here
    public List<HomeDTO> getEntryDTOs(String Username)
    {    
        List<Entry> Entries = URepo.findByUsername(Username).getEntries();
        return Entries.stream()
        .map(entry -> new HomeDTO(entry.getTitle(), entry.getContent(), entry.getDate()))
        .collect(Collectors.toList());
    } 
}
