package com.ryansusana.elepy.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.elepy.dao.*;
import spark.Request;
import spark.Response;

public class DefaultFind<T> implements Find<T> {


    @Override
    public Page<T> find(Request request, Response response, Crud<T> dao, ObjectMapper objectMapper) {

        response.type("application/json");
        String q = request.queryParams("q");

        String fieldSort = request.queryParams("sortBy");

        String fieldDirection = request.queryParams("sortDirection");

        String ps = request.queryParams("pageSize");
        String pn = request.queryParams("pageNumber");

        int pageSize = ps == null ? Integer.MAX_VALUE : Integer.parseInt(ps);
        int pageNumber = pn == null ? 1 : Integer.parseInt(pn);

        PageSetup pageSetup = new PageSetup(pageSize, pageNumber);

        if (q != null || fieldSort != null || fieldDirection != null) {
            return dao.search(new SearchSetup(q, fieldSort, (fieldDirection != null && fieldDirection.toLowerCase().contains("desc")) ? SortOption.DESCENDING : SortOption.ASCENDING), pageSetup);
        }
        return dao.get(pageSetup);
    }
}