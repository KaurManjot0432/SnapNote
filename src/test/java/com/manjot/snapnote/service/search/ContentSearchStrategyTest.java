package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContentSearchStrategyTest {

    @Test
    void search_ContentFound() {
        NoteRepository repository = mock(NoteRepository.class);
        ContentSearchStrategy strategy = new ContentSearchStrategy();
        String query = "test";
        String userName = "user";
        List<Note> expectedResult = Collections.singletonList(new Note());

        when(repository.findByContentContainingAndUserName(query, userName)).thenReturn(expectedResult);

        List<Note> result = strategy.search(repository, query, userName);

        assertEquals(expectedResult, result);

        verify(repository, times(1)).findByContentContainingAndUserName(query, userName);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void search_ContentNotFound() {
        NoteRepository repository = mock(NoteRepository.class);
        ContentSearchStrategy strategy = new ContentSearchStrategy();
        String query = "test";
        String userName = "user";

        when(repository.findByContentContainingAndUserName(query, userName)).thenReturn(Collections.emptyList());

        List<Note> result = strategy.search(repository, query, userName);

        assertEquals(Collections.emptyList(), result);

        verify(repository, times(1)).findByContentContainingAndUserName(query, userName);
        verifyNoMoreInteractions(repository);
    }
}