package com.gromoks.movieland.dao.jdbc.sqlbuilder;

import com.gromoks.movieland.dao.sqlbuilder.QueryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {

    @Autowired
    private String getAllMovieSQL;

    @Test
    public void testEnrichQueryWithOrderRequestParam() {
        String expectedSQL = getAllMovieSQL + " ORDER BY price asc";
        HashMap<String,String> requestParam = new HashMap<>();
        requestParam.put("price","asc");
        assertEquals(QueryBuilder.enrichQueryWithOrderRequestParam(getAllMovieSQL, requestParam),expectedSQL);
    }
}
