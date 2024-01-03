package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;

import java.util.List;

public class ContentSearchStrategy implements NoteSearchStrategy {
    @Override
    public List<Note> search(NoteRepository repository, String query, String userName) {
        return repository.findByContentContainingAndUserName(query, userName);
    }
}
