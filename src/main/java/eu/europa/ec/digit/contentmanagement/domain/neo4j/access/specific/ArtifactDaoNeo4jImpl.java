package eu.europa.ec.digit.contentmanagement.domain.neo4j.access.specific;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.session.SessionFactory;

import eu.europa.ec.digit.contentmanagement.domain.api.access.DataConnectionObject_i;
import eu.europa.ec.digit.contentmanagement.domain.api.access.specific.ArtifactDao_i;
import eu.europa.ec.digit.contentmanagement.domain.api.entities.Artifact_i;
import eu.europa.ec.digit.contentmanagement.domain.api.util.collections.PaginatedList_i;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.AbstractDaoNeo4jImpl;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.access.DataConnectionObjectNeo4jImpl;
import eu.europa.ec.digit.contentmanagement.domain.neo4j.entities.impl.ArtifactNeo4jImpl;

/**
 * 
 * @author bentsth
 */
public class ArtifactDaoNeo4jImpl extends AbstractDaoNeo4jImpl<Artifact_i, ArtifactNeo4jImpl>
        implements ArtifactDao_i<ArtifactNeo4jImpl> {

    public ArtifactDaoNeo4jImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    protected Class<Artifact_i> getInterfaceOfEntity() {
        return Artifact_i.class;
    }


    @Override
    protected Class<ArtifactNeo4jImpl> getImplementingClassOfEntity() {
        return ArtifactNeo4jImpl.class;
    }


    @Override
    public Artifact_i getNewEntityForTest(int seed) {
        return new ArtifactNeo4jImpl("name_NEO4J_" + seed, null);
    }


    @Override
    public PaginatedList_i<? extends Artifact_i> getChildren(DataConnectionObject_i dco, long id, int skipItems,
            int maxItems) throws Exception {

        if (dco == null)
            dco = new DataConnectionObjectNeo4jImpl(sessionFactory.openSession());
        
        Artifact_i parent = retrieve(id);
        String cypher = "MATCH (obj:Artifact) - [:CHILD_OF] -> (parent:Artifact {uuid:'" + parent.getUuid() + "'}) RETURN obj " + getSkipLimitString(skipItems, maxItems) + ";";
        String cypherForTotal = "MATCH (obj:Artifact) - [:CHILD_OF] -> (parent:Artifact {uuid:'" + parent.getUuid() + "'}) RETURN COUNT(obj);";
        return list(dco, skipItems, maxItems, cypher, cypherForTotal);
    }


    @Override
    public Set<Artifact_i> getParents(DataConnectionObject_i dco, long id) throws Exception {
        boolean dcoCreated = false;
        if (dco == null) {
            dco = openNewDco();
            dcoCreated = true;
        }

        try {
            Artifact_i child = retrieve(id);
            String cypher = "MATCH (child:Artifact {uuid:'" + child.getUuid() + "'}) - [:CHILD_OF] -> (parent:Artifact) RETURN parent;";
            String cypherForTotal = "MATCH (child:Artifact {uuid:'" + child.getUuid() + "'}) - [:CHILD_OF] -> (parent:Artifact) RETURN COUNT(parent);";
            return new HashSet<>(list(dco, 0, Integer.MAX_VALUE, cypher, cypherForTotal));
        } finally {
            if (dcoCreated)
                dco.close();
        }
    }


    @Override
    public Set<Artifact_i> getParentsAll(DataConnectionObject_i dco, long id) throws Exception {
        boolean dcoCreated = false;
        if (dco == null) {
            dco = openNewDco();
            dcoCreated = true;
        }

        try {
            Artifact_i child = retrieve(id);
            String cypher = "MATCH (child:Artifact {uuid:'" + child.getUuid() + "'}) - [:CHILD_OF*] -> (parent:Artifact) RETURN parent;";
            String cypherForTotal = "MATCH (child:Artifact {uuid:'" + child.getUuid() + "'}) - [:CHILD_OF*] -> (parent:Artifact) RETURN COUNT(parent);";
            return new HashSet<>(list(dco, 0, Integer.MAX_VALUE, cypher, cypherForTotal));
        } finally {
            if (dcoCreated)
                dco.close();
        }
    }
}
