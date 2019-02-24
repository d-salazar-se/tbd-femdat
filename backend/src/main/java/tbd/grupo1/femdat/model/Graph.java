package tbd.grupo1.femdat.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import tbd.grupo1.femdat.repositories.GraphRepository;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Graph {

	@Autowired
	private static GraphRepository graphRepository;

	public static Map<String, Object> toD3Format(Collection<TwitterUser> twitterUsers) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<TwitterUser> result = twitterUsers.iterator();
		while (result.hasNext()) {
			TwitterUser twitterUser = result.next();
			int sid = nodes.size();
			nodes.add(map("id", sid, "name", "@"+twitterUser.getScreenName(), "label", "Twitter User", "_color", "cyan"));
			int target = i;
			i++;
			for (Hashtag hashtag : twitterUser.getHashtags()) {
				Map<String, Object> hash = map("name", hashtag.getName(), "label", "Hashtag", "_size", 20+(1.5*hashtag.getQuantity()));
				int source = nodes.indexOf(hash);
				if (source == -1) {
					nodes.add(hash);
					source = i++;
				}
				rels.add(map("sid", source, "tid", target));
			}
		}
		return map("nodes", nodes, "links", rels);
	}

	private static Map<String, Object> map(Object... args) {
		Map<String, Object> result = new HashMap<String, Object>(args.length);

		for (int i = 0; i < args.length; i++) {
			result.put(args[i].toString(), args[++i]);
		}

		return result;
	}

	public Map<String, Object> graph() {
		Collection<TwitterUser> result = graphRepository.graph();
		return toD3Format(result);
	}	
}