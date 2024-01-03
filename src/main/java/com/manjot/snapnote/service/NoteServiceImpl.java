package com.manjot.snapnote.service;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.manjot.snapnote.exception.ErrorMessages.INVALID_ACCESS;
import static com.manjot.snapnote.exception.ErrorMessages.INVALID_NOTE;

@Service
public class NoteServiceImpl implements NoteService{
    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note createNote(@NonNull final Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteById(@NonNull final String noteId, @NonNull final String userName) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);

        if(optionalNote.isPresent()) {
            Note note = optionalNote.get();

            // Check if the authenticated user has access to the note
            if (note.getUserName().equals(userName)) {
                return note;
            } else {
                throw new SnapNoteServiceException(INVALID_ACCESS);
            }
        } else {
            throw new ResourceNotFoundException(INVALID_NOTE);
        }
    }

    @Override
    public List<Note> getAllNotes(@NonNull final String userName) {

        return noteRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No notes found for user: " + userName));
    }
}
