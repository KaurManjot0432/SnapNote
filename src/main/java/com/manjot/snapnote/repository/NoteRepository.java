package com.manjot.snapnote.repository;

import com.manjot.snapnote.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Note entities in MongoDB.
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    Optional<List<Note>> findByUserName(String userName);
    Optional<Note> findByIdAndUserName(String id, String userName);
    List<Note> findByContentContainingAndUserName(String content, String userName);
    List<Note> findByLabelListInAndUserName(List<String> labels, String userName);
}
