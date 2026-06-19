package com.springbootsampleray.store.entry;

import org.springframework.web.bind.annotation.*;
import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.user.UserService;
import java.lang.System.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@RestController
@RequestMapping("/me")
public class HomeController {
    private final UserService UService;
    private final EntryService EService;
    public static final Logger logger = System.getLogger(HomeController.class.getName());

    public HomeController(UserService service, EntryService e_service)
    {
        this.UService = service;
        this.EService = e_service;
    }

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> HomePageResponse(@AuthenticationPrincipal UserDetails creds)
    {
        String UName = creds.getUsername();
        logger.log(System.Logger.Level.INFO, "Displaying homepage for user " + UName + ".");
        User CurrUser = UService.FindUserInService(UName);
        int longestStreak = CurrUser.getLongestStreak();
        int currStreak = CurrUser.getCurrStreak();
        List<HomeDTO> Entries = EService.getEntryDTOs(UName);
        return ResponseEntity.ok(new HomeResponse("Welcome, " +
            CurrUser.getUsername() + "!", longestStreak, currStreak, Entries));
    }
}
