package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer>{
	@Query(value = "select * from `tweet` t"
			+ "	where t.user_id = ?1", 
			nativeQuery = true)
	public List<Tweet> haveSameUser(Integer id);
}
