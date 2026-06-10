package com.springbootsampleray.store.entry;

//RestController, RequestMapping, PostMapping, RequestBody
import org.springframework.web.bind.annotation.*;
import java.lang.System.Logger;
import java.lang.annotation.Repeatable;
import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.springbootsampleray.store.entry.EntryService;
import com.springbootsampleray.store.entry.Entry;


/*
extract username from a security context, 
this way users can only access their own entruees
do NOT take userID from a request body

role: returns entries or a success message
*/

@RestController
@RequestMapping("/entries")
public class EntryController {
    @Autowired
    public EntryRepo ERepo;
    @Autowired
    public EntryService EService; 
    public static final Logger logger = System.getLogger(EntryController.class.getName()); 

    @PostMapping("newjournal") //creating a new entry
    public void createEntry(String title, String content) {
        //TODO: process POST request
        //FIX ME AND WORK FROM HERE
        EService.saveEntry(0, title, content);
    }

    @GetMapping("/{id}") //retrieving a past entry
    public ResponseEntity<Entry> getEntry(@PathVariable long id) //retrieve an entry obj and wrap it in a response entity
    {
        try
        {
            logger.log(System.Logger.Level.INFO, "Retrieving entry number: " + Long.toString(id)); 
            Entry thisEntry = ERepo.findById(id).orElseThrow(() -> new RuntimeException("Entry not found.")); 
            return ResponseEntity.ok(thisEntry); 
        }
        catch(Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Invalid entry ID. "); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PutMapping("/{id}") //editing a past entry
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }

    //TODO: delete mapping function
    
}
