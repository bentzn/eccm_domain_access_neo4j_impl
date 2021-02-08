package eu.europa.ec.digit.contentmanagement.domain.neo4j.access.specific;

import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.access.specific.RepositoryDao_i;
import eu.europa.ec.digit.contentmanagement.domain.api.entities.Repository_i;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.AbstractDaoNeo4jImpl;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.RepositoryNeo4jImpl;

/**
 * 
 * @author bentsth
 */
public class RepositoryDaoNeo4jImpl extends AbstractDaoNeo4jImpl<Repository_i, RepositoryNeo4jImpl>
        implements RepositoryDao_i<RepositoryNeo4jImpl> {

    public RepositoryDaoNeo4jImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    protected Class<Repository_i> getInterfaceOfEntity() {
        return Repository_i.class;
    }


    @Override
    protected Class<RepositoryNeo4jImpl> getImplementingClassOfEntity() {
        return RepositoryNeo4jImpl.class;
    }


    @Override
    public Repository_i getNewEntityForTest(int seed) {
        return new RepositoryNeo4jImpl("name_NEO4J_" + seed, "description_" + seed, seed);
    }
}
