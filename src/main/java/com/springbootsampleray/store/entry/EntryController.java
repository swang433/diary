package com.springbootsampleray.store.entry;

//RestController, RequestMapping, PostMapping, RequestBody
import org.springframework.web.bind.annotation.*;
import java.lang.System.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.springbootsampleray.store.entry.EntryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public EntryService entryService; 
    public static final Logger logger = System.getLogger(EntryController.class.getName()); 

    @PostMapping("journaling") //creating a new entry
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @GetMapping("journal") //retrieving a past entry
    public String getMethodName(@RequestParam String param) {
        //TODO: proceess get request
        return new String();
    }
    
    @PutMapping("path/{id}") //editing a past entry
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }

    //TODO: delete mapping function
    
}
