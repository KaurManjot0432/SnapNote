package com.manjot.snapnote.controller;

import com.manjot.snapnote.annotation.RateLimited;
import com.manjot.snapnote.dto.note.NoteDTO;
import com.manjot.snapnote.dto.note.NoteDTOMapper;
import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.model.enums.QueryType;
import com.manjot.snapnote.service.note.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.manjot.snapnote.dto.note.NoteDTOMapper.mapToNote;
import static com.manjot.snapnote.dto.note.NoteDTOMapper.mapToNoteDTO;

/**
 * Controller class for handling Note-related requests.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Creates a new Note.
     *
     * @param noteDTO  The DTO containing Note details.
     * @param request  The HTTP request.
     * @return ResponseEntity containing the created Note or an error message.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @RateLimited
    public ResponseEntity<?> createNote(@RequestBody @NotNull NoteDTO noteDTO,
                                        @NotNull final HttpServletRequest request) {
        noteDTO.setUserName(request.getAttribute("userName").toString());
        Note note = noteService.createNote(mapToNote(noteDTO));
        NoteDTO responseDTO = mapToNoteDTO(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves a Note by its ID.
     *
     * @param id       The ID of the Note to retrieve.
     * @param request  The HTTP request.
     * @return ResponseEntity containing the retrieved Note or an error message.
     */
    @GetMapping("/{id}")
    @RateLimited
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNoteById(@PathVariable @NotNull final String id,
                                         @NotNull final HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            Note note = noteService.getNoteById(id, username);
            NoteDTO responseDTO = mapToNoteDTO(note);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Retrieves all Notes for the authenticated user.
     *
     * @param request  The HTTP request.
     * @return ResponseEntity containing the list of Notes or an error message.
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @RateLimited
    public ResponseEntity<?> getAllNotes(@NotNull final HttpServletRequest request) {
        try {
            List<NoteDTO> noteListDTOS = noteService.getAllNotes(request.getAttribute("userName").toString())
                    .stream().map(NoteDTOMapper::mapToNoteDTO).toList();
            return ResponseEntity.ok(noteListDTOS);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Updates an existing Note.
     *
     * @param id              The ID of the Note to update.
     * @param updatedNoteDTO  The DTO containing updated Note details.
     * @param request         The HTTP request.
     * @return ResponseEntity containing the updated Note or an error message.
     */
    @PutMapping("/{id}")
    @RateLimited
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateNote(@PathVariable @NotNull final String id,
                                        @RequestBody @NotNull final NoteDTO updatedNoteDTO,
                                        @NotNull final HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            Note updatedNoteResult = noteService.updateNote(id, username, mapToNote(updatedNoteDTO));
            NoteDTO responseDTO = mapToNoteDTO(updatedNoteResult);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Deletes a Note by its ID.
     *
     * @param id       The ID of the Note to delete.
     * @param request  The HTTP request.
     * @return ResponseEntity indicating success or an error message.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @RateLimited
    public ResponseEntity<?> deleteNoteById(@PathVariable @NotNull final String id,
                                            @NotNull final HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            noteService.deleteNoteById(id, username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Shares a Note with another user.
     *
     * @param id                 The ID of the Note to share.
     * @param recipientUsername The username of the recipient user.
     * @param request            The HTTP request.
     * @return ResponseEntity indicating success or an error message.
     */
    @PostMapping("/{id}/share")
    @PreAuthorize("hasRole('USER')")
    @RateLimited
    public ResponseEntity<?> shareNoteWithUser(@PathVariable @NotNull final String id,
                                               @RequestParam @NotNull final String recipientUsername,
                                               @NotNull final HttpServletRequest request) {
        try {
            String senderUsername = request.getAttribute("userName").toString();
            noteService.shareNoteWithUser(id, senderUsername, recipientUsername);
            return ResponseEntity.ok("Note shared successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }  catch (SnapNoteServiceException e) {
            return handleException(e);
        }
    }

    /**
     * Searches for Notes based on a query and query type.
     *
     * @param q         The search query.
     * @param queryType The type of query (e.g., CONTENT, LABEL).
     * @param request   The HTTP request.
     * @return ResponseEntity containing the search results or an error message.
     */
    @GetMapping("/search")
    @RateLimited
    public ResponseEntity<?> searchNotes(@RequestParam @NotNull final String q,
                                         @RequestParam @NotNull final QueryType queryType,
                                         @NotNull final HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            List<Note> searchResults = noteService.searchNotes(q, queryType, username);
            return ResponseEntity.ok(searchResults);
        } catch (SnapNoteServiceException e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        logger.error("Error occurred while processing request", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
