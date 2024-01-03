package com.manjot.snapnote.service;

import com.manjot.snapnote.model.Note;

import java.util.List;

public interface NoteService {
    public Note createNote(Note note);

    public Note getNoteById(String noteId);

    public List<Note> getAllNotesByUser(String userName);
}
