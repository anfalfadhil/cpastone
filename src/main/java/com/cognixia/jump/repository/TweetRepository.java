package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer>{

}
