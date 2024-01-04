package com.manjot.snapnote.service.search;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultSearchStrategyTest {

    @Test
    void search_NotesFound() {
        NoteRepository repository = mock(NoteRepository.class);
        DefaultSearchStrategy strategy = new DefaultSearchStrategy();
        String userName = "user";
        List<Note> expectedResult = Collections.singletonList(new Note());

        when(repository.findByUserName(userName)).thenReturn(Optional.of(expectedResult));

        List<Note> result = strategy.search(repository, "", userName);

        assertEquals(expectedResult, result);
        verify(repository, times(1)).findByUserName(userName);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void search_NotesNotFound() {
        NoteRepository repository = mock(NoteRepository.class);
        DefaultSearchStrategy strategy = new DefaultSearchStrategy();
        String userName = "user";

        when(repository.findByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> strategy.search(repository, "", userName));

        verify(repository, times(1)).findByUserName(userName);
        verifyNoMoreInteractions(repository);
    }
}