package com.manjot.snapnote.service.search;

import com.manjot.snapnote.exception.ResourceNotFoundException;
import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;

import java.util.List;
/**
 * Default search strategy used when no specific strategy is found for the given query type.
 */
public class DefaultSearchStrategy implements NoteSearchStrategy{
    /**
     * Searches for Notes based on a default strategy, e.g., finding all Notes for a specific user.
     *
     * @param repository The repository for Note entities.
     * @param query      The search query (not utilized in default strategy).
     * @param userName   The username associated with the Notes.
     * @return The list of Notes matching the default search criteria.
     * @throws ResourceNotFoundException if no Notes are found.
     */
    @Override
    public List<Note> search(NoteRepository repository, String query, String userName) {
        return repository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Noting found!"));
    }
}
