package com.mycomp.demo.persistence.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.jpa.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockScope;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Qualifier(JpaService.READ_WRITE)
public class JpaServiceImpl implements JpaService {

    static final int MAX_PARAM_COUNT = 500;
    static final String IN = "in";

    @PersistenceContext private EntityManager entityManager;

    @PostConstruct
    public void setLockMode() {
        entityManager.setProperty(AvailableSettings.LOCK_SCOPE, PessimisticLockScope.EXTENDED);
    }

    @Override
    public void setCacheQueries(final boolean cacheQueries) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setQueryCacheRegion(final String queryCacheRegion) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @Transactional
    public <T> void update(final T... entities) {
        try {
            for (T entity: entities) {
                entityManager.merge(entity);
            }
        } catch (final Exception e) {
            throw new FailedToUpdateEntities(e, entities);
        }
    }

    @Override
    @Transactional
    public <T> void save(final T... entities) {
        try {
            for (T entity: entities) {
                entityManager.persist(entity);
            }
        } catch (final Exception e) {
            throw new FailedToSaveEntities(e, entities);
        }
    }

    @Override
    @Transactional
    public <T> void update(final Collection<T> c) {
        update(c.toArray());
    }

    @Override
    @Transactional
    public <T> void save(final Collection<T> c) {
        save(c.toArray());
    }


    @Override
    @Transactional
    public <T> void delete(final T... entities) {
        try {
            for (T entity: entities) {
                entityManager.remove(entity);
            }
        } catch (final Exception e) {
            throw new FailedToDeleteEntities(e, entities);
        }
    }

    @Override
    @Transactional
    public <T> void delete(final Collection<T> c) {
        delete(c.toArray());
    }

    @Override
    @Transactional
    public int executeUpdate(final String queryString) {
        try {
            return entityManager.createQuery(queryString).executeUpdate();
        } catch (final Exception e) {
            throw new FailedToExecuteUpdate(e, queryString);
        }
    }

    @Override
    @Transactional
    public int executeUpdate(final String queryString, final Map<String, Object> parameters) {
        try {
            final Query query = createQuery(queryString);
            setParameters(parameters, query);
            return query.executeUpdate();
        } catch (final Exception e) {
            throw new FailedToExecuteUpdate(e, queryString, parameters);
        }
    }

    @Override
    @Transactional
    public int executeBatchUpdate(final String queryString, final Map<String, Object> parameters) {
        checkInParams(queryString, parameters);

        int count = 0;

        for (Map<String, Object> m: partition(parameters)) {
            final Query query = createQuery(queryString);
            setParameters(m, query);
            count += query.executeUpdate();
        }

        return count;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQuery(final String queryString, final Map<String, Object> parameters) {
        try {
            final Query query = createQuery(queryString);
            setParameters(parameters, query);
            return query.getResultList();
        } catch (final Exception e) {
            throw new FailedToExecuteQuery(e, queryString, parameters);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeBatchQuery(final String queryString, final Map<String, Object> parameters) {
        checkInParams(queryString, parameters);

        final List<T> results = Lists.newArrayList();

        for (Map<String, Object> m: partition(parameters)) {
            final Query query = createQuery(queryString);
            setParameters(m, query);
            results.addAll(query.getResultList());
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> partition(final Map<String, Object> parameters) {
        final List<Map<String, Object>> partition = Lists.newArrayList();

        final List<Object> items = Lists.partition((List) parameters.get(IN), MAX_PARAM_COUNT);
        for (Object item: items) {
            final Map<String, Object> copy = copyParams(parameters);
            copy.put(IN, item);
            partition.add(copy);
        }

        return partition;
    }

    private Map<String, Object> copyParams(final Map<String, Object> parameters) {
        final Map<String, Object> copy = Maps.newHashMap(parameters);
        copy.remove(IN);
        return copy;
    }

    private void checkInParams(final String queryString, final Map<String, Object> parameters) {
        if (parameters == null) {
            throw new FailedToExecuteBatchQueryNoInParam(queryString);
        }

        final Object in = parameters.get(IN);
        if (in == null) {
            throw new FailedToExecuteBatchQueryNoInParam(queryString, parameters);
        }

        if (!(in instanceof Collection)) {
            throw new FailedToExecuteBatchQueryNoInParam(queryString, parameters);
        }

        if (((Collection) in).isEmpty()) {
            throw new FailedToExecuteBatchQueryNoInParam(queryString, parameters);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQuery(final String queryString) {
        try {
            return createQuery(queryString).getResultList();
        } catch (final Exception e) {
            throw new FailedToExecuteQuery(e, queryString);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQueryWithTopResults(final String queryString, final Map<String, Object> paramMap, final int numberOfResult) {
        try {
            final Query query = createQuery(queryString);
            setParameters(paramMap, query);
            query.setMaxResults(numberOfResult);
            return query.getResultList();
        } catch (final Exception e) {
            throw new FailedToExecuteQuery(e, queryString, paramMap);
        }
    }


    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQueryWithTopResults(final String queryString, final int numberOfResult) {
        try {
            final Query query = createQuery(queryString);
            query.setMaxResults(numberOfResult);
            return query.getResultList();
        } catch (final Exception e) {
            throw new FailedToExecuteQuery(e, queryString);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQueryWithPaging(final String queryString, final Map<String, Object> paramMap, final int offset, final int limit) {
        try {
            final Query query = createQuery(queryString);
            setParameters(paramMap, query);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (final Exception e) {
            throw new FailedToExecuteQuery(e, queryString, paramMap);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public <T> T findById(final Class<T> clazz, final Object id) {
        try {
            return entityManager.find(clazz, id);
        } catch (final Exception e) {
            throw new FailedToFindEntity(e, clazz, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByIds(final Class<T> clazz, final List<?> ids) {
        return ids.stream().map(id -> findById(clazz, id)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> T findFirstByQuery(final String queryString) {
        final List<?> entities = executeQuery(queryString);
        if (entities.isEmpty()) {
            return null;
        } else {
            return (T) entities.get(0);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T> T findFirstByQuery(final String queryString, final Map<String, Object> parameters) {
        final List<?> entities = executeQuery(queryString, parameters);
        if (entities.isEmpty()) {
            return null;
        } else {
            return (T) entities.get(0);
        }
    }

    private Query createQuery(final String queryString) {
        return entityManager.createQuery(queryString);
    }

    private void setParameters(final Map<String, Object> parameters, final Query query) {
        for (final Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(final Class<T> clazz) {
        return executeQuery(String.format("select e from %s e", clazz.getSimpleName()));
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}
