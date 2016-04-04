package br.com.erudio.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import br.com.erudio.model.Model;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

public abstract class GenericService<T extends Model<PK>, PK extends Serializable>
        implements Serializable {

    protected static final int LOTE = 5000;

    private static final long serialVersionUID = 1L;

    private final Class<T> type;

    @PersistenceContext
    private EntityManager em;

    protected GenericService() {
        this.type = getGenericClass();
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass() {
        Class<T> result = null;
        Type type = this.getClass().getGenericSuperclass();

        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] fieldArgTypes = pt.getActualTypeArguments();
            result = (Class<T>) fieldArgTypes[0];
        }
        return result;
    }

    public Class<T> getType() {
        return type;
    }

    public List<T> findAll() {
        final Criteria criteria = getCriteria(getType().getSimpleName() + ".findAll");
        addDefaultOrder(criteria);

        @SuppressWarnings("unchecked")
        List<T> list = criteria.list();
        return list;
    }

    protected void addDefaultOrder(Criteria criteria) {
    }

    public List<T> find(Integer firstResult, Integer maxResults) {
        return find(getCriteria(), firstResult, maxResults);
    }

    public List<T> find(Criteria criteria, Integer firstResult,
                        Integer maxResults) {
        paginateCriteria(firstResult, maxResults, criteria);

        return listByCriteria(criteria);
    }

    private void paginateCriteria(Integer firstResult, Integer maxResults,
                                  Criteria criteria) {
        if (firstResult != null) {
            criteria.setFirstResult(firstResult);
        }

        if (maxResults != null) {
            criteria.setMaxResults(maxResults);
        }
    }

    public T findById(PK pk) {
        return getEntityManager().find(type, pk);
    }

    public void remove(PK id) {
        remove(getEntityManager().getReference(type, id));
    }

    public void remove(T obj) {
        getEntityManager().remove(obj);
    }

    public void delete(T obj) {
        remove(obj);
    }

    public void remove(Collection<PK> ids) {
        int op = 1;
        for (PK id : ids) {
            remove(id);
            if (op++ % LOTE == 0) {
                flush();
                clear();
            }
        }
    }

    public T merge(T obj) {
        return getEntityManager().merge(obj);
    }

    public void refresh(T obj) {
        getEntityManager().refresh(obj);
    }

    public T save(T obj) {

        if (obj.getId() == null) {
            getEntityManager().persist(obj);
        } else {
            obj = getEntityManager().merge(obj);
        }
        return obj;
    }

    public Session getSession() {
        return ((Session) getEntityManager().getDelegate());
    }

    public Criteria getCriteria() {
        return getCriteria(null);
    }

    public Criteria getCriteria(String cacheRegion) {
        Criteria criteria = getSession().createCriteria(type);

        if (!Strings.isNullOrEmpty(cacheRegion)) {
            criteria.setCacheable(true);
            criteria.setCacheRegion(cacheRegion);
        }
        return criteria;
    }

    //Pra criar hql com cache
    public Query getHibernateQuery(String query) {
        return getSession().createQuery(query);
    }

    public List<T> listByCriteria(Criteria criteria, Integer firstResult,
                                  Integer maxResults) {
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        paginateCriteria(firstResult, maxResults, criteria);

        @SuppressWarnings("unchecked")
        final List<T> ret = criteria.list();
        return ret;
    }

    public List<T> listByCriteria(Criteria criteria) {
        @SuppressWarnings("unchecked")
        final List<T> ret = criteria.list();
        return ret;
    }

    public T uniqueByCriteria(Criteria criteria) {
        final List<T> ret = listByCriteria(criteria);
        if (ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    public List<T> findByCriteria(Collection<Criterion> criteria) {
        Session session = getSession();
        Criteria crit = session.createCriteria(type);
        for (Criterion c : criteria) {
            crit.add(c);
        }
        return listByCriteria(crit);
    }

    public Long count() {
        Criteria criteria = getCriteria();

        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    public Long countByCriteria(Criteria criteria) {

        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    public List<T> findByCriteria(Criterion... criterion) {
        return findByCriteria(Lists.newArrayList(criterion));
    }

    public T findByCriteriaSingleResultOrNull(Criterion... criterion) {
        final List<T> ret = findByCriteria(criterion);
        if (!ret.isEmpty()) {
            return ret.get(0);
        }
        return null;
    }

    public T findByTypedQuerySingleResultOrNull(TypedQuery<T> query) {
        final List<T> ret = query.getResultList();
        if (!ret.isEmpty()) {
            return ret.get(0);
        }
        return null;
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void clear() {
        getEntityManager().clear();
    }

}