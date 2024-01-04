package com.manjot.snapnote.service.note;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.model.enums.QueryType;

import java.util.List;

public interface NoteService {
    public Note createNote(Note note);

    public Note getNoteById(String noteId, String userName);

    public List<Note> getAllNotes(String userName);

    public Note updateNote(String id, String username, Note updatedNote);

    public void deleteNoteById(String id, String username);
    public void shareNoteWithUser(String noteId, String senderUsername, String recipientUsername);

    public List<Note> searchNotes(String query, QueryType queryType, String userName);
}
