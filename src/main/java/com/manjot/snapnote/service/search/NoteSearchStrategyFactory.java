package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.enums.QueryType;

import java.util.HashMap;
import java.util.Map;

import static com.manjot.snapnote.model.enums.QueryType.CONTENT;
import static com.manjot.snapnote.model.enums.QueryType.LABEL;

/**
 * Factory class responsible for creating and providing specific search strategies based on the given query type.
 */
public class NoteSearchStrategyFactory {
    // Mapping of QueryType to corresponding NoteSearchStrategy
    private static final Map<QueryType, NoteSearchStrategy> strategies = new HashMap<>();

    static {
        // Initialize the map with default strategies for each QueryType
        strategies.put(CONTENT, new ContentSearchStrategy());
        strategies.put(LABEL, new LabelSearchStrategy());
    }

    /**
     * Gets the appropriate search strategy based on the provided QueryType.
     *
     * @param queryType The type of search query.
     * @return The NoteSearchStrategy corresponding to the provided QueryType, or a default strategy if not found.
     */
    public static NoteSearchStrategy getStrategy(QueryType queryType) {
        return strategies.getOrDefault(queryType, new DefaultSearchStrategy());
    }
}
