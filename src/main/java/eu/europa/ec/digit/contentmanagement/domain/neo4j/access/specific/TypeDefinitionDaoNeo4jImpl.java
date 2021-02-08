package eu.europa.ec.digit.contentmanagement.domain.neo4j.access.specific;

import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.access.DataConnectionObject_i;
import eu.europa.ec.digit.contentmanagement.domain.api.access.specific.TypeDefinitionDao_i;
import eu.europa.ec.digit.contentmanagement.domain.api.entities.TypeDefinition_i;
import eu.europa.ec.digit.contentmanagement.domain.api.util.collections.PaginatedList_i;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.*;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.TypeDefinitionNeo4jImpl;

/**
 * 
 * @author bentsth
 */
public class TypeDefinitionDaoNeo4jImpl extends AbstractDaoNeo4jImpl<TypeDefinition_i, TypeDefinitionNeo4jImpl> implements TypeDefinitionDao_i<TypeDefinitionNeo4jImpl> {

    public TypeDefinitionDaoNeo4jImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }



    @Override
    protected Class<TypeDefinition_i> getInterfaceOfEntity() {
        return TypeDefinition_i.class;
    }



    @Override
    protected Class<TypeDefinitionNeo4jImpl> getImplementingClassOfEntity() {
        return TypeDefinitionNeo4jImpl.class;
    }



    @Override
    public TypeDefinitionNeo4jImpl getNewEntityForTest(int seed) {
        return new TypeDefinitionNeo4jImpl("name_NEO4J_" + seed, "displayName_" + seed, "description_" + seed);
    }



    @Override
    public PaginatedList_i<? extends TypeDefinition_i> getChildren(DataConnectionObject_i dco, String uuid, int skipItems, int maxItems) throws Exception {
        if (dco == null)
            dco = new DataConnectionObjectNeo4jImpl(sessionFactory.openSession());

        TypeDefinition_i parent = retrieve(dco, uuid);
        String cypher = "MATCH (obj:" + getDataSourceNameOfEntity() + ") - [:HAS_PARENT] - (parent:" + getDataSourceNameOfEntity() + " {uuid:'" + parent.getUuid()
                + "'}) RETURN obj " + getSkipLimitString(skipItems, maxItems) + ";";
        String cypherForTotal = "MATCH (obj:" + getDataSourceNameOfEntity() + ") - [:HAS_PARENT] - (parent:" + getDataSourceNameOfEntity() + " {uuid:'" + parent.getUuid()
                + "'}) RETURN COUNT(obj);";

        return list(dco, skipItems, maxItems, cypher, cypherForTotal);
    }

}
