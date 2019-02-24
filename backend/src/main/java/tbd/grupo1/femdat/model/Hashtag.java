package tbd.grupo1.femdat.model;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Hashtag {

    @Id
    @GeneratedValue
	private Long id;
	private String name;
	private int quantity = 0;

	@Relationship(type = "TWITTED")
	private List<TwitterUser> users = new ArrayList<>();

	public Hashtag() {
	}

	public Hashtag(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public List<TwitterUser> getTwitterUsers() {
		return this.users;
	}
}
