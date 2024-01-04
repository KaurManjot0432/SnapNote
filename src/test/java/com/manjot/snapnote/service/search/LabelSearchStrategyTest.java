package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class LabelSearchStrategyTest {
    @Test
    void search_LabelFound() {
        NoteRepository repository = mock(NoteRepository.class);
        LabelSearchStrategy strategy = new LabelSearchStrategy();
        String query = "test";
        String userName = "user";
        List<Note> expectedResult = Collections.singletonList(new Note());

        when(repository.findByLabelListInAndUserName(List.of(query), userName)).thenReturn(expectedResult);
        List<Note> result = strategy.search(repository, query, userName);

        assertEquals(expectedResult, result);
        verify(repository, times(1)).findByLabelListInAndUserName(List.of(query), userName);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void search_LabelNotFound() {
        NoteRepository repository = mock(NoteRepository.class);
        LabelSearchStrategy strategy = new LabelSearchStrategy();
        String query = "test";
        String userName = "user";

        when(repository.findByLabelListInAndUserName(List.of(query), userName)).thenReturn(Collections.emptyList());

        List<Note> result = strategy.search(repository, query, userName);

        assertEquals(Collections.emptyList(), result);
        verify(repository, times(1)).findByLabelListInAndUserName(List.of(query), userName);
        verifyNoMoreInteractions(repository);
    }
}
