package tbd.grupo1.femdat.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import twitter4j.User;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity
public class TwitterUser {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String screenName;
	private String location;
	private String profileImageUrlHttps;
	private int followersCount;
	private int friendsCount;
	private int statusesCount;

	@JsonIgnoreProperties("twitter_user")
	@Relationship(type = "TWITTED", direction = Relationship.OUTGOING)
	private List<Hashtag> hashtags;

	@Autowired
	private static MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost"), "femdat");

	public TwitterUser() {}

	public TwitterUser(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.screenName = user.getScreenName();
		this.location = user.getLocation();
		this.profileImageUrlHttps = user.getProfileImageURL().toString();
		this.followersCount = user.getFollowersCount();
		this.friendsCount = user.getFriendsCount();
		this.statusesCount = user.getStatusesCount();
	}

	public TwitterUser(long id, String name, String screenName, String location, String profileImageUrlHttps, int followersCount, int friendsCount, int statusesCount) {
		this.id = id;
		this.name = name;
		this.screenName = screenName;
		this.location = location;
		this.profileImageUrlHttps = profileImageUrlHttps;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.statusesCount = statusesCount;
	}

	public long getId(){
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName(){
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation(){
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProfileImageUrlHttps(){
		return this.profileImageUrlHttps;
	}

	public void setProfileImageUrlHttps(String profileImageUrlHttps) {
		this.profileImageUrlHttps = profileImageUrlHttps;
	}

	public int getFollowersCount(){
		return this.followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount(){
		return this.friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getStatusesCount(){
		return this.statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public List<Hashtag> getHashtags() {
		return this.hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public void addHashtag(String hashtag) {
		if (this.hashtags == null) {
			this.hashtags = new ArrayList<Hashtag>();
		}

		this.hashtags.add(new Hashtag(hashtag));
	}

	public void addHashtag(Hashtag hashtag) {
		if (this.hashtags == null) {
			this.hashtags = new ArrayList<Hashtag>();
		}
		this.hashtags.add(hashtag);
	}
}