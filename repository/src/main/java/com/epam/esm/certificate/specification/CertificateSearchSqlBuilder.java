package com.epam.esm.certificate.specification;
import java.util.stream.Stream;

public class CertificateSearchSqlBuilder {

    private final String tagName;

    private final String descriptionPart;

    private final String orderBy;

    private final String  SQL_FIND_ALL = "select * from certificate";

    private final String SQL_SELECT_JOINS = " inner join certificate_tag on certificate.id" +
            " = certificate_tag.certificate_id inner join tag t on certificate_tag.tag_id" +
            " = t.id where ";

    private final String SQL_TAG_NAME_PART = "t.name = (:tag_name) ";

    private final String SQL_DESCRIPTION_PART = " description like (:description) ";

    private final String SQL_ORDER_BY_PART = " order by ";

    public CertificateSearchSqlBuilder(String tagName, String descriptionPart,
                                       String orderBy) {
        this.tagName = tagName;
        this.descriptionPart = descriptionPart;
        this.orderBy = orderBy;
    }

    public boolean checkOrderBy() {
        return Stream.of(OrderBy.values()).anyMatch(value ->
                value.getOrderByFieldName().equals(orderBy));
    }

    public String getSqlQuery() {
        String SQL;
        if(tagName == null && descriptionPart == null &&
                !checkOrderBy()) {
            return SQL_FIND_ALL;
        } else {
            SQL = SQL_FIND_ALL;
        }
        if(tagName != null) {
            SQL = SQL + SQL_SELECT_JOINS + SQL_TAG_NAME_PART;
            if(descriptionPart != null) {
                SQL = SQL + " and " + SQL_DESCRIPTION_PART;
            }
        } else {
            if(descriptionPart != null) {
                SQL = SQL + SQL_SELECT_JOINS + SQL_DESCRIPTION_PART;
            }
        }
        if(checkOrderBy()) {
            SQL = SQL + SQL_ORDER_BY_PART + orderBy;
        }
        return SQL;
    }
}
