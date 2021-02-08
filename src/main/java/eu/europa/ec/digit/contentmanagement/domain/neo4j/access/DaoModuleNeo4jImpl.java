package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import static eu.europa.ec.digit.contentmanagement.domain.neo4j.access.EccmConstantsNeo4jImpl.*;

import java.util.*;

import org.apache.log4j.Logger;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.access.AbstractDaoModule;
import eu.europa.ec.digit.contentmanagement.domain.api.access.EntityDao_i;
import eu.europa.ec.digit.contentmanagement.domain.api.access.specific.*;
import eu.europa.ec.digit.contentmanagement.domain.api.query.EccmQuery;
import eu.europa.ec.digit.contentmanagement.domain.api.query.ResultElement;
import eu.europa.ec.digit.contentmanagement.domain.api.util.EccmUtils;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.specific.*;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.RepositoryNeo4jImpl;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.TypeDefinitionNeo4jImpl;
import eu.europa.ec.digit.contentmanagement.exception.UnimplementedException;

/**
 * 
 * @author bentsth
 */
public class DaoModuleNeo4jImpl extends AbstractDaoModule {

    private static final Logger logger = Logger.getLogger(DaoModuleNeo4jImpl.class);
    protected SessionFactory sessionFactory;


    public DaoModuleNeo4jImpl() {
    }


    @Override
    protected void initSub() throws Exception {
        Properties props = EccmUtils.readEccmPropsFromClasspath();
        String dbUrl = props.getProperty(PROP_NAME_DB_URL, PROP_DEFAULT_DB_URL);
        String dbUsername = props.getProperty(PROP_NAME_DB_USERNAME);
        String dbPassword = props.getProperty(PROP_NAME_DB_PASSWORD);

        if (logger.isDebugEnabled()) {
            logger.debug("DB props:");
            logger.debug("    dbUrl:      " + dbUrl);
            logger.debug("    dbUsername: " + dbUsername);
            logger.debug("    dbPassword: " + dbPassword);
        }

        Configuration cConfiguration = null;
        if (dbUsername == null)
            cConfiguration = new Configuration.Builder().uri(dbUrl).build();
        else
            cConfiguration = new Configuration.Builder().uri(dbUrl).credentials(dbUsername, dbPassword).build();

        sessionFactory = new SessionFactory(cConfiguration,
                "eu.europa.ec.digit.contentmanagement.domain.neo4j.entities",
                "eu.europa.ec.digit.contentmanagement.domain.api.entities");

        if (logger.isDebugEnabled())
            logger.debug("Created session factory");

    }


    @Override
    public void closeSub() {
        if (sessionFactory != null) {
            if (logger.isDebugEnabled())
                logger.debug("Closing entityManagerFactory...");
            sessionFactory.close();
        }
    }


    @Override
    public List<EntityDao_i<?, ?>> getDaos() {
        List<EntityDao_i<?, ?>> lst = new LinkedList<>();
        lst.add(getRepositoryDao());
        lst.add(getTypeDefinitionDao());
        lst.add(getArtifactDao());
        return lst;
    }


    @Override
    public RepositoryDao_i<RepositoryNeo4jImpl> getRepositoryDao() {
        return new RepositoryDaoNeo4jImpl(sessionFactory);
    }


    @Override
    public TypeDefinitionDao_i<TypeDefinitionNeo4jImpl> getTypeDefinitionDao() {
        return new TypeDefinitionDaoNeo4jImpl(sessionFactory);
    }


    @Override
    public ArtifactDao_i<?> getArtifactDao() {
        return new ArtifactDaoNeo4jImpl(sessionFactory);
    }


    @Override
    public <T> List<T> executeQuery(Class<T> clazz, EccmQuery query) {
        // TODO Auto-generated method stub
        throw new UnimplementedException();
    }


    @Override
    public List<ResultElement> executeQuery(EccmQuery query) {
        // TODO Auto-generated method stub
        throw new UnimplementedException();
    }
}
