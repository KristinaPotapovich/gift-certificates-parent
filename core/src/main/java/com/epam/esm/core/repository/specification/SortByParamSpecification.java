package com.epam.esm.core.repository.specification;

/**
 * The type Sort by param specification.
 */
public class SortByParamSpecification {
    private static final String CREATE_DATE = "create_date";
    private static final String CREATE_DATE_DESC = "create_date DESC";
    private static final String NAME = "name";
    private static final String NAME_DESC = "name DESC";
    private String paramForSorting;
    private String order;

    /**
     * Instantiates a new Sort by param specification.
     *
     * @param paramForSorting the param for sorting
     * @param order           the order
     */
    public SortByParamSpecification(String paramForSorting, String order) {
        this.paramForSorting = paramForSorting;
        this.order = order;
    }

    /**
     * Build query for sorting string.
     *
     * @param query the query
     * @return the string
     */
    public String buildQueryForSorting(String query) {
        String fullQuery = null;
        if (ParamForSort.DATE.name().toLowerCase().equals(paramForSorting)
                && Order.ASC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append(CREATE_DATE).toString();
        }
        if (ParamForSort.DATE.name().toLowerCase().equals(paramForSorting)
                && Order.DESC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append(CREATE_DATE_DESC).toString();
        }
        if (ParamForSort.NAME.name().toLowerCase().equals(paramForSorting)
                && Order.ASC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append(NAME).toString();
        }
        if (ParamForSort.NAME.name().toLowerCase().equals(paramForSorting)
                && Order.DESC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append(NAME_DESC).toString();
        }
        return fullQuery;
    }

    /**
     * The enum Order.
     */
    public enum Order {
        /**
         * Asc order.
         */
        ASC,
        /**
         * Desc order.
         */
        DESC
    }

    /**
     * The enum Param for sort.
     */
    public enum ParamForSort {
        /**
         * Date param for sort.
         */
        DATE,
        /**
         * Name param for sort.
         */
        NAME
    }
}
