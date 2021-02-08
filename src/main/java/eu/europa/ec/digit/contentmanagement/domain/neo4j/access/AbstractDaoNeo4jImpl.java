package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.access.*;
import eu.europa.ec.digit.contentmanagement.domain.api.entities.AbstractEntity_i;
import eu.europa.ec.digit.contentmanagement.domain.api.util.collections.PaginatedListImpl;
import eu.europa.ec.digit.contentmanagement.domain.api.util.collections.PaginatedList_i;

/**
 * 
 * @author bentsth
 */
public abstract class AbstractDaoNeo4jImpl<INTERFACE extends AbstractEntity_i, IMPLEMENTATION extends INTERFACE>
        extends AbstractDao<Session, INTERFACE, IMPLEMENTATION> implements EntityDao_i<INTERFACE, IMPLEMENTATION> {

    private static final Logger logger = Logger.getLogger(AbstractDaoNeo4jImpl.class);
    protected SessionFactory sessionFactory;


    protected AbstractDaoNeo4jImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    protected abstract Class<INTERFACE> getInterfaceOfEntity();


    protected abstract Class<IMPLEMENTATION> getImplementingClassOfEntity();


    protected String getDataSourceNameOfEntity(Class<?> clazz) {
        for (Annotation ann : clazz.getDeclaredAnnotations()) {
            if (ann instanceof NodeEntity)
                return ((NodeEntity) ann).value();
        }
        
        return null;
    }


    @Override
    protected Session createConnection() {
        return sessionFactory.openSession();
    }


    @Override
    public DataConnectionObjectNeo4jImpl openNewDco() {
        return new DataConnectionObjectNeo4jImpl(sessionFactory.openSession());
    }


    @Override
    protected DcoWrapper_i<Session> wrap(DataConnectionObject_i dco) {
        return new DcoWrapperNeo4jImpl(dco);
    }


    protected void createEntity(Session connectionObject, INTERFACE entity) throws Exception {
        if (entity.getId() > 0)
            throw new Exception("Can't create entity that already has an id");

        connectionObject.save(entity);
    }


    @Override
    protected INTERFACE retrieveEntity(Session connectionObject, long id) {
        return connectionObject.load(getImplementingClassOfEntity(), id);
    }


    @Override
    protected INTERFACE retrieveEntity(Session connectionObject, String uuid) throws Exception {
        String query = "MATCH (obj:" + getDataSourceNameOfEntity() + "{uuid:'" + uuid + "'}) RETURN obj;";

        if (logger.isDebugEnabled())
            logger.debug("retrieveEntity, query: " + query);
        
        Iterable<IMPLEMENTATION> result = connectionObject.query(getImplementingClassOfEntity(), query, new HashMap<>());

        for (IMPLEMENTATION impl : result) {
            if (logger.isDebugEnabled())
                logger.debug("Found entity on UUID: " + uuid + ", entity: " + impl);

            return impl;
        }
        
        if (logger.isDebugEnabled())
            logger.debug("Entity not found on UUID");

        return null;
    }


    @Override
    protected void updateEntity(Session connectionObject, INTERFACE entity) {
        connectionObject.save(entity);
    }


    @Override
    protected void deleteEntity(Session connectionObject, INTERFACE entity) {
        connectionObject.delete(entity);
    }

    
    protected String getSkipLimitString(int skipItems, int maxItems) {
        return "SKIP " + skipItems + " LIMIT " + maxItems;
    }

    protected PaginatedList_i<IMPLEMENTATION> listEntities(Session connectionObject, int skipItems, int maxItems)
            throws Exception {
        String query = "MATCH (obj:" + getDataSourceNameOfEntity() + ") RETURN obj " + getSkipLimitString(skipItems, maxItems) + ";";
        Iterable<IMPLEMENTATION> result = connectionObject.query(getImplementingClassOfEntity(), query, new HashMap<>());
        List<IMPLEMENTATION> coll = new LinkedList<>();
        for (IMPLEMENTATION impl : result) {
            coll.add(impl);
        }

        return new PaginatedListImpl<>(coll, skipItems, maxItems, countEntities(connectionObject));
    }



    public PaginatedList_i<IMPLEMENTATION> list(DataConnectionObject_i dco, int skipItems, int maxItems, String query, String queryForTotalCount) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("list (" + getTypeName() + "), query: " + query);

        AtomicBoolean entityManagerCreated = new AtomicBoolean(false);
        DcoWrapper_i<Session> dcoWrapper = wrap(dco);
        initConnectionAndTransaction(dcoWrapper, entityManagerCreated, null);

        try {
            Iterable<IMPLEMENTATION> result = dcoWrapper.getConnectionObject().query(getImplementingClassOfEntity(), query, new HashMap<>());
            List<IMPLEMENTATION> coll = new LinkedList<>();
            for (IMPLEMENTATION impl : result) {
                coll.add(impl);
            }
            
            // Get total count
            int totalItems = 0;
            if(coll.size() == maxItems || coll.size() == 0) {
                // Page is full or has nothing at all, we have to query
                Iterable<Integer> totalItemsResult = dcoWrapper.getConnectionObject().query(Integer.class, queryForTotalCount, new HashMap<>());
                for (Integer cnt : totalItemsResult) {
                    totalItems = cnt;
                    break;
                }
            }
            else {
                // total items can be calculated
                totalItems = skipItems + coll.size();
            }
            
            PaginatedList_i<IMPLEMENTATION> lst = new PaginatedListImpl<>(coll, skipItems, maxItems, totalItems);

            if (logger.isDebugEnabled())
                logger.debug("list (" + getTypeName() + "), returning: " + lst.size() + " records");

            return lst;
        } finally {
            closeEntityManager(dcoWrapper, entityManagerCreated);
        }
    }


    protected long countEntities(Session connectionObject) {
        return connectionObject.countEntitiesOfType(getImplementingClassOfEntity());
    }
}
