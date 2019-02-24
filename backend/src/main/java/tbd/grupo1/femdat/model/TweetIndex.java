package tbd.grupo1.femdat.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.profile.ProfileResult;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import tbd.grupo1.femdat.repositories.TweetRepository;
import tbd.grupo1.femdat.sentimentanalysis.Classifier;
import tbd.grupo1.femdat.sentimentanalysis.SentimentAnalysisController;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.*;
import java.net.InetAddress;

@Repository
public class TweetIndex {
	private final String INDEX = "femdat";
	private final String TYPE = "tweet";
	private RestHighLevelClient restHighLevelClient;
	private ObjectMapper objectMapper;
	@Autowired
	private TweetRepository tweetRepository;
	@Autowired
	Classifier classifier;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private MongoTemplate mongoTemplate;


	public TweetIndex(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	public Tweet insertTweet(Tweet tweet) {
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, tweet.getId().toString()).source(objectMapper.convertValue(tweet, Map.class));

		try {
			restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
		return tweet;
	}

	public Map<String, Object> getTweetById(String id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		return sourceAsMap;
	}

	public Map<String, Object> updateTweetById(String id, Tweet tweet) {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id).fetchSource(true);
		Map<String, Object> error = new HashMap<>();
		error.put("Error", "Unable to update tweet");
		try {
			String tweetJson = objectMapper.writeValueAsString(tweet);
			updateRequest.doc(tweetJson, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
			Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
			return sourceAsMap;
		} catch (JsonProcessingException e) {
			e.getMessage();
			System.out.println(e);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		return error;
	}

	public void deleteTweetById(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		try {
			restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
	}

	public long count(String word) {
		long total = 0;
		try {
			SearchRequest searchRequest = new SearchRequest(INDEX); 
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
			searchSourceBuilder.query(QueryBuilders.termQuery("text", word)); 
			searchSourceBuilder.fetchSource(false);
			searchSourceBuilder.size(0);
			searchRequest.source(searchSourceBuilder); 

			SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			
			SearchHits hits = response.getHits();
			total = hits.getTotalHits();
			// System.out.println("\n[INFO] La palabra "+word+" se encuentra en " + total + " tweets\n");
		} catch(Exception e) {
			return 0;	
		}
		return total;
	}

	public Map<String, HashMap<String, Double>> getTweetByWord(String word) {
		try {
			SearchResponse searchResponse = null;
			SearchRequest searchRequest = new SearchRequest(INDEX);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.termQuery("text", word));
			searchSourceBuilder.fetchSource(false);
			searchSourceBuilder.size(10000);
			searchRequest.source(searchSourceBuilder);

			searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			Map<String,HashMap<String, Double>> result = new TreeMap<String, HashMap<String, Double>>();
			SearchHit[] hit = hits.getHits();
			Tweet tweet;
			Double positivo=0.0, negativo=0.0;
			for (SearchHit aux:
				 hit) {
				tweet = tweetRepository.findById(Long.parseLong(aux.getId()));
				HashMap<String,Double> classify = classifier.classify(tweet.getText());
				if(classify.get("positive") < classify.get("negative")){
					negativo++;
				}
				else{
					positivo++;
				}
			}
			HashMap<String, Double> values = new HashMap<String,Double>();
			values.put("positive",positivo);
			values.put("negative",negativo);
			result.put(word,values);
			return result;
		} catch(Exception e) {
			return null;
		}
	}

	public Map<Integer, String> getKeywords(){
		String[] bag = null;
		Map<Integer, String> words = new TreeMap<Integer, String>();
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
		int i = 0;
		for (String word: bag) {
			words.put(i,word);
			i++;
		}
		return words;
	}

	public HashMap<String, Long> getTweetByEntities() {
		String[] words = {"Corporación Humanas","INDH Chile","CIEG","Red Contra Violencia","Cegecal"};
		HashMap<String, Long> result = new HashMap<String, Long>();
		for (String word:
			 words) {
			Query query = new Query();
			query.addCriteria(Criteria.where("user.name").is(word));

			List<Tweet> tweets = mongoTemplate.find(query, Tweet.class, "tweet");
			result.put(word,new Long(tweets.size()));
		}
		return result;

	}

}
