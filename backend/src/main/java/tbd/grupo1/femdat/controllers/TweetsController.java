package tbd.grupo1.femdat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import tbd.grupo1.femdat.model.Data;
import tbd.grupo1.femdat.model.Tweet;
import tbd.grupo1.femdat.model.TwitterUser;
import tbd.grupo1.femdat.model.TweetIndex;
import tbd.grupo1.femdat.repositories.TweetRepository;
import tbd.grupo1.femdat.sentimentanalysis.Classifier;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@CrossOrigin(origins = "*")
@ComponentScan(basePackages="tbd.grupo1.femdat")
@RestController
@EnableAutoConfiguration
@RequestMapping("/tweets")
public class TweetsController {
    @Autowired
    private TweetRepository repository;

   	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private TweetIndex femdatIndex;

	@Autowired
	private Classifier classifier;

	@GetMapping("/summary")
	public Map<String, Integer> summaryByCreatedAt() {
    	Map<String, Integer> summary = new TreeMap<String, Integer>();
    	    	
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		int days;
		int quantity;
		String dateAsString;

		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		for (days = -7; days <= 0; days++) {
			calendar.add(Calendar.DATE, 1);
			
			date = calendar.getTime();

			dateAsString = df.format(date);
			// quantity = repository.findByCreatedAt(dateAsString).size();	// malo, filtra por timestamp
			quantity = Tweet.findByCreatedAt(date).size();

			summary.put(dateAsString, quantity);
		}

		return summary;
    }

	@GetMapping("/summary/{word}")
	public Map<String, Long> summaryByWord(@PathVariable String word) {
    	Map<String, Long> summary = new TreeMap<String, Long>();
    	    	
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		int days;
		Long quantity;
		String dateAsString;

		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		for (days = -7; days <= 0; days++) {
			calendar.add(Calendar.DATE, 1);
			
			date = calendar.getTime();

			dateAsString = df.format(date);
			quantity = Tweet.findWordByCreatedAt(word, date);

			summary.put(dateAsString, quantity);
		}

		return summary;
    }

	@GetMapping("/apreciation")
	public Map<String, Float> summaryByApreciation(){
		return Tweet.summaryByApreciation(femdatIndex, resourceLoader, classifier);
	}

	@GetMapping("/top-figures")
	public List<TwitterUser> getTopFigures(){
		return Tweet.topFigures();
	}

	@GetMapping("/top-topics")
	public List<Data> getTopTopics(){
		return Tweet.topTopics(femdatIndex, resourceLoader);
	}

	@GetMapping("/top-tweets")
	public List<Tweet> getTopTweets(){
		return Tweet.topTweets();
	}

	@GetMapping("/countries-afinity")
	public List<Data> getCountriesAfinity(){
		return Tweet.countriesAfinity(femdatIndex, classifier);
	}

	@GetMapping("/countries-activity")
	public List<Data> getCountriesActivity(){
		return Tweet.countriesActivity(femdatIndex);
	}

	@GetMapping("/institutions-activity")
	public List<Data> getInstitutionsActivity(){
		return Tweet.institutionsActivity(femdatIndex);
	}

	@GetMapping("/last-week-sa")
	public Map<String, Double> getSummarySA(){
		return Tweet.sentimentAnalysisLastWeek(classifier);
	}
}
