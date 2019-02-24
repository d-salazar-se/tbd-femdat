package tbd.grupo1.femdat.repositories;

import java.util.List;
import java.util.Date;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import tbd.grupo1.femdat.model.Tweet;

public interface TweetRepository extends MongoRepository<Tweet, Integer> {

    public List<Tweet> findByCreatedAt(String createdAt);
    public Tweet findById(Long id);
}