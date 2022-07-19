package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{

}
