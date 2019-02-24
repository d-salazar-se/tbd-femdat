package tbd.grupo1.femdat.repositories;

import java.util.Collection;
import tbd.grupo1.femdat.model.TwitterUser;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "twitterUsers", path = "graph")
public interface GraphRepository extends Neo4jRepository<TwitterUser, Long> {
    @Query("MATCH (u:TwitterUser)-[r:TWITTED]->(h:Hashtag) RETURN u,r,h")
	Collection<TwitterUser> graph();
}