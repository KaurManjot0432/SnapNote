package com.manjot.snapnote.controller;

import com.manjot.snapnote.dto.note.NoteDTO;
import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.exception.SnapNoteServiceException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.model.enums.QueryType;
import com.manjot.snapnote.service.note.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class NoteControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createNote_Success() {
        NoteDTO noteDTO = new NoteDTO();
        Note note = new Note();
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.createNote(any(Note.class))).thenReturn(note);

        ResponseEntity<?> response = noteController.createNote(noteDTO, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(NoteDTO.class);
    }

    @Test
    public void getNoteById_Success() {
        String noteId = "1";
        Note note = new Note();
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.getNoteById(eq(noteId), anyString())).thenReturn(note);

        ResponseEntity<?> response = noteController.getNoteById(noteId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(NoteDTO.class);
    }

    @Test
    public void getNoteById_NotFound() {
        String noteId = "1";
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.getNoteById(eq(noteId), anyString())).thenThrow(new ResourceNotFoundException("Note not found"));

        ResponseEntity<?> response = noteController.getNoteById(noteId, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Note not found");
    }

    @Test
    void getAllNotes_Success() {
        Note note = new Note();
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.getAllNotes(any())).thenReturn(List.of(note));

        ResponseEntity<?> response = noteController.getAllNotes(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
        verify(noteService, times(1)).getAllNotes(any());
    }

    @Test
    void getAllNotes_Error() {
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.getAllNotes(any())).thenThrow(ResourceNotFoundException.class);

        ResponseEntity<?> response = noteController.getAllNotes(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(noteService, times(1)).getAllNotes(any());
    }

    @Test
    void updateNote_Success() {
        String noteId = "noteId";
        NoteDTO updatedNoteDTO = new NoteDTO();
        when(request.getAttribute("userName")).thenReturn("user");

        Note updatedNoteResult = new Note();
        when(noteService.updateNote(any(), any(), any())).thenReturn(updatedNoteResult);

        ResponseEntity<?> response = noteController.updateNote(noteId, updatedNoteDTO, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(NoteDTO.class);
        verify(noteService, times(1)).updateNote(any(), any(), any());
    }

    @Test
    void updateNote_NotFound() {
        String noteId = "noteId";
        NoteDTO updatedNoteDTO = new NoteDTO();
        when(request.getAttribute("userName")).thenReturn("user");

        when(noteService.updateNote(any(), any(), any())).thenThrow(ResourceNotFoundException.class);
        ResponseEntity<?> response = noteController.updateNote(noteId, updatedNoteDTO, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(noteService, times(1)).updateNote(any(), any(), any());
    }

    @Test
    void deleteNoteById_Success() {
        String noteId = "noteId";
        when(request.getAttribute("userName")).thenReturn("user");

        ResponseEntity<?> response = noteController.deleteNoteById(noteId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(noteService, times(1)).deleteNoteById(any(), any());
    }

    @Test
    void deleteNoteById_NotFound() {
        String noteId = "noteId";
        when(request.getAttribute("userName")).thenReturn("user");

        doThrow(ResourceNotFoundException.class).when(noteService).deleteNoteById(any(), any());

        ResponseEntity<?> response = noteController.deleteNoteById(noteId, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(noteService, times(1)).deleteNoteById(any(), any());
    }

    @Test
    void shareNoteWithUser_Success() {
        String noteId = "noteId";
        String recipientUsername = "recipientUsername";
        when(request.getAttribute("userName")).thenReturn("user");

        ResponseEntity<?> response = noteController.shareNoteWithUser(noteId, recipientUsername, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Note shared successfully.");
        verify(noteService, times(1)).shareNoteWithUser(any(), any(), any());
    }

    @Test
    void shareNoteWithUser_NotFound() {
        String noteId = "noteId";
        String recipientUsername = "recipientUsername";
        when(request.getAttribute("userName")).thenReturn("user");

        doThrow(ResourceNotFoundException.class).when(noteService).shareNoteWithUser(any(), any(), any());

        ResponseEntity<?> response = noteController.shareNoteWithUser(noteId, "username", request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(noteService, times(1)).shareNoteWithUser(any(), any(), any());
    }

    @Test
    void shareNoteWithUser_Error() {
        String noteId = "noteId";
        String recipientUsername = "recipientUsername";
        when(request.getAttribute("userName")).thenReturn("user");

        doThrow(SnapNoteServiceException.class).when(noteService).shareNoteWithUser(any(), any(), any());

        ResponseEntity<?> response = noteController.shareNoteWithUser(noteId, "username", request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(noteService, times(1)).shareNoteWithUser(any(), any(), any());
    }
    @Test
    void searchNotes_Success() {
        String q = "query";
        QueryType queryType = QueryType.CONTENT;
        List<Note> noteList = new ArrayList<>();
        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.searchNotes(any(), any(), any())).thenReturn(noteList);

        ResponseEntity<?> response = noteController.searchNotes(q, queryType, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
        verify(noteService, times(1)).searchNotes(any(), any(), any());
    }

    @Test
    void searchNotes_Exception() {
        String q = "query";
        QueryType queryType = QueryType.CONTENT;

        when(request.getAttribute("userName")).thenReturn("user");
        when(noteService.searchNotes(any(), any(), any())).thenThrow(SnapNoteServiceException.class);

        ResponseEntity<?> response = noteController.searchNotes(q, queryType, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(noteService, times(1)).searchNotes(any(), any(), any());
    }
}
