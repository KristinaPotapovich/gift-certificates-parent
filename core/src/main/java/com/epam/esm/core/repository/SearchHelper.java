package com.epam.esm.core.repository;

public class SearchHelper {
    private static final String SYMBOL_FOR_CONCAT = "%";

    private SearchHelper() {

    }

    public static String buildAppealForDataBase(String param) {
        return SYMBOL_FOR_CONCAT.concat(param).concat(SYMBOL_FOR_CONCAT);
    }
}
