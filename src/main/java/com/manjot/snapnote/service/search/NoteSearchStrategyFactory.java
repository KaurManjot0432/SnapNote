package com.manjot.snapnote.service.search;

import com.manjot.snapnote.model.enums.QueryType;

import java.util.HashMap;
import java.util.Map;

import static com.manjot.snapnote.model.enums.QueryType.CONTENT;
import static com.manjot.snapnote.model.enums.QueryType.LABEL;

public class NoteSearchStrategyFactory {
    private static final Map<QueryType, NoteSearchStrategy> strategies = new HashMap<>();

    static {
        strategies.put(CONTENT, new ContentSearchStrategy());
        strategies.put(LABEL, new LabelSearchStrategy());
    }

    public static NoteSearchStrategy getStrategy(QueryType queryType) {
        return strategies.getOrDefault(queryType, new DefaultSearchStrategy());
    }
}
