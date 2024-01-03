package com.manjot.snapnote.controller;

import com.manjot.snapnote.dto.note.NoteDTO;
import com.manjot.snapnote.dto.note.NoteDTOMapper;
import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> createNote(@RequestBody NoteDTO noteDTO,
                                        HttpServletRequest request) {
        try {
            noteDTO.setUserName(request.getAttribute("userName").toString());
            Note note = noteService.createNote(mapToNote(noteDTO));
            NoteDTO responseDTO = mapToNoteDTO(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNoteById(@PathVariable String id,
                                         HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            Note note = noteService.getNoteById(id, username);
            NoteDTO responseDTO = mapToNoteDTO(note);
            return ResponseEntity.ok(responseDTO);
        } catch (SnapNoteServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotes(HttpServletRequest request) {
        try {
            List<NoteDTO> noteListDTOS = noteService.getAllNotes(request.getAttribute("userName").toString())
                    .stream().map(NoteDTOMapper::mapToNoteDTO).toList();
            return ResponseEntity.ok(noteListDTOS);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateNote(@PathVariable String id,
                                        @RequestBody NoteDTO updatedNoteDTO,
                                        HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            Note updatedNoteResult = noteService.updateNote(id, username, mapToNote(updatedNoteDTO));
            NoteDTO responseDTO = mapToNoteDTO(updatedNoteResult);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNoteById(@PathVariable String id,
                                            HttpServletRequest request) {
        try {
            String username = request.getAttribute("userName").toString();
            noteService.deleteNoteById(id, username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> shareNoteWithUser(@PathVariable String id,
                                               @RequestParam String recipientUsername,
                                               HttpServletRequest request) {
        try {
            String senderUsername = request.getAttribute("userName").toString();
            noteService.shareNoteWithUser(id, senderUsername, recipientUsername);
            return ResponseEntity.ok("Note shared successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }  catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        logger.error("Error occurred while processing request", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
