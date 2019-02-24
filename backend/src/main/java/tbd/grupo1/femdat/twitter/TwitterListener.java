package tbd.grupo1.femdat.twitter;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import tbd.grupo1.femdat.model.Tweet;
import tbd.grupo1.femdat.model.TweetIndex;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

@Service
@Configurable
public class TwitterListener {
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private TwitterStream twitterStream;
	
	@Autowired
	private MongoTemplate mongo;

	@Autowired
    private TweetIndex femdatIndex;

	@PostConstruct
	public void run() {
		twitterStream.addListener(new StatusListener() {
			public void onStatus(Status status) {
				// System.out.println("\n\n[ID] => "+status);

				Tweet tweet = new Tweet(
					status.getId(),
					status.getCreatedAt(),
					status.getText(),
					status.getRetweetCount(),
					status.getFavoriteCount(),
					status.getUser(),
					status.getPlace().getCountry()
				);

    			// ESTA COSA LOS AGREGA A ElasticSearch
				femdatIndex.insertTweet(tweet);
    			// ESTA COSA LOS AGREGA A MONGO
				mongo.insert(tweet);
			}

			@Override
			public void onException(Exception arg0) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
			}
		});

		String[] bag = null;
		
		try {
			Resource resource = resourceLoader.getResource("classpath:bagOfWords.txt");
			Scanner sc = new Scanner(resource.getFile());
			List<String> lines = new ArrayList<String>();
			
			while (sc.hasNextLine()) {
			  lines.add(sc.nextLine());
			}
			
			bag = lines.toArray(new String[0]);
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FilterQuery filter = new FilterQuery();
		filter.track(bag);
		filter.language(new String[]{"es"});
		filter.follow(new long[]{
			1615325473, // CIEGUCHILE
			17647946, 	// mujeresenred
			73459894, 	// corphumanas
			1567314756,	// cegecal
			183315665, 	// inddhh
			249112212,	// @feminicidio
			24927782	// @ciudaddemujeres
		});
		twitterStream.filter(filter);
	}

	public TwitterStream getTwitterStream() {
		return twitterStream;
	}

	public void setTwitterStream(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}

	public MongoTemplate getMongo() {
		return mongo;
	}

	public void setMongo(MongoTemplate mongo) {
		this.mongo = mongo;
	}
}