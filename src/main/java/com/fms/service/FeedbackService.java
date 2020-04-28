package com.fms.service;

import java.util.List;

import com.fms.model.FeedbackQuestion;

public interface FeedbackService {

	FeedbackQuestion insertFeedback(FeedbackQuestion feedback) throws Exception;

	List<FeedbackQuestion> getAllFeedback() throws Exception;

	String removeFeedback(String feedback) throws Exception;

}
