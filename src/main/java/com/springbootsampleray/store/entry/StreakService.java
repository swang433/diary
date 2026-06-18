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
    Update current streak, last entry date, and longest streak in one place.
    This reduces races between callers and centralizes persistence-relevant logic.
    */
    public void updateStreak(User currUser, int newStreak, LocalDate date)
    {
        currUser.setCurrStreak(newStreak);
        // update longest if we've unlocked a new record
        if (newStreak > currUser.getLongestStreak()) {
            currUser.setLongestStreak(newStreak);
        }
        currUser.setLastEntryDate(date);
    }
}
