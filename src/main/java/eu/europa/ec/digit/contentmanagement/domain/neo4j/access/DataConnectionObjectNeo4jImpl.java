package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import org.neo4j.ogm.session.Session;

import eu.europa.ec.digit.contentmanagement.domain.api.access.DataConnectionObject_i;

/**
 * 
 * @author bentsth
 */
public class DataConnectionObjectNeo4jImpl implements DataConnectionObject_i {

    private Session session;


    public DataConnectionObjectNeo4jImpl(Session session) {
        this.session = session;
    }


    @Override
    public void beginTransaction() {
        session.beginTransaction();
    }


    @Override
    public void commitTransaction() {
        if(session.getTransaction() != null)
            session.getTransaction().commit();
    }


    @Override
    public void rollbackTransaction() {
        if(session.getTransaction() != null)
            session.getTransaction().rollback();
    }


    @Override
    public void close() {
    }


    public Session getSession() {
        return session;
    }


    public void setSession(Session session) {
        this.session = session;
    }
}
