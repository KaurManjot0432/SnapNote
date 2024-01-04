package com.manjot.snapnote.service.search;

import org.junit.jupiter.api.Test;

import static com.manjot.snapnote.model.enums.QueryType.CONTENT;
import static com.manjot.snapnote.model.enums.QueryType.LABEL;
import static com.manjot.snapnote.model.enums.QueryType.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NoteSearchStrategyFactoryTest {

    @Test
    void getStrategy_Content() {
        NoteSearchStrategy strategy = NoteSearchStrategyFactory.getStrategy(CONTENT);

        assertNotNull(strategy);
    }

    @Test
    void getStrategy_Label() {
        NoteSearchStrategy strategy = NoteSearchStrategyFactory.getStrategy(LABEL);

        assertNotNull(strategy);
    }

    @Test
    void getStrategy_Default() {
        NoteSearchStrategy strategy = NoteSearchStrategyFactory.getStrategy(DEFAULT);

        assertNotNull(strategy);
    }
}

