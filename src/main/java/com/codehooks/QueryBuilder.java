package com.codehooks;

/*
 * Created by CodeHooks.
 * User: Vijay NK
 * Date: 19/07/20
 * Time: 3:58 pm
 * To change this file fork the repo and start modifying the files
 */

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Optional;

public class QueryBuilder {

    private CriteriaBuilder criteriaBuilder = new CriteriaBuilder();

    public Query prepareQuery(HashMap<String, Object> params) {
        Query query = new Query(criteriaBuilder.buildCriteria(params));
        Optional<Sort> sortOptional = criteriaBuilder.sort(params);
        if(sortOptional.isPresent()) {
            query = query.with(sortOptional.get());
        }
        Optional<Pageable> pageable = criteriaBuilder.pageable(params);
        if(pageable.isPresent()) {
            query = query.with(pageable.get());
        }
        return query;
    }


}
