package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import org.neo4j.ogm.session.Session;

import eu.europa.ec.digit.contentmanagement.domain.api.access.DataConnectionObject_i;
import eu.europa.ec.digit.contentmanagement.domain.api.access.DcoWrapper_i;

/**
 * 
 * @author bentsth
 */
class DcoWrapperNeo4jImpl implements DcoWrapper_i<Session> {

    private DataConnectionObjectNeo4jImpl dco;

    DcoWrapperNeo4jImpl(DataConnectionObject_i dco) {
        if (dco == null)
            this.dco = new DataConnectionObjectNeo4jImpl(null);
        else
            this.dco = (DataConnectionObjectNeo4jImpl) dco;
    }



    @Override
    public Session getConnectionObject() {
        return dco.getSession();
    }



    @Override
    public void setConnectionObject(Session connectionObject) {
        dco.setSession(connectionObject);
    }



    @Override
    public void beginTransaction() {
        dco.getSession().beginTransaction();
    }



    @Override
    public void commitTransaction() {
        dco.getSession().getTransaction().commit();
    }



    @Override
    public void rollbackTransaction() {
        dco.getSession().getTransaction().rollback();
    }



    @Override
    public boolean isTransactionActive() {
        return dco.getSession().getTransaction() != null;
    }



    @Override
    public void close() {
        // Nothing here
    }
}
