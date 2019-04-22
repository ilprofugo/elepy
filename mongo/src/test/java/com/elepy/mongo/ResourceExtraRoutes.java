package com.elepy.mongo;


import com.elepy.annotations.ElepyConstructor;
import com.elepy.annotations.Inject;
import com.elepy.annotations.Route;
import com.elepy.dao.Crud;
import com.elepy.http.HttpMethod;
import com.elepy.http.Request;
import com.elepy.http.Response;

public class ResourceExtraRoutes {

    private Crud<Resource> resourceCrud;

    @ElepyConstructor
    public ResourceExtraRoutes(@Inject(tag = "/resources") Crud<Resource> resourceCrud) {
        this.resourceCrud = resourceCrud;
    }

    @Route(path = "/resources-extra", requiredPermissions = {}, method = HttpMethod.GET)
    public String newRoute(Request request, Response response) {
        response.status(201);
        return "generated";
    }
}
