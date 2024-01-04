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
