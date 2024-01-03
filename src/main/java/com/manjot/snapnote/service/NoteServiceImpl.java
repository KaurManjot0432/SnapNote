package com.manjot.snapnote.service;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;
import jakarta.validation.constraints.NotNull;
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
        return noteRepository.findByIdAndUserName(noteId, userName)
                .orElseThrow(() -> new ResourceNotFoundException(INVALID_NOTE));
    }

    @Override
    public List<Note> getAllNotes(@NonNull final String userName) {

        return noteRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No notes found for user: " + userName));
    }

    @Override
    public Note updateNote(@NonNull final String id, @NonNull final String username, @NonNull final Note updatedNote) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUserName(id, username);

        if (optionalNote.isPresent()) {
            Note existingNote = optionalNote.get();
            // Update fields of existingNote with fields from updatedNote
            existingNote.setTitle(updatedNote.getTitle());
            existingNote.setContent(updatedNote.getContent());
            existingNote.setLabelList(updatedNote.getLabelList());
            // Save the updated note
            return noteRepository.save(existingNote);
        } else {
            throw new ResourceNotFoundException(INVALID_NOTE);
        }
    }

    @Override
    public void deleteNoteById(@NotNull final String id, @NotNull final String username) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUserName(id, username);

        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();

            // Delete the note
            noteRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(INVALID_NOTE);
        }
    }
}
