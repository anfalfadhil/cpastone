package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
	@Query(value = "select * from tweet_like l "
			+ "where l.tweet_id = ?1 "
			+ "l.user_id = ?2", 
			nativeQuery = true)
	public Optional<Like> findByData(Integer user_id, Integer tweet_id);
}
