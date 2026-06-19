package com.springbootsampleray.store.entry;

import org.springframework.web.bind.annotation.*;

import java.lang.System.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.springbootsampleray.store.auth.dto.*;

import jakarta.annotation.Nullable;

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
    public ResponseEntity<EntryResponse> saveEntryResponse(@RequestBody EntryRequest entryReq,
        @AuthenticationPrincipal UserDetails creds)
    {
        String CurrUsername = creds.getUsername();
        logger.log(System.Logger.Level.INFO, "Entry save attempt by " + CurrUsername);
        EService.saveEntry(CurrUsername, entryReq.getTitle(), entryReq.getContent());
        return ResponseEntity.ok(new EntryResponse("Entry creation for user " + CurrUsername + " successful. "));
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<EntryReadResponse> findEntryResponse(@PathVariable long entryId, @AuthenticationPrincipal UserDetails creds)
    {
        logger.log(System.Logger.Level.INFO, "Retrieving entry number: " + entryId);
        return EService.findEntry(creds.getUsername(), entryId)
            .map(entry -> ResponseEntity.ok(
                new EntryReadResponse("Successfully retrieved entry number " + entry.getId(), entry.getTitle(), entry.getContent())
            ))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<EntryResponse> updateEntryResponse(@PathVariable long entryId,
        @RequestBody EntryRequest entryReq,
        @AuthenticationPrincipal UserDetails creds)
    {
        logger.log(System.Logger.Level.INFO, "Retrieving entry number for editing : "
            + entryId + " by user " + creds.getUsername() + ".");

        @Nullable String newContent = entryReq.getContent();
        @Nullable String newTitle = entryReq.getTitle();
        EService.edit(creds.getUsername(), entryId, newTitle, newContent);

        return ResponseEntity.ok(new EntryResponse("Successfully retrieved and edited entry number " + entryId));
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<EntryResponse> deleteEntryResponse(@PathVariable long entryId,
        @AuthenticationPrincipal UserDetails creds)
    {
        String CurrUsername = creds.getUsername();
        logger.log(System.Logger.Level.INFO, "Deleting entry number " + entryId);
        EService.deleteEntry(CurrUsername, entryId);
        return ResponseEntity.ok(new EntryResponse("Successfully deleted entry number " + entryId + "."));
    }
}
