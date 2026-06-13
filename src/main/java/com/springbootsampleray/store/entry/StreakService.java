package com.springbootsampleray.store.entry;

import org.springframework.stereotype.Service;
import com.springbootsampleray.store.user.*;

@Service
public class StreakService {
    /*
    TODO:  
    hide the jwt auth mechanism and key from public
    add streaks end point to let users check their streaks
    possibly add a leaderboard endpoint further down the line
    entry operations need protection via authorization
    */
    public void updateStreak(User currUser, int newStreak)
    {
        currUser.setCurrStreak(newStreak);
    }
}
