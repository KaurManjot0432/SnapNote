package com.manjot.snapnote.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model class representing a Note entity in MongoDB.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "notes")
public class Note {
    /**
     * The unique identifier of the note.
     */
    @Id
    private String id;

    /**
     * The title of the note.
     */
    @NotNull
    private String title;

    /**
     * The content of the note, which is text-indexed for searching.
     */
    @TextIndexed
    private String content;

    /**
     * List of labels associated with the note, text-indexed for searching.
     */
    @TextIndexed
    private List<String> labelList;

    /**
     * The username of the user who owns the note.
     */
    @NotNull
    private String userName;

    /**
     * The timestamp when the note was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;
}
