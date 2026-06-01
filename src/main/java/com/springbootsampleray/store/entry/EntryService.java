package com.springbootsampleray.store.entry;
import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.user.UserRepo;

public class EntryService {
    public void createEntry(String content, long userId) {
        Entry entry = new Entry();
        EntryRepo.save(entry);
    }
}
