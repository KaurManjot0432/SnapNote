package com.manjot.snapnote.dto.note;

import com.manjot.snapnote.model.Note;
import jakarta.validation.constraints.NotNull;

public class NoteDTOMapper {
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

    public static Note mapToNote(@NotNull final NoteDTO noteDTO) {
        return Note.builder()
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .labelList(noteDTO.getLabelList())
                .userName(noteDTO.getUserName())
                .build();
    }
}
