package com.mycomp.demo.persistence.service;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface JpaService {

    String READ_ONLY = "readOnly";
    String READ_WRITE = "readWrite";

    <T> T findById(Class<T> clazz, Object id);
    <T> List<T> findByIds(Class<T> clazz, List<?> ids);

    <T> void update(T... o);
    <T> void update(Collection<T> c);

    <T> void save(T... o);
    <T> void save(Collection<T> c);

    <T> void delete(T... o);
    <T> void delete(Collection<T> c);

    void setCacheQueries(boolean cacheQueries);
    void setQueryCacheRegion(String queryCacheRegion);

    int executeUpdate(String query);
    int executeUpdate(String query, Map<String, Object> parameters);
    int executeBatchUpdate(String query, Map<String, Object> parameters);

    void flushAndClear();

    <T> List<T> executeQuery(String query);
    <T> List<T> executeQuery(String query, Map<String, Object> parameters);
    <T> List<T> executeBatchQuery(String query, Map<String, Object> parameters);

    <T> T findFirstByQuery(String query);
    <T> T findFirstByQuery(String query, Map<String, Object> parameters);

    <T> List<T> findAll(Class<T> clazz);

    EntityManager getEntityManager();

    <T> List<T> executeQueryWithTopResults(String query, Map<String, Object> paramMap, int numberOfResult);
    <T> List<T> executeQueryWithTopResults(String queryString, int numberOfResult);
    <T> List<T> executeQueryWithPaging(String query, Map<String, Object> paramMap, int offset, int limit);

    final class FailedToFindEntity extends RuntimeException {
        <T> FailedToFindEntity(final Throwable cause, final Class<T> clazz, final Object id) {
            super("Entity class [" + clazz + "], id [" + id + "]", cause);
        }
    }

    final class FailedToUpdateEntities extends RuntimeException {
        FailedToUpdateEntities(final Throwable cause, final Object... entities) {
            super("Entities: " + StringUtils.join(entities, ','), cause);
        }

        FailedToUpdateEntities() {
            super("update(...) not allowed for read only persistence service.");
        }
    }

    final class FailedToDeleteEntities extends RuntimeException {
        FailedToDeleteEntities(final Throwable cause, final Object... entities) {
            super("Entities: " + StringUtils.join(entities, ','), cause);
        }
        FailedToDeleteEntities() {
            super("Not allowed for read only persistence service.");
        }
    }

    final class FailedToSaveEntities extends RuntimeException {
        FailedToSaveEntities(final Throwable cause, final Object... entities) {
            super("Entities: " + StringUtils.join(entities, ','), cause);
        }
        FailedToSaveEntities() {
            super("Not allowed for read only persistence service.");
        }
    }

    final class FailedToExecuteUpdate extends RuntimeException {
        FailedToExecuteUpdate(final Throwable cause, final String queryString) {
            super("Update query: [" + queryString + "]", cause);
        }
        FailedToExecuteUpdate(final Throwable cause, final String queryString, final Map<String, Object> parameters) {
            super("Update query: [" + queryString + "], parameters: " + parameters, cause);
        }
    }

    final class FailedToExecuteQuery extends RuntimeException {
        FailedToExecuteQuery(final Throwable cause, final String queryString) {
            super("Query: [" + queryString + "]", cause);
        }
        FailedToExecuteQuery(final Throwable cause, final String queryString, final Map<String, Object> parameters) {
            super("Query: [" + queryString + "], parameters: " + parameters, cause);
        }
    }

    final class FailedToExecuteBatchQueryNoInParam extends RuntimeException {
        FailedToExecuteBatchQueryNoInParam(final String queryString) {
            super("Parameters for query [" + queryString + "] had no parameters.  Expecting something like 'select * from table where val in (:in)'");
        }
        FailedToExecuteBatchQueryNoInParam(final String queryString, final Map<String, Object> parameters) {
            super("Parameters for query [" + queryString + "] had no (:in) param, found [" + parameters + "].  Expecting something like 'select * from table where val in (:in)'");
        }
    }

}
