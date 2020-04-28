package com.fms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fms.model.FeedbackQuestion;

public interface FeedbackRepository extends MongoRepository<FeedbackQuestion, String>{

	List<FeedbackQuestion> findAllByfbType(String fbType);
}
