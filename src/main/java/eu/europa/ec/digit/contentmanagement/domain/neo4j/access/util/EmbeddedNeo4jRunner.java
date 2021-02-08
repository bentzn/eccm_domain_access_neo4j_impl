package eu.europa.ec.digit.contentmanagement.domain.neo4j.access.util;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

/**
 * 
 * @author bentsth
 */
public class EmbeddedNeo4jRunner {

    public static final String SERVER_URI = "bolt://localhost:7687";
    private static volatile File dirDatabase;
    private static volatile GraphDatabaseService graphDb;
    private static volatile SessionFactory sessionFactory;


    public static SessionFactory getSessionFactory() {
        return getSessionFactory(null, null);
    }


    public static SessionFactory getSessionFactory(String username, String password) {
        Configuration cConfiguration = null;
        if (username == null)
            cConfiguration = new Configuration.Builder().uri(SERVER_URI).build();
        else
            cConfiguration = new Configuration.Builder().uri(SERVER_URI).credentials(username, password).build();
        return new SessionFactory(cConfiguration, "eu.europa.ec.digit.contentmanagement.domain.neo4j.entities",
                "eu.europa.ec.digit.contentmanagement.domain.api.entities");
    }


    public static void startDatabase() {
        startDatabase("./database/" + System.currentTimeMillis());
    }

    public static void startDatabase(String path) {
        dirDatabase = new File(path);
        BoltConnector bolt = new BoltConnector();
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dirDatabase).setConfig(bolt.enabled, "true")
                .setConfig(bolt.type, "BOLT").setConfig(bolt.listen_address, "localhost:7687").newGraphDatabase();
        registerShutdownHook(graphDb);
    }


    public static void shutdownDatabaseAndSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
        graphDb.shutdown();
    }


    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

}
