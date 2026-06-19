package com.springbootsampleray.store.entry;

import com.springbootsampleray.store.exceptions.AccessDeniedException;
import com.springbootsampleray.store.exceptions.EntryNotFoundException;
import com.springbootsampleray.store.exceptions.UserNotFoundException;
import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.user.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
Covers the ownership-check logic in EntryService for findEntry, edit, and deleteEntry.
This is the logic that took several passes to get right in conversation -- worth
locking in with tests so future changes don't silently regress it.

Uses Mockito mocks for EntryRepo/UserRepo/StreakService rather than a Spring context,
since the goal here is testing the service's branching logic in isolation, not wiring.
*/
@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    @Mock
    private EntryRepo ERepo;

    @Mock
    private UserRepo URepo;

    @Mock
    private StreakService SService;

    private EntryService entryService;

    // two distinct users to exercise ownership mismatches
    private User owner;
    private User otherUser;
    private Entry ownedEntry;

    private static final long OWNER_ID = 1L;
    private static final long OTHER_ID = 2L;
    private static final long ENTRY_ID = 100L;
    private static final String OWNER_USERNAME = "ray";
    private static final String OTHER_USERNAME = "intruder";

    @BeforeEach
    void setUp() {
        entryService = new EntryService(URepo, ERepo, SService);

        owner = new User();
        owner.setId(OWNER_ID);
        owner.setUsername(OWNER_USERNAME);

        otherUser = new User();
        otherUser.setId(OTHER_ID);
        otherUser.setUsername(OTHER_USERNAME);

        ownedEntry = new Entry(ENTRY_ID, "My Title", "My Content", owner, LocalDate.now());
    }

    // ---------- findEntry ----------

    @Test
    void findEntry_ownerCanRetrieveTheirOwnEntry() {
        when(URepo.findByUsername(OWNER_USERNAME)).thenReturn(owner);
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));

        Optional<Entry> result = entryService.findEntry(OWNER_USERNAME, ENTRY_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(ENTRY_ID);
    }

    @Test
    void findEntry_nonOwnerIsDeniedAccess() {
        when(URepo.findByUsername(OTHER_USERNAME)).thenReturn(otherUser);
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));

        assertThatThrownBy(() -> entryService.findEntry(OTHER_USERNAME, ENTRY_ID))
            .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void findEntry_nonexistentEntryThrowsEntryNotFound() {
        when(URepo.findByUsername(OWNER_USERNAME)).thenReturn(owner);
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entryService.findEntry(OWNER_USERNAME, ENTRY_ID))
            .isInstanceOf(EntryNotFoundException.class);
    }

    @Test
    void findEntry_unknownRequesterThrowsUserNotFound() {
        when(URepo.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> entryService.findEntry("ghost", ENTRY_ID))
            .isInstanceOf(UserNotFoundException.class);
    }

    // ---------- edit ----------

    @Test
    void edit_ownerCanEditTheirOwnEntry() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername(OWNER_USERNAME)).thenReturn(owner);

        entryService.edit(OWNER_USERNAME, ENTRY_ID, "New Title", "New Content");

        assertThat(ownedEntry.getTitle()).isEqualTo("New Title");
        assertThat(ownedEntry.getContent()).isEqualTo("New Content");
        verify(ERepo).save(ownedEntry);
    }

    @Test
    void edit_nonOwnerIsDeniedAndEntryIsUnchanged() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername(OTHER_USERNAME)).thenReturn(otherUser);

        assertThatThrownBy(() -> entryService.edit(OTHER_USERNAME, ENTRY_ID, "Hacked Title", "Hacked Content"))
            .isInstanceOf(AccessDeniedException.class);

        // confirm the entry was never mutated or saved despite the attempt
        assertThat(ownedEntry.getTitle()).isEqualTo("My Title");
        assertThat(ownedEntry.getContent()).isEqualTo("My Content");
        verify(ERepo, never()).save(any());
    }

    @Test
    void edit_nonexistentEntryThrowsEntryNotFound() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entryService.edit(OWNER_USERNAME, ENTRY_ID, "Title", "Content"))
            .isInstanceOf(EntryNotFoundException.class);

        verify(ERepo, never()).save(any());
    }

    @Test
    void edit_unknownRequesterThrowsUserNotFound() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> entryService.edit("ghost", ENTRY_ID, "Title", "Content"))
            .isInstanceOf(UserNotFoundException.class);

        verify(ERepo, never()).save(any());
    }

    @Test
    void edit_nullTitleAndContentLeaveExistingValuesUntouched() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername(OWNER_USERNAME)).thenReturn(owner);

        entryService.edit(OWNER_USERNAME, ENTRY_ID, null, null);

        // null means "no change" per EntryController's null-check contract
        assertThat(ownedEntry.getTitle()).isEqualTo("My Title");
        assertThat(ownedEntry.getContent()).isEqualTo("My Content");
        verify(ERepo).save(ownedEntry);
    }

    // ---------- deleteEntry ----------

    @Test
    void deleteEntry_ownerCanDeleteTheirOwnEntry() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername(OWNER_USERNAME)).thenReturn(owner);

        entryService.deleteEntry(OWNER_USERNAME, ENTRY_ID);

        verify(ERepo).deleteById(ENTRY_ID);
    }

    @Test
    void deleteEntry_nonOwnerIsDeniedAndNothingIsDeleted() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername(OTHER_USERNAME)).thenReturn(otherUser);

        assertThatThrownBy(() -> entryService.deleteEntry(OTHER_USERNAME, ENTRY_ID))
            .isInstanceOf(AccessDeniedException.class);

        verify(ERepo, never()).deleteById(any());
    }

    @Test
    void deleteEntry_nonexistentEntryThrowsEntryNotFound() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entryService.deleteEntry(OWNER_USERNAME, ENTRY_ID))
            .isInstanceOf(EntryNotFoundException.class);

        verify(ERepo, never()).deleteById(any());
    }

    @Test
    void deleteEntry_unknownRequesterThrowsUserNotFound() {
        when(ERepo.findById(ENTRY_ID)).thenReturn(Optional.of(ownedEntry));
        when(URepo.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> entryService.deleteEntry("ghost", ENTRY_ID))
            .isInstanceOf(UserNotFoundException.class);

        verify(ERepo, never()).deleteById(any());
    }
}