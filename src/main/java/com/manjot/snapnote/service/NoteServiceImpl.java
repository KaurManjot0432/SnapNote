package com.manjot.snapnote.service;

import com.manjot.snapnote.exception.NoteNotFoundException;
import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService{
    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note createNote(@NonNull Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteById(String noteId) {

        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + noteId + " not found"));
    }

    @Override
    public List<Note> getAllNotesByUser(String userName) {

        return noteRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No notes found for user: " + userName));
    }
}
