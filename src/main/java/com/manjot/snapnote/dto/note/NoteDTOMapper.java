package com.manjot.snapnote.dto.note;

import com.manjot.snapnote.model.Note;
import jakarta.validation.constraints.NotNull;

/**
 * Mapper class to convert between Note and NoteDTO objects.
 */
public class NoteDTOMapper {

    /**
     * Maps a Note entity to a NoteDTO.
     *
     * @param note The Note entity to be mapped.
     * @return The corresponding NoteDTO.
     */
    public static NoteDTO mapToNoteDTO(@NotNull final Note note) {
       return NoteDTO.builder()
               .id(note.getId())
               .title(note.getTitle())
               .content(note.getContent())
               .labelList(note.getLabelList())
               .createdAt(note.getCreatedAt())
               .userName(note.getUserName())
               .build();
    }

    /**
     * Maps a NoteDTO to a Note entity.
     *
     * @param noteDTO The NoteDTO to be mapped.
     * @return The corresponding Note entity.
     */
    public static Note mapToNote(@NotNull final NoteDTO noteDTO) {
        return Note.builder()
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .labelList(noteDTO.getLabelList())
                .userName(noteDTO.getUserName())
                .build();
    }
}
