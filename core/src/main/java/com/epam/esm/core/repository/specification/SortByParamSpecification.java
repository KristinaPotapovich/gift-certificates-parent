package com.epam.esm.core.repository.specification;

public class SortByParamSpecification {
    private String paramForSorting;
    private String order;

    public SortByParamSpecification(String paramForSorting, String order) {
        this.paramForSorting = paramForSorting;
        this.order = order;
    }

    public String buildQueryForSorting(String query) {
        String fullQuery = null;
        if (ParamForSort.DATE.name().toLowerCase().equals(paramForSorting)
                && Order.ASC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append("create_date").toString();
        }
        if (ParamForSort.DATE.name().toLowerCase().equals(paramForSorting)
                && Order.DESC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append("create_date DESC").toString();
        }
        if (ParamForSort.NAME.name().toLowerCase().equals(paramForSorting)
                && Order.ASC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append("name").toString();
        }
        if (ParamForSort.NAME.name().toLowerCase().equals(paramForSorting)
                && Order.DESC.name().toLowerCase().equals(order)) {
            fullQuery = new StringBuilder().append(query).append("name DESC").toString();
        }
        return fullQuery;
    }

    public enum Order {
        ASC,
        DESC
    }

    public enum ParamForSort {
        DATE,
        NAME
    }
}
