package eu.europa.ec.digit.contentmanagement.domain.neo4j.access.util;

import static org.junit.Assert.*;
import static eu.europa.ec.digit.contentmanagement.domain.neo4j.access.util.EmbeddedNeo4jRunner.*;

import org.junit.Test;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.entities.Repository_i;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.RepositoryNeo4jImpl;

/**
 * 
 * @author bentsth
 */
public class TestEmbeddedNeo4jRunnerTestImpl {

    
    @Test
    public void test() {
        try {
            startDatabase();
            SessionFactory sessionFactory = getSessionFactory();
            Session cSession = sessionFactory.openSession();
            Repository_i repo0a = new RepositoryNeo4jImpl("some_name", "some_description", 0);
            cSession.save(repo0a);
            assertEquals("some_name", repo0a.getName());
            
            cSession.beginTransaction();
            Repository_i repo0b = cSession.load(RepositoryNeo4jImpl.class, repo0a.getId());
            assertEquals("some_name", repo0b.getName());

            cSession.delete(repo0b);
            Repository_i repo0c = cSession.load(RepositoryNeo4jImpl.class, repo0a.getId());
            assertNull(repo0c);
            
            cSession.getTransaction().commit();
            
            cSession = sessionFactory.openSession();
            Repository_i repo0d = cSession.load(RepositoryNeo4jImpl.class, repo0a.getId());
            assertNull(repo0d);
        } finally {
            shutdownDatabaseAndSessionFactory();
        }
    }
}
