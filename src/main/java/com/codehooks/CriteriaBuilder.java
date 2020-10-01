package com.codehooks;

/*
 * Created by CodeHooks.
 * User: Vijay NK
 * Date: 18/07/20
 * Time: 8:13 pm
 * To change this file fork the repo and start modifying the files
 */

import com.codehooks.constants.AppConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.stream.Collectors;

public class CriteriaBuilder {

    public Criteria buildCriteria(HashMap<String, Object> params) {
        Set<String> keys = params.keySet();
        Criteria andCriteria = new Criteria();
        Criteria orCriteria = new Criteria();
        boolean isOr = false;
        for (String key : keys) {
            if (key.contains("{or}")) {
                isOr = true;
                String actualKey = key.substring(4);
                createQuery(actualKey, params.get("{or}" + actualKey), orCriteria);
                continue;
            }
            createQuery(key, params.get(key), andCriteria);
        }

        return isOr ? new Criteria().orOperator(andCriteria, orCriteria) : andCriteria;
    }

    private void createQuery(String keyWithCondition, Object value, Criteria criteria) {
        if (keyWithCondition != null && !keyWithCondition.isEmpty()) {
            String[] keyAndCondition = keyWithCondition.split("[.]");
            String key = keyAndCondition[0];
            String condition = keyAndCondition[1];
            String[] values = value.toString().split("[,]");
            switch (condition) {
                case "like":
                case "eq":
                    criteria.and(key).is(value);
                case "neq":
                    criteria.and(key).ne(value);
                    break;
                case "in":
                    criteria.and(key).in(values);
                    break;
                case "nin":
                    criteria.and(key).nin(values);
                    break;
                case "gt":
                    criteria.and(key).gt(value);
                    break;
                case "gte":
                    criteria.and(key).gte(value);
                    break;
                case "lt":
                    criteria.and(key).lt(value);
                    break;
                case "lte":
                    criteria.and(key).lte(value);
                    break;
                case "between":
                    criteria.and(key).gt(values[1]).and(key).lt(values[0]);
                    break;
                case "startsWith":
                    criteria.and(key).regex("^" + value, "i");
                    break;
                case "endsWith":
                    criteria.and(key).regex(value + "^", "i");
                    break;
                case "contains":
                    criteria.and(key).regex(value.toString());
                    break;
                case "containsIc":
                    criteria.and(key).regex(value.toString(), "i");
                    break;
                default:
                    break;
            }
        }
    }

    public Optional<Sort> sort(HashMap<String, Object> params) {
        Set<String> sortKeys = params.keySet().stream().filter(key -> key.contains(AppConstants.SORT_KEYWORD)).collect(Collectors.toSet());

        Set<String> descending = sortKeys.stream()
                .filter(key -> params.get(key).toString().equals("d"))
                .map(key -> key.replace(AppConstants.SORT_KEYWORD, ""))
                .collect(Collectors.toSet());

        Set<String> ascending = sortKeys.stream()
                .filter(key -> !params.get(key).toString().equals("d"))
                .map(key -> key.replace(AppConstants.SORT_KEYWORD, ""))
                .collect(Collectors.toSet());

        if (descending.isEmpty() && ascending.isEmpty()) {
            return Optional.empty();
        }

        List<Sort.Order> orders = new ArrayList<>();
        descending.stream().map(Sort.Order::desc).forEach(orders::add);
        ascending.stream().map(Sort.Order::asc).forEach(orders::add);
        return Optional.of(Sort.by(orders));
    }

    public Optional<Pageable> pageable(HashMap<String, Object> params) {
        List<String> keys = params.keySet().stream().filter(key -> key.equalsIgnoreCase(AppConstants.PAGE_PROP)).collect(Collectors.toList());
        if (keys.isEmpty()) {
            return Optional.empty();
        }
        String key = keys.get(0);
        String value = params.get(key).toString();
        String[] values = value.split("[,]");
        return Optional.of(PageRequest.of(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
    }


}
