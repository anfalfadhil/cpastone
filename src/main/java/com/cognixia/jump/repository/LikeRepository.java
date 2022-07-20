package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Like;
import com.cognixia.jump.model.Tweet;
import com.cognixia.jump.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
	
	public Optional<Like> findByUserAndTweet(User user, Tweet tweet);
	public List<Like> findByTweet(Tweet tweet);
}
