package com.elepy.routes;

import com.elepy.concepts.IntegrityEvaluatorImpl;
import com.elepy.concepts.ObjectEvaluator;
import com.elepy.dao.Crud;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

import java.util.List;

public class DefaultCreate<T> implements Create<T> {

    @Override
    public boolean create(Request request, Response response, Crud<T> dao, ObjectMapper objectMapper, List<ObjectEvaluator<T>> objectEvaluators) throws Exception {
        String body = request.body();
        try {


            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, dao.getType());

            final List<T> ts = objectMapper.readValue(body, type);
            return multipleCreate(ts, dao, objectEvaluators);
        } catch (JsonMappingException e) {

            T item = objectMapper.readValue(body, dao.getType());
            return defaultCreate(item, dao, objectMapper, objectEvaluators);
        }
    }

    protected boolean defaultCreate(T product, Crud<T> dao, ObjectMapper objectMapper, List<ObjectEvaluator<T>> objectEvaluators) throws Exception {
        for (ObjectEvaluator<T> objectEvaluator : objectEvaluators) {
            objectEvaluator.evaluate(product);
        }
        new IntegrityEvaluatorImpl<T>().evaluate(product, dao);
        dao.create(product);
        return true;
    }

    protected boolean multipleCreate(Iterable<T> items, Crud<T> dao, List<ObjectEvaluator<T>> objectEvaluators) throws Exception {
        for (T item : items) {
            for (ObjectEvaluator<T> objectEvaluator : objectEvaluators) {
                objectEvaluator.evaluate(item);
            }
            new IntegrityEvaluatorImpl<T>().evaluate(item, dao);
        }
        System.out.println("creating now!");
        dao.create(items);
        return true;

    }
}
