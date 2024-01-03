package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;

import java.util.List;

public interface NoteSearchStrategy {
    List<Note> search(NoteRepository repository, String query, String userName);
}
