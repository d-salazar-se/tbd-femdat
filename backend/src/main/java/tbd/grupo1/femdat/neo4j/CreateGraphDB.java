package tbd.grupo1.femdat.neo4j;

import tbd.grupo1.femdat.model.TwitterUser;

import java.util.List;

class CreateGraphDB {
	

	public static void run()
	{
		// List<String> hashtags = KeyWords.findAll();
		// insertHashtags(hashtags);
	}


	public void insertTwitterUsers(TwitterUser user) {
		String cmd;

		cmd = String.format("CREATE (u:TwitterUser {id: %l, name: \"%s\", screenName: \"%s\", location: \"%s\", profileImageUrlHttps: \"%s\", followersCount: %d, friendsCount: %d, statusesCount: %d }", 
								user.getId(),
								user.getScreenName(),
								user.getLocation(),
								user.getProfileImageUrlHttps(),
								user.getFollowersCount(),
								user.getFriendsCount(),
								user.getStatusesCount());

		System.out.println("\n");
		System.out.println(cmd);
		System.out.println("\n");
	}

	public void insertTwitterUsers(List<TwitterUser> users) {
		String cmd;

		for (TwitterUser user : users) {
			cmd = String.format("CREATE (u:TwitterUser {id: %l, name: \"%s\", screenName: \"%s\", location: \"%s\", profileImageUrlHttps: \"%s\", followersCount: %d, friendsCount: %d, statusesCount: %d }", 
									user.getId(),
									user.getScreenName(),
									user.getLocation(),
									user.getProfileImageUrlHttps(),
									user.getFollowersCount(),
									user.getFriendsCount(),
									user.getStatusesCount());

			System.out.println("\n");
			System.out.println(cmd);
			System.out.println("\n");
		}
	}

	public void insertHashtag(String hashtag) {
		String cmd;

		cmd = String.format("CREATE (h:Hashtag {name: \"%s\", quantity: 0}", hashtag);

		System.out.println("\n");
		System.out.println(cmd);
		System.out.println("\n");
	}

	public void insertHashtags(List<String> hashtags) {
		String cmd;

		for (String hashtag : hashtags) {
			cmd = String.format("CREATE (h:Hashtag {name: \"%s\", quantity: 0}", hashtag);

			System.out.println("\n");
			System.out.println(cmd);
			System.out.println("\n");
		}
	}

	public void createRelation(TwitterUser user, String hashtag) {
		String cmd;

		cmd = String.format("MATCH (u:TwitterUser {screenName: \"%s\"}) (h:Hashtag {name: \"%s\"}) CREATE (u)-[r:TWITTED]->(h)", user.getScreenName(), hashtag);

		System.out.println("\n");
		System.out.println(cmd);
		System.out.println("\n");

		cmd = String.format("MERGE (h:Hashtag {name: \"%s\"} SET h.quantity += 1 RETURN h", hashtag);

		System.out.println("\n");
		System.out.println(cmd);
		System.out.println("\n");
	}

}
