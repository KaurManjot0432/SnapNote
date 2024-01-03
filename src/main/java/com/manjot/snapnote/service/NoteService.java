package com.manjot.snapnote.service;

import com.manjot.snapnote.model.Note;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface NoteService {
    public Note createNote(@NotNull final Note note);

    public Note getNoteById(@NotNull final String noteId,@NotNull final String userName);

    public List<Note> getAllNotes(@NotNull final String userName);
}
