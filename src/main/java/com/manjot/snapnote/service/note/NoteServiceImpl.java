package com.manjot.snapnote.service.note;

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

/**
 * Service implementation for handling Note-related operations.
 */
@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository,
                           UserRepository userRepository
    ) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new Note.
     *
     * @param note The Note to be created.
     * @return The created Note.
     */
    @Override
    public Note createNote(@NonNull final Note note) {
        return noteRepository.save(note);
    }

    /**
     * Retrieves a Note by its ID and the associated username.
     *
     * @param noteId   The ID of the Note to retrieve.
     * @param userName The username associated with the Note.
     * @return The retrieved Note.
     * @throws ResourceNotFoundException if the Note is not found.
     */
    @Override
    public Note getNoteById(@NonNull final String noteId,
                            @NonNull final String userName) {
        return noteRepository.findByIdAndUserName(noteId, userName)
                .orElseThrow(() -> new ResourceNotFoundException(INVALID_NOTE));
    }

    /**
     * Retrieves all Notes for a specific user.
     *
     * @param userName The username for which to retrieve Notes.
     * @return The list of Notes for the user.
     * @throws ResourceNotFoundException if no Notes are found for the user.
     */
    @Override
    public List<Note> getAllNotes(@NonNull final String userName) {

        return noteRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No notes found for user: " + userName));
    }

    /**
     * Updates an existing Note.
     *
     * @param id           The ID of the Note to update.
     * @param username     The username associated with the Note.
     * @param updatedNote  The updated Note details.
     * @return The updated Note.
     * @throws ResourceNotFoundException if the Note is not found.
     */
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

    /**
     * Deletes a Note by its ID and the associated username.
     *
     * @param id       The ID of the Note to delete.
     * @param username The username associated with the Note.
     * @throws ResourceNotFoundException if the Note is not found.
     */
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

    /**
     * Shares a Note with another user.
     *
     * @param noteId             The ID of the Note to share.
     * @param senderUsername     The username of the sender.
     * @param recipientUsername  The username of the recipient.
     * @throws ResourceNotFoundException if the Note or User is not found.
     * @throws SnapNoteServiceException  if an error occurs during the sharing process.
     */
    @Override
    public void shareNoteWithUser(@NotNull final String noteId,
                                  @NotNull final String senderUsername,
                                  @NotNull final String recipientUsername) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUserName(noteId, senderUsername);
        Optional<User> optionalUser = userRepository.findByUsername(recipientUsername);

        if(optionalNote.isEmpty()) throw new ResourceNotFoundException(INVALID_NOTE);
        if(optionalUser.isEmpty()) throw new ResourceNotFoundException(INVALID_USER);

        try {
            Note existingNote = optionalNote.get();

            Note sharedNote = new Note();
            sharedNote.setTitle(existingNote.getTitle());
            sharedNote.setContent(existingNote.getContent());
            sharedNote.setLabelList(existingNote.getLabelList());
            sharedNote.setUserName(recipientUsername);

            noteRepository.save(sharedNote);
        } catch (Exception e) {
            throw new SnapNoteServiceException("Error occurred during note search." + e);
        }
    }

    /**
     * Searches for Notes based on a query and query type.
     *
     * @param query     The search query.
     * @param queryType The type of query (e.g., CONTENT, LABEL).
     * @param userName  The username associated with the Notes.
     * @return The list of Notes matching the search criteria.
     * @throws SnapNoteServiceException if an error occurs during the search process.
     */
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
