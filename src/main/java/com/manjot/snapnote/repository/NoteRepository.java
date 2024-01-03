package com.manjot.snapnote.repository;

import com.manjot.snapnote.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    Optional<List<Note>> findByUserName(String userName);
}
