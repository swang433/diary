package com.springbootsampleray.store.entry;

//RestController, RequestMapping, PostMapping, RequestBody
import org.springframework.web.bind.annotation.*;
import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.user.UserService;
import java.lang.System.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@RestController
@RequestMapping("/me")
public class HomeController {
    private final UserService UService;  
    private final EntryService EService; 
    public static final Logger logger = System.getLogger(EntryController.class.getName()); 

    //constructor injection
    public HomeController(UserService service, EntryService e_service)
    {
        this.UService = service; 
        this.EService = e_service; 
    }

    @GetMapping("/home")
    public ResponseEntity<HomeResponse>HomePageResponse(@AuthenticationPrincipal UserDetails creds) 
    {
        //retrieve user object and data
        String UName = creds.getUsername(); 
        try
        {
            logger.log(System.Logger.Level.INFO, "Displaying homepage for user " + UName + "."); 
            User CurrUser = UService.FindUserInService(UName); //catch any user not found exceptions
            int longestStreak = CurrUser.getLongestStreak(); 
            int currStreak = CurrUser.getCurrStreak(); 
            List<HomeDTO> Entries = EService.getEntryDTOs(UName);
            //return the response entity here
            return ResponseEntity.ok(new HomeResponse("Welcome, " + 
            CurrUser.getUsername() + "!", longestStreak, currStreak, Entries));
        }
        catch (Exception e)
        {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body
            (
                new HomeResponse("Unable to retrieve home page for this user."
                , 0, 0, null)
            ); 
        }
    }
}
