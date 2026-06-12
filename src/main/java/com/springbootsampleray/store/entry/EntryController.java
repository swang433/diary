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

    @PostMapping("/newjournal") 
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

    @GetMapping("/{entryId}")
    public ResponseEntity<EntryReadResponse> findEntryResponse(@PathVariable long entryId) 
    //retrieve an entry obj and wrap it in a response entity
    {
        String EntryIdString = Long.toString(entryId);
        try
        {
            logger.log(System.Logger.Level.INFO, "Retrieving entry number: " + EntryIdString); 
            return EService.findEntry(entryId)
            .map
            (entry -> ResponseEntity.ok
                (
                    new EntryReadResponse("Successfully retrieved entry number " + Long.toString(entry.getId()), entry.getTitle(), entry.getContent())
                )
            )
            .orElse(ResponseEntity.notFound().build());
        }
        catch(Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Invalid entry ID. "); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EntryReadResponse("unable to retrieve entry. ", null, null));
        }
    }
    
    @PutMapping("/{entryId}") //editing a past entry
    public ResponseEntity<EntryResponse> updateEntryResponse(@PathVariable long entryId
        , @RequestBody EntryRequest entryReq
        , @AuthenticationPrincipal UserDetails creds) {   
        String EntryIdString = Long.toString(entryId);
        try
        {
            logger.log(System.Logger.Level.INFO, "Retrieving entry number for editing : "
             + EntryIdString
             + " by user " + creds.getUsername() + "."); 

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
            //TODO: display the new title and content here too eventually will think of something
            return ResponseEntity.ok(new EntryResponse("Successfully retrieved and edited entry number " + EntryIdString));
        }
        catch (Exception e)
        {
            logger.log(System.Logger.Level.ERROR, "Unable to retrieve or edit entry number" + EntryIdString + "; error: " + e.getMessage()); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EntryResponse("Unable to retrieve or edit entry number " + EntryIdString)); 
        }
    }

    @DeleteMapping("/{entryId}") //deleting an existing entry
    public ResponseEntity<EntryResponse> deleteEntryResponse(@PathVariable long entryId
        , @AuthenticationPrincipal UserDetails creds)
        {
            String EntryIdString = Long.toString(entryId);
            try 
            {
                logger.log(System.Logger.Level.INFO, "Deleting entry number " + EntryIdString); 
                EService.deleteEntry(entryId);
                return ResponseEntity.ok(new EntryResponse("Successfully deleted entry number " + EntryIdString + "."));
            }
            catch (Exception e)
            {
                logger.log(System.Logger.Level.ERROR, "Unable to delete entry number " + EntryIdString + " because " + e.getMessage() + ".");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EntryResponse("Unable to delete entry number " + EntryIdString)); 
            }
        }
}