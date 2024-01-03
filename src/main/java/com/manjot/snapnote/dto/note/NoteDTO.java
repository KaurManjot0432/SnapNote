package com.manjot.snapnote.dto.note;

import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteDTO {
    private String id;

    private String userName;

    private String title;

    private String content;

    private List<String> labelList;

    private LocalDateTime createdAt;
}
