package tbd.grupo1.femdat.controllers;

import com.mongodb.util.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import tbd.grupo1.femdat.model.Tweet;
import tbd.grupo1.femdat.model.TweetIndex;
import tbd.grupo1.femdat.sentimentanalysis.SentimentAnalysisController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan(basePackages="tbd.grupo1.femdat")
@RestController
@EnableAutoConfiguration
@CrossOrigin(origins = "*")
@RequestMapping("/elasticsearch")
public class ElasticSearchController {
    @Autowired
    private TweetIndex femdatIndex;

    public ElasticSearchController(TweetIndex femdatIndex) {
        this.femdatIndex = femdatIndex;
    }

	@GetMapping("/{id}")
	public Map<String, Object> getTweetById(@PathVariable String id){
	  return femdatIndex.getTweetById(id);
	}
	
	@PostMapping
	public Tweet insertTweet(@RequestBody Tweet tweet) throws Exception {
	  return femdatIndex.insertTweet(tweet);
	}
	
	@PutMapping("/{id}")
	public Map<String, Object> updateTweetById(@RequestBody Tweet tweet, @PathVariable String id) {
	  return femdatIndex.updateTweetById(id, tweet);
	}
	
	@DeleteMapping("/{id}")
	public void deleteTweetById(@PathVariable String id) {
	  femdatIndex.deleteTweetById(id);
	}

	@GetMapping("/afinidad/{word}")
	public Map<String, HashMap<String, Double>> getAfinidadWord(@PathVariable String word){
    	return femdatIndex.getTweetByWord(word);
    }

    @GetMapping("/keywords")
	public Map<Integer, String> getKeyWords(){
    	return femdatIndex.getKeywords();
	}

	@GetMapping("/entidades-afinidad")
	public HashMap<String, Long> getTopAfinidad(){
    	return femdatIndex.getTweetByEntities();
	}
}
