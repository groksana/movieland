package com.gromoks.movieland.dao.jdbc.sqlbuilder;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    public static String enrichQueryWithOrderRequestParam(String initialQuery, HashMap<String,String> requestParamMap) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(initialQuery);
        int rowNumber = 0;

        for (Map.Entry<String,String> entry : requestParamMap.entrySet()) {
            if (entry.getValue() != null) {
                if (rowNumber == 0) {
                    sqlBuilder.append(" ORDER BY ");
                }
                rowNumber++;
                if (rowNumber > 1) {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(entry.getKey());
                sqlBuilder.append(" ");
                sqlBuilder.append(entry.getValue());
            }
        }

        String resultQuery = sqlBuilder.toString();
        return resultQuery;
    }
}
