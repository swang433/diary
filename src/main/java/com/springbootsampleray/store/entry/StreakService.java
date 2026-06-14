package com.springbootsampleray.store.entry;

import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.*;
import java.time.LocalDate;

@Service
public class StreakService {
    /*
    TODO:  
    hide the jwt auth mechanism and key from public
    add streaks end point to let users check their streaks
    possibly add a leaderboard endpoint further down the line
    entry operations need protection via authorization
    */

    /*
    updating current streaks only here
    longest streaks are only changed in the controller layer
    */ 
    public void updateStreak(User currUser, int newStreak)
    {
        currUser.setCurrStreak(newStreak);
        currUser.setLastEntryDate(LocalDate.now());
    }
}
