package eu.europa.ec.digit.contentmanagement.domain.neo4j.access;

import eu.europa.ec.digit.contentmanagement.domain.api.EccmDomainConstants;

/**
 * 
 * @author bentsth
 */
public class EccmConstantsNeo4jImpl extends EccmDomainConstants{

    public static final String PROP_NAME_DB_PATH = "db_path";
    public static final String PROP_NAME_DB_URL = "db_url";
    public static final String PROP_NAME_DB_USERNAME = "db_username";
    public static final String PROP_NAME_DB_PASSWORD = "db_password";
    
    public static final String PROP_DEFAULT_DB_PATH = "./database/db";
    public static final String PROP_DEFAULT_DB_URL = "bolt://localhost:7687";
    public static final String PROP_DEFAULT_DB_USERNAME = null;
    public static final String PROP_DEFAULT_DB_PASSWORD = null;
}
