package com.epam.esm.service.services.impl;

import com.epam.esm.core.exception.UnsupportedParametersForSorting;
import com.epam.esm.core.repository.specification.ParamForSorting;

/**
 * Validation service.
 */
public class ValidationService {
    private static final String SORTING_FAIL_MASSAGE = "sorting_fail";

    private ValidationService() {
    }

    /**
     * Validate param for search.
     *
     * @param param the param
     */
    public static void validateParamForSearch(String param) {
        if (param != null && !ParamForSorting.DATE.name().equalsIgnoreCase(param)
                && !ParamForSorting.NAME.name().equalsIgnoreCase(param)) {
            throw new UnsupportedParametersForSorting(SORTING_FAIL_MASSAGE);
        }
    }
}
