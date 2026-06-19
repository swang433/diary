package com.springbootsampleray.store.entry;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.UserRepo;
import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.exceptions.*;

import java.time.temporal.ChronoUnit;
import org.springframework.transaction.annotation.Transactional;


//the connector between the controller and the repo layers

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

    @Transactional
    public void saveEntry(String username, String title, String content)
    {
        User currUser = URepo.findByUsername(username);
        if (currUser == null)
        {   
            throw new UserNotFoundException("User not found. ");
        }
        //maps to ID = 0 since the DB generates it
        LocalDate DateNow = LocalDate.now();
        Entry new_entry = new Entry(0, title, content, currUser, DateNow);  

        //first entry ever of current user
        if (currUser.getLastEntryDate() == null)
        {
            SService.updateStreak(currUser, 1, DateNow);
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) == 1)
        {
            SService.updateStreak(currUser, currUser.getCurrStreak() + 1, DateNow);
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) > 1)
        {
            //reset streak to 1 here
            SService.updateStreak(currUser, 1, DateNow);
        }
        else if (ChronoUnit.DAYS.between(currUser.getLastEntryDate(), DateNow) == 0)
        {
            SService.updateStreak(currUser, currUser.getCurrStreak(), DateNow);
        }

        ERepo.save(new_entry); 
        URepo.save(currUser); 
    }

    public Optional<Entry> findEntry(long entryId)
    {
        return ERepo.findById(entryId); 
    }

    public void edit(String reqUsername, long entryId, String newTitle, String newContent)
    {   
        Entry thisEntry = ERepo.findById(entryId)
        .orElseThrow(() -> new RuntimeException("Entry not found. ")); 
        User ReqUser = URepo.findByUsername(reqUsername); 

        //check for primary key mismatch
        if (thisEntry.getUser().getId() != ReqUser.getId())
        {
            throw new AccessDeniedException("Not your entry. "); 
        }

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

    public void deleteEntry(String reqUsername, long entryId)
    {
        Entry thisEntry = ERepo.findById(entryId)
        .orElseThrow(() -> new RuntimeException("Entry not found. ")); 
        User ReqUser = URepo.findByUsername(reqUsername);
        
        if (ReqUser == null)
        {
            throw new UserNotFoundException("Unable to retrieve user. "); 
        }

        //check for primary key mismatch
        if (thisEntry.getUser().getId() != ReqUser.getId())
        {
            throw new AccessDeniedException("Not your entry. "); 
        }

        if (!ERepo.existsById(entryId))
        {
            throw new EntryNotFoundException("Entry not found"); 
        }
        ERepo.deleteById(entryId);
    } 
    
    public List<HomeDTO> getEntryDTOs(String Username)
    {    
        User CurrUser = URepo.findByUsername(Username); 
        if (CurrUser == null)
        {
            throw new UserNotFoundException("Unable to retrieve entries for this user because this user might not exist. "); 
        }

        List<Entry> Entries = URepo.findByUsername(Username).getEntries();
        return Entries.stream()
        .map(entry -> new HomeDTO(entry.getTitle(), entry.getContent(), entry.getDate()))
        .collect(Collectors.toList());
    } 
}
