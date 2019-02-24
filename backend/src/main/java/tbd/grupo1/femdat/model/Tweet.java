package tbd.grupo1.femdat.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import com.fasterxml.jackson.annotation.JsonInclude;
import twitter4j.User;

import tbd.grupo1.femdat.sentimentanalysis.Classifier;
import tbd.grupo1.femdat.model.Data;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.IOException;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tweet {
	@Id
    private Long id;
    private Date createdAt;
    private String text;
    private int retweetCount;
    private int favoriteCount;
	private TwitterUser user;
	private String country;

	@Autowired
	private static MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost"), "femdat");

	// @Autowired
	// public static Classifier classifier;
	public Tweet() {}

	public Tweet(Long id, Date createdAt, String text, int retweetCount, int favoriteCount, User user, String country) {
		this.id = id;
		this.createdAt = createdAt;
		this.text = text;
		this.retweetCount = retweetCount;
		this.favoriteCount = favoriteCount;
		this.user = new TwitterUser(user);
		this.country = country;
	}

	public static List<Tweet> findByCreatedAt(Date date) {
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		date = calendar.getTime();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date nextDay = calendar.getTime();
        
        Query query = new Query();
		query.addCriteria(Criteria.where("createdAt").gt(date).lt(nextDay));

		List<Tweet> tweets = mongoTemplate.find(query, Tweet.class, "tweet");

		return tweets;
	}

	public static long findWordByCreatedAt(String word, Date date) {
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		date = calendar.getTime();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date nextDay = calendar.getTime();
        
        Query query = new Query();
		query.addCriteria(Criteria.where("createdAt").gt(date).lt(nextDay));

		long totalTweets = mongoTemplate.count(query, Tweet.class, "tweet");

		if (totalTweets == 0) {
			return 0;
		}

		query.addCriteria(Criteria.where("text").regex(".*"+word+".*"));
		System.out.println(query);
		long totalWord = mongoTemplate.count(query, Tweet.class, "tweet");

		return (totalWord*100/totalTweets);
	}

	public static Map<String, Float> summaryByApreciation(TweetIndex femdatIndex, ResourceLoader resourceLoader, Classifier classifier){
		Map<String, Float> summary = new TreeMap<String, Float>();

		HashMap<String,Double> classification;
		float good = 0, bad = 0;
		long totalTweetsWithWord = 0;

		long totalTweetsInDB = mongoTemplate.getCollection("tweet").count();
		
		try {
			Resource resource = new ClassPathResource("bagOfWords.txt");
			System.out.println(resource.getFile());
			Scanner sc = new Scanner(resource.getFile());

			String currentWord;
			while (sc.hasNextLine()) {
				currentWord = sc.nextLine();

				classification = classifier.classify(currentWord);

				// http://localhost:9200/femdat/tweet/_count?q=text:currentWord
				totalTweetsWithWord = femdatIndex.count(currentWord);

				good += classification.get("positive")*(totalTweetsWithWord);
				bad += classification.get("negative")*(totalTweetsWithWord);
			}
			
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		summary.put("good", new Float(good));
		summary.put("bad", new Float(bad));

		return summary;
	}

	public static List<Tweet> topTweets() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "retweetCount"));
		query.fields().include("_id");
		query.fields().include("user");
		query.fields().include("text");

		List<Tweet> tweets = mongoTemplate.find(query, Tweet.class, "tweet");
		List<Tweet> topTweets = new ArrayList<Tweet>();
		int ingresados = 0;

		for (Tweet tweet : tweets) {
			topTweets.add(tweet);
			ingresados++;

			if (ingresados == 10) {
				break;
			}
		}

		return topTweets;
	}

	public static List<TwitterUser> topFigures() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "user.followersCount"));
		query.fields().include("_id");
		query.fields().include("user");

		List<Tweet> tweets = mongoTemplate.find(query, Tweet.class, "tweet");
		List<TwitterUser> topFigures = new ArrayList<TwitterUser>();

		int ingresados = 0;
		long lastinsertedId = -1;

		for (Tweet tweet : tweets) {
			if (topFigures.size() == 0 || tweet.user.getId() != lastinsertedId) {
				lastinsertedId = tweet.user.getId();
				topFigures.add(tweet.user);
				ingresados++;

				if (ingresados == 5) {
					break;
				}
			}
		}

		return topFigures;
	}
	
	public static List<Data> topTopics(TweetIndex femdatIndex, ResourceLoader resourceLoader) {
        List<Data> wordFrecuency = new ArrayList<Data>();

		long totalTweetsWithWord = 0;
		
		try {
			Resource resource = new ClassPathResource("bagOfWords.txt");
			Scanner sc = new Scanner(resource.getFile());

			String currentWord;
			while (sc.hasNextLine()) {
				currentWord = sc.nextLine();

				// http://localhost:9200/femdat/tweet/_count?q=text:currentWord
				totalTweetsWithWord = femdatIndex.count(currentWord);

				wordFrecuency.add(new Data(currentWord, new Long(totalTweetsWithWord)));
			}
			
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(wordFrecuency, new SortByQuantity());

		List<Data> topTopics = new ArrayList<Data>();
		int i;
		for (i = 0; i < 5; i++) {
			topTopics.add(wordFrecuency.get(i));
		}

		return topTopics;
	}

	public static List<Data> countriesActivity(TweetIndex femdatIndex) {
        List<Data> countries = new ArrayList<Data>();

        List<String> countriesNames = new ArrayList<String>();
        countriesNames.add("Argentina");
		countriesNames.add("Brasil");
		countriesNames.add("Bolivia");
		countriesNames.add("Chile");
		countriesNames.add("Colombia");
		countriesNames.add("Ecuador");
		countriesNames.add("Paraguay");
		countriesNames.add("Peru");
		countriesNames.add("Uruguay");
		countriesNames.add("Venezuela");
		countriesNames.add("España");

        Long totalTweetsForCountry;
        for (String country : countriesNames) {
	        Query query = new Query();
			query.addCriteria(Criteria.where("country").is(country));

			totalTweetsForCountry = mongoTemplate.count(query, "tweet");
        	
        	countries.add(new Data(country, totalTweetsForCountry));
		}

		return countries;
	}

	public static List<Data> countriesAfinity(TweetIndex femdatIndex, Classifier classifier) {
		List<Data> countries = new ArrayList<Data>();

        List<String> countriesNames = new ArrayList<String>();
        countriesNames.add("Argentina");
		countriesNames.add("Brasil");
		countriesNames.add("Bolivia");
		countriesNames.add("Chile");
		countriesNames.add("Colombia");
		countriesNames.add("Ecuador");
		countriesNames.add("Paraguay");
		countriesNames.add("Peru");
		countriesNames.add("Venezuela");
		countriesNames.add("Uruguay");
		countriesNames.add("España");

		HashMap<String,Double> classification;
        int totalTweetsForCountry, countryAfinity;
        for (String country : countriesNames) {
        	countryAfinity = 0;
	        Query query = new Query();
			query.addCriteria(Criteria.where("country").is(country));

			List<Tweet> tweets = mongoTemplate.find(query, Tweet.class, "tweet");

			totalTweetsForCountry = tweets.size();

			for (Tweet tweet : tweets) {
        		classification = classifier.classify(tweet.getText());
        		countryAfinity += 100 * classification.get("positive");
			}

        	countries.add(new Data(country, new Long(countryAfinity/totalTweetsForCountry)));
		}

		return countries;
	}

	public static List<Data> institutionsActivity(TweetIndex femdatIndex) {
		String[][] institutions = new String[][] {
			{"Centro Indisciplinario de Estudio de Género", "CIEGUCHILE"},
			{"Red Chilena Contra la Violencia Doméstica y Sexual", "MujeresRed"},
			{"Corporación Humanas", "corphumanas"},
			{"Centro de Estudios de Género y Cultura en América Latina", "cegecal"},
			{"Instituto Nacional de Derechos Humanos ", "inddhh"}
		};

		List<Data> activity = new ArrayList<Data>();

		for (int i = 0; i < institutions.length; i++) {
			Long quantity = femdatIndex.count(institutions[i][1]) + femdatIndex.count("@"+institutions[i][1]);

			// activity.add(new Data(institutions[i][0] + " (@"+institutions[i][1]+")", quantity));
			activity.add(new Data("@"+institutions[i][1], quantity));
		}

		return activity;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public int getRetweetCount() {
		return retweetCount;
	}
	
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}
	
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public TwitterUser getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = new TwitterUser(user);
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public static Map<String, Double> sentimentAnalysisLastWeek(Classifier classifier){
		Map<String, Double> summary = new TreeMap<String, Double>();

		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();

		int days;
		Double positive;
		String dateAsString;

		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		for (days = -7; days <= 0; days++) {
			calendar.add(Calendar.DATE, 1);

			date = calendar.getTime();

			dateAsString = df.format(date);
			// quantity = repository.findByCreatedAt(dateAsString).size();	// malo, filtra por timestamp
			positive = 100*getPositiveTweets(classifier, Tweet.findByCreatedAt(date));

			summary.put(dateAsString, positive);
		}

		return summary;
	}

	public static Double getPositiveTweets(Classifier classifier, List<Tweet> tweets){
		Double sum = 0.0;
		for (Tweet tweet: tweets) {
			sum = sum + classifier.classify(tweet.getText()).get("positive");
		}
		return sum/tweets.size();
	}
}