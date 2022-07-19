package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
