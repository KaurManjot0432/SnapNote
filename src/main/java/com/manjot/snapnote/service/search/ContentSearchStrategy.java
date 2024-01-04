package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.Note;
import com.manjot.snapnote.repository.NoteRepository;

import java.util.List;

/**
 * Search strategy for searching Notes based on content.
 */
public class ContentSearchStrategy implements NoteSearchStrategy {
    /**
     * Searches for Notes based on content containing the given query and associated with a specific user.
     *
     * @param repository The repository for Note entities.
     * @param query      The search query.
     * @param userName   The username associated with the Notes.
     * @return The list of Notes matching the search criteria.
     */
    @Override
    public List<Note> search(NoteRepository repository, String query, String userName) {
        return repository.findByContentContainingAndUserName(query, userName);
    }
}
