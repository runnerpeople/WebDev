package com.kpbs;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            for (Map.Entry<String,String[]> filter_param: filter_params.entrySet()) {
                Expression<String> path = from.get(filter_param.getKey());
                Expression<String> path2 = from2.get(filter_param.getKey());
                Predicate pred = criteriaBuilder.like(path, filter_param.getValue()[0] + "%");
                Predicate pred2 = criteriaBuilder.like(path2,filter_param.getValue()[0] + "%");
                criteriaQuery = criteriaQuery.where(pred);
                countQuery = countQuery.where(pred2);
            }
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
