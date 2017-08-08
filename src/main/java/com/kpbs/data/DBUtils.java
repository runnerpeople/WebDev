package com.kpbs.data;

import com.kpbs.response.ResponseServer;
import com.kpbs.model.DBData;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class DBUtils {

    @PersistenceContext
    private EntityManager entityManager;

    private final Logger log = Logger.getLogger(DBUtils.class.getName());


    public ResponseServer getUsers(int start_params, int count_params, HashMap<String,String[]> sorted_params,
                                   HashMap<String,String[]> filter_params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<DBData> criteriaQuery = criteriaBuilder.createQuery(DBData.class);
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<DBData> from = criteriaQuery.from(DBData.class);
        Root<DBData> from2 = countQuery.from(DBData.class);
        CriteriaQuery<DBData> select;
        if (sorted_params != null) {
            for (Map.Entry<String,String[]> sorted_param: sorted_params.entrySet()) {
                if (sorted_param.getValue()[0].equals("asc"))
                    criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sorted_param.getKey())));
                else
                    criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sorted_param.getKey())));
            }
        }
        if (filter_params != null) {
            ArrayList<Predicate> predicates = new ArrayList<>();
            ArrayList<Predicate> predicatesToCount = new ArrayList<>();
            for (Map.Entry<String,String[]> filter_param: filter_params.entrySet()) {
                Field field;
                try {
                    field = DBData.class.getDeclaredField(filter_param.getKey());
                }
                catch (NoSuchFieldException ex) {
                    log.log(Level.WARNING, ex.getMessage());
                    return null;
                }
                Class fieldType = field.getType();
                switch (fieldType.getName()) {
                    case "java.lang.String": {
                        Expression<String> path = from.get(filter_param.getKey());
                        Expression<String> path2 = from2.get(filter_param.getKey());
                        Predicate pred = criteriaBuilder.like(path, filter_param.getValue()[0] + "%");
                        Predicate pred2 = criteriaBuilder.like(path2, filter_param.getValue()[0] + "%");
                        predicates.add(pred);
                        predicatesToCount.add(pred2);
                        break;
                    }
                    case "java.lang.Character": {
                        Expression<Character> path = from.get(filter_param.getKey());
                        Expression<Character> path2 = from2.get(filter_param.getKey());
                        Predicate pred = criteriaBuilder.equal(path, filter_param.getValue()[0].charAt(0));
                        Predicate pred2 = criteriaBuilder.equal(path2, filter_param.getValue()[0].charAt(0));
                        predicates.add(pred);
                        predicatesToCount.add(pred2);
                        break;
                    }
                    case "java.util.Date": {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Expression<Date> path = from.get(filter_param.getKey());
                        Expression<Date> path2 = from2.get(filter_param.getKey());
                        Predicate pred = null;
                        Predicate pred2 = null;
                        if (filter_param.getValue()[0].startsWith(">=")) {
                            try {
                                Date date = formatter.parse(filter_param.getValue()[0].substring(2));
                                pred = criteriaBuilder.greaterThanOrEqualTo(path, date);
                                pred2 = criteriaBuilder.greaterThanOrEqualTo(path2, date);
                            }
                            catch (ParseException ex) {
                                log.log(Level.WARNING, "Error in format date -" + filter_param.getValue()[0]);
                                return null;
                            }
                        }
                        else if (filter_param.getValue()[0].startsWith(">")) {
                            try {
                                Date date = formatter.parse(filter_param.getValue()[0].substring(1));
                                pred = criteriaBuilder.greaterThan(path, date);
                                pred2 = criteriaBuilder.greaterThan(path2, date);
                            }
                            catch (ParseException ex) {
                                log.log(Level.WARNING, "Error in format date -" + filter_param.getValue()[0]);
                                return null;
                            }
                        }
                        else if (filter_param.getValue()[0].startsWith("<=")) {
                            try {
                                Date date = formatter.parse(filter_param.getValue()[0].substring(2));
                                pred = criteriaBuilder.lessThanOrEqualTo(path, date);
                                pred2 = criteriaBuilder.lessThanOrEqualTo(path2, date);
                            }
                            catch (ParseException ex) {
                                log.log(Level.WARNING, "Error in format date -" + filter_param.getValue()[0]);
                                return null;
                            }
                        }
                        else if (filter_param.getValue()[0].startsWith("<")) {
                            try {
                                Date date = formatter.parse(filter_param.getValue()[0].substring(1));
                                pred = criteriaBuilder.lessThan(path, date);
                                pred2 = criteriaBuilder.lessThan(path2, date);
                            }
                            catch (ParseException ex) {
                                log.log(Level.WARNING, "Error in format date -" + filter_param.getValue()[0]);
                                return null;
                            }
                        }
                        if (pred == null && pred2 == null)
                            return null;
                        predicates.add(pred);
                        predicatesToCount.add(pred2);
                        break;
                    }
                    case "java.math.BigDecimal": {
                        Expression<BigDecimal> path = from.get(filter_param.getKey());
                        Expression<BigDecimal> path2 = from2.get(filter_param.getKey());
                        Predicate pred = null;
                        Predicate pred2 = null;
                        if (filter_param.getValue()[0].startsWith(">=")) {
                            if (filter_param.getValue()[0].length() == 2)
                                return null;
                            BigDecimal number = new BigDecimal(filter_param.getValue()[0].substring(2));
                            pred = criteriaBuilder.greaterThanOrEqualTo(path, number);
                            pred2 = criteriaBuilder.greaterThanOrEqualTo(path2, number);
                        }
                        else if (filter_param.getValue()[0].startsWith(">")) {
                            if (filter_param.getValue()[0].length() == 1)
                                return null;
                            BigDecimal number = new BigDecimal(filter_param.getValue()[0].substring(1));
                            pred = criteriaBuilder.greaterThan(path, number);
                            pred2 = criteriaBuilder.greaterThan(path2, number);
                        }
                        else if (filter_param.getValue()[0].startsWith("<=")) {
                            if (filter_param.getValue()[0].length() == 2)
                                return null;
                            BigDecimal number = new BigDecimal(filter_param.getValue()[0].substring(2));
                            pred = criteriaBuilder.lessThanOrEqualTo(path, number);
                            pred2 = criteriaBuilder.lessThanOrEqualTo(path2, number);
                        }
                        else if (filter_param.getValue()[0].startsWith("<")) {
                            if (filter_param.getValue()[0].length() == 1)
                                return null;
                            BigDecimal number = new BigDecimal(filter_param.getValue()[0].substring(1));
                            pred = criteriaBuilder.lessThan(path, number);
                            pred2 = criteriaBuilder.lessThan(path2, number);
                        }
                        else if (filter_param.getValue()[0].startsWith("=")) {
                            if (filter_param.getValue()[0].length() == 1)
                                return null;
                            BigDecimal number = new BigDecimal(filter_param.getValue()[0].substring(1));
                            pred = criteriaBuilder.equal(path, number);
                            pred2 = criteriaBuilder.equal(path2, number);
                        }
                        if (pred == null && pred2 == null)
                            return null;
                        predicates.add(pred);
                        predicatesToCount.add(pred2);
                        break;
                    }
                }
            }
            Predicate[] predicateArray = new Predicate[predicates.size()];
            predicateArray = predicates.toArray(predicateArray);
            Predicate[] predicateToCountArray = new Predicate[predicatesToCount.size()];
            predicateToCountArray = predicatesToCount.toArray(predicateToCountArray);
            criteriaQuery = criteriaQuery.where(predicateArray);
            countQuery = countQuery.where(predicateToCountArray);
        }
        select = criteriaQuery.select(from);

        countQuery.select(criteriaBuilder.count(from2));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        final TypedQuery<DBData> typedQuery = entityManager.createQuery(select);
        List<DBData> data;
        if (start_params <= count) {
            typedQuery.setFirstResult(start_params);
            typedQuery.setMaxResults(count_params);
            data = typedQuery.getResultList();
        }
        else
            data = null;
        return new ResponseServer(data,(long)start_params,count);
    }
}
