package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.Comment;
import com.cognixia.jump.model.User;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	public List<Comment> findByUser(User user);
}
