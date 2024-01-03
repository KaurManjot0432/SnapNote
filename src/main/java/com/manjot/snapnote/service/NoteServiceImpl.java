package com.manjot.snapnote.service;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.model.User;
import com.manjot.snapnote.model.enums.QueryType;
import com.manjot.snapnote.repository.NoteRepository;
import com.manjot.snapnote.repository.UserRepository;
import com.manjot.snapnote.service.search.NoteSearchStrategy;
import com.manjot.snapnote.service.search.NoteSearchStrategyFactory;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.manjot.snapnote.exception.ErrorMessages.*;

@Service
public class NoteServiceImpl implements NoteService{
    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository,
                           UserRepository userRepository
    ) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Note createNote(@NonNull final Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteById(@NonNull final String noteId,
                            @NonNull final String userName) {
        return noteRepository.findByIdAndUserName(noteId, userName)
                .orElseThrow(() -> new ResourceNotFoundException(INVALID_NOTE));
    }

    @Override
    public List<Note> getAllNotes(@NonNull final String userName) {

        return noteRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No notes found for user: " + userName));
    }

    @Override
    public Note updateNote(@NonNull final String id,
                           @NonNull final String username,
                           @NonNull final Note updatedNote) {
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
    public void deleteNoteById(@NotNull final String id,
                               @NotNull final String username) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUserName(id, username);

        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();

            // Delete the note
            noteRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(INVALID_NOTE);
        }
    }

    @Override
    public void shareNoteWithUser(@NotNull final String noteId,
                                  @NotNull final String senderUsername,
                                  @NotNull final String recipientUsername) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUserName(noteId, senderUsername);
        Optional<User> optionalUser = userRepository.findByUsername(recipientUsername);

        if(optionalNote.isEmpty()) throw new ResourceNotFoundException(INVALID_NOTE);
        if(optionalUser.isEmpty()) throw new ResourceNotFoundException(INVALID_USER);

        Note existingNote = optionalNote.get();

        Note sharedNote = new Note();
        sharedNote.setTitle(existingNote.getTitle());
        sharedNote.setContent(existingNote.getContent());
        sharedNote.setLabelList(existingNote.getLabelList());
        sharedNote.setUserName(recipientUsername);

        noteRepository.save(sharedNote);
    }

    public List<Note> searchNotes(@NotNull final String query,
                                  @NotNull final QueryType queryType,
                                  @NotNull final String userName) {
        try {
            NoteSearchStrategy strategy = NoteSearchStrategyFactory.getStrategy(queryType);
            return strategy.search(noteRepository, query, userName);
        } catch (Exception e) {
            throw new SnapNoteServiceException("Error occurred during note search." + e);
        }
    }
}
