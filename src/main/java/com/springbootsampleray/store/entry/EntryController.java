package com.springbootsampleray.store.entry;

//RestController, RequestMapping, PostMapping, RequestBody
import org.springframework.web.bind.annotation.*;

import java.lang.System.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.springbootsampleray.store.auth.dto.*;;


/*
extract username from a security context, 
this way users can only access their own entruees
do NOT take userID from a request body

role: returns entries or a success message
*/

@RestController
@RequestMapping("/entries")
public class EntryController {
    private final EntryService EService; 
    public static final Logger logger = System.getLogger(EntryController.class.getName()); 

    public EntryController(EntryService eService)
    {
        this.EService = eService; 
    }

    @PostMapping("newjournal") 
    //creating a new entry
    public ResponseEntity<EntryResponse> saveEntryResponse(@RequestBody EntryRequest entryReq, 
        @AuthenticationPrincipal UserDetails creds)
    {
        try
        {
            String currUsername = creds.getUsername(); 
            logger.log(System.Logger.Level.INFO, "Entry save attempt by " + currUsername); 
            EService.saveEntry(currUsername, entryReq.getTitle(), entryReq.getContent());
            return ResponseEntity.ok(new EntryResponse("Entry creation for user " + currUsername + " successful. ")); 
        }
        catch (Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Error creating a new journal entry. ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new EntryResponse("Entry creation failed " + e.getMessage())); 
        }
    }

    @GetMapping("/{entryId}") //retrieving a past entry
    public ResponseEntity<EntryResponse> findEntryResponse(@PathVariable long entryId) 
    //retrieve an entry obj and wrap it in a response entity
    {
        try
        {
            logger.log(System.Logger.Level.INFO, "Retrieving entry number: " + Long.toString(entryId)); 
            return EService.findEntry(entryId)
            .map
            (entry -> ResponseEntity.ok
                (
                    new EntryResponse("Successfully retrieved entry number " + Long.toString(entry.getId()))
                )
            )
            .orElse(ResponseEntity.notFound().build());
        }
        catch(Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Invalid entry ID. "); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PutMapping("/{entryId}")
    public ResponseEntity<EntryResponse> updateEntryResponse(@PathVariable long entryId
        , @RequestBody EntryRequest entryReq
        , @AuthenticationPrincipal UserDetails creds) {   
        
        try
        {
            logger.log(System.Logger.Level.INFO, "Retrieving entry number for editing : " + Long.toString(entryId)); 

            //null checks for new title and or content
            String newContent = entryReq.getContent();
            String newTitle = entryReq.getTitle();  
            if (newContent != null)
            {
                EService.editContent(entryId, newContent);
            }

            if (newTitle != null)
            {
                EService.editTitle(entryId, newTitle);
            } 

            return ResponseEntity.ok(new EntryResponse("Successfully retrieved and edited entry number " + Long.toString(entryId))); 
        }
        catch (Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Unable to retrieve or edit entry number " + Long.toString(entryId)); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
    }

    //TODO: delete mapping function WORK FROM HERE
    //WORK FROM HERE
}
