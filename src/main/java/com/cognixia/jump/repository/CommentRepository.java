package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Comment;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.Tweet;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	public List<Comment> findByUser(User user);
	public List<Comment> findByTweet(Tweet tweet);
}
