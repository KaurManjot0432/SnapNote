package com.manjot.snapnote.controller;

import com.manjot.snapnote.dto.note.NoteDTO;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.manjot.snapnote.dto.note.NoteDTOMapper.mapToNote;
import static com.manjot.snapnote.dto.note.NoteDTOMapper.mapToNoteDTO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNote(@RequestBody NoteDTO noteDTO, HttpServletRequest request) {
        noteDTO.setUserName(request.getAttribute("userName").toString());
        Note note = noteService.createNote(mapToNote(noteDTO));
        NoteDTO responseDTO = mapToNoteDTO(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
