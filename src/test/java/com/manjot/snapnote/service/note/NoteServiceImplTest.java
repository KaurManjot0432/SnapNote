package com.manjot.snapnote.service.note;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.model.User;
import com.manjot.snapnote.repository.NoteRepository;
import com.manjot.snapnote.repository.UserRepository;
import com.manjot.snapnote.service.search.NoteSearchStrategyFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.manjot.snapnote.exception.ErrorMessages.INVALID_NOTE;
import static com.manjot.snapnote.exception.ErrorMessages.INVALID_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteSearchStrategyFactory noteSearchStrategyFactory;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    public void createNote_Success() {
        Note inputNote = new Note();
        Note savedNote = new Note();
        Mockito.when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        Note result = noteService.createNote(inputNote);

        assertThat(result).isEqualTo(savedNote);
        Mockito.verify(noteRepository).save(any(Note.class));
    }

    @Test
    public void getNoteById_Success() {
        String noteId = "noteId";
        String userName = "userName";
        Note note = new Note();
        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(userName))).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById(noteId, userName);

        assertThat(result).isEqualTo(note);
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(userName));
    }

    @Test
    public void getNoteById_NotFound() {
        String noteId = "noteId";
        String userName = "userName";
        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(userName))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getNoteById(noteId, userName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(INVALID_NOTE);
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(userName));
    }

    @Test
    public void getAllNotes_Success() {
        String userName = "userName";
        List<Note> notes = Arrays.asList(new Note(), new Note());
        Mockito.when(noteRepository.findByUserName(eq(userName))).thenReturn(Optional.of(notes));

        List<Note> result = noteService.getAllNotes(userName);

        assertThat(result).isEqualTo(notes);
        Mockito.verify(noteRepository).findByUserName(eq(userName));
    }

    @Test
    public void getAllNotes_NotFound() {
        String userName = "userName";
        Mockito.when(noteRepository.findByUserName(eq(userName))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getAllNotes(userName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No notes found");
        Mockito.verify(noteRepository).findByUserName(eq(userName));
    }

    @Test
    public void updateNote_Success() {
        String username = "username";
        String id = "id";
        Note updatedNote = new Note();
        Note existingNote = new Note();

        Mockito.when(noteRepository.findByIdAndUserName(id, username)).thenReturn(Optional.of(existingNote));
        Mockito.when(noteRepository.save(any(Note.class))).thenReturn(existingNote);
        Note result = noteService.updateNote(id, username, updatedNote);

        assertThat(result).isEqualTo(existingNote);
        Mockito.verify(noteRepository).save(any(Note.class));
    }
    @Test
    public void updateNote_Failure() {
        String noteId = "invalidNoteId";
        String username = "testUser";
        Note updatedNote = new Note();
        Optional<Note> emptyNote = Optional.empty();

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(username))).thenReturn(emptyNote);

        assertThatThrownBy(() -> noteService.updateNote(noteId, username, updatedNote))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(INVALID_NOTE);

        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(username));
        Mockito.verify(noteRepository, Mockito.never()).save(any());
    }

    @Test
    public void deleteNoteById_Success() {
        String noteId = "validNoteId";
        String username = "testUser";
        Note existingNote = new Note();
        Optional<Note> optionalNote = Optional.of(existingNote);

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(username))).thenReturn(optionalNote);
        noteService.deleteNoteById(noteId, username);
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(username));
        Mockito.verify(noteRepository).deleteById(eq(noteId));
    }

    @Test
    public void deleteNoteById_InvalidNoteId() {
        String noteId = "invalidNoteId";
        String username = "testUser";
        Optional<Note> emptyNote = Optional.empty();

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(username))).thenReturn(emptyNote);

        assertThatThrownBy(() -> noteService.deleteNoteById(noteId, username))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(INVALID_NOTE);

        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(username));
        Mockito.verify(noteRepository, Mockito.never()).deleteById(eq(noteId));
    }

    @Test
    public void shareNoteWithUser_Success() {
        String noteId = "validNoteId";
        String senderUsername = "senderUser";
        String recipientUsername = "recipientUser";
        Note existingNote = new Note();
        Optional<Note> optionalNote = Optional.of(existingNote);
        Optional<User> optionalUser = Optional.of(new User());

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(senderUsername))).thenReturn(optionalNote);
        Mockito.when(userRepository.findByUsername(eq(recipientUsername))).thenReturn(optionalUser);

        noteService.shareNoteWithUser(noteId, senderUsername, recipientUsername);

        // Verify that findByIdAndUserName and findByUsername were called with the expected arguments
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(senderUsername));
        Mockito.verify(userRepository).findByUsername(eq(recipientUsername));
        // Verify that save method was called with the expected argument (sharedNote)
        Mockito.verify(noteRepository).save(any(Note.class));
    }

    @Test
    public void shareNoteWithUser_InvalidNote() {
        String noteId = "invalidNoteId";
        String senderUsername = "senderUser";
        String recipientUsername = "recipientUser";
        Optional<Note> emptyNote = Optional.empty();
        Optional<User> emptyUser = Optional.empty();

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(senderUsername))).thenReturn(emptyNote);
        Mockito.when(userRepository.findByUsername(eq(recipientUsername))).thenReturn(emptyUser);

        assertThatThrownBy(() -> noteService.shareNoteWithUser(noteId, senderUsername, recipientUsername))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(INVALID_NOTE);

        // Verify that findByIdAndUserName and findByUsername were called with the expected arguments
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(senderUsername));
        Mockito.verify(userRepository).findByUsername(eq(recipientUsername));
        // Verify that save method was not called
        Mockito.verify(noteRepository, Mockito.never()).save(any(Note.class));
    }
    @Test
    public void shareNoteWithUser_InvalidUser() {
        String noteId = "invalidNoteId";
        String senderUsername = "senderUser";
        String recipientUsername = "recipientUser";
        Note existingNote = new Note();
        Optional<Note> optionalNote = Optional.of(existingNote);
        Optional<User> emptyUser = Optional.empty();

        Mockito.when(noteRepository.findByIdAndUserName(eq(noteId), eq(senderUsername))).thenReturn(optionalNote);
        Mockito.when(userRepository.findByUsername(eq(recipientUsername))).thenReturn(emptyUser);

        assertThatThrownBy(() -> noteService.shareNoteWithUser(noteId, senderUsername, recipientUsername))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(INVALID_USER);

        // Verify that findByIdAndUserName and findByUsername were called with the expected arguments
        Mockito.verify(noteRepository).findByIdAndUserName(eq(noteId), eq(senderUsername));
        Mockito.verify(userRepository).findByUsername(eq(recipientUsername));
        // Verify that save method was not called
        Mockito.verify(noteRepository, Mockito.never()).save(any(Note.class));
    }
}

