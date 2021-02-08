package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import org.junit.*;

import eu.europa.ec.digit.contentmanagement.domain.api.access.DaoModule_i;
import eu.europa.ec.digit.contentmanagement.domain.api.access.TestDaosAndDataModel;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.util.EmbeddedNeo4jRunner;

/**
 * 
 * @author bentsth
 */
public class TestDaosAndDataModelNeo4jImpl extends TestDaosAndDataModel{
    
    private static DaoModule_i daoModule;
    
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        EmbeddedNeo4jRunner.startDatabase();
        daoModule = new DaoModuleNeo4jImpl();
        daoModule.init();
    }
    
    
    @AfterClass
    public static void afterClass() {
        EmbeddedNeo4jRunner.shutdownDatabaseAndSessionFactory();
    }
    

    @Test
    public void test() throws Exception {
        run(daoModule);
    }
}
