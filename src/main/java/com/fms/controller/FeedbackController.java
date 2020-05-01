package com.fms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fms.model.FeedbackQuestion;
import com.fms.service.FeedbackService;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService service;

	@PostMapping("/feedback")
	public ResponseEntity<FeedbackQuestion> insertFeedback(@RequestBody FeedbackQuestion feedback) throws Exception {
		FeedbackQuestion fQuestion = service.insertFeedback(feedback);
		return new ResponseEntity<FeedbackQuestion>(fQuestion, HttpStatus.OK);
	}

	@GetMapping("/feedback")
	public ResponseEntity<List<FeedbackQuestion>> getAllFeedback() throws Exception {
		List<FeedbackQuestion> feedbackList = service.getAllFeedback();
		return new ResponseEntity<List<FeedbackQuestion>>(feedbackList, HttpStatus.OK);
	}

	@DeleteMapping("/feedback")
	public ResponseEntity<String> removeFeedback(@RequestBody FeedbackQuestion feedback) throws Exception {
		String res = service.removeFeedback(feedback.getFbQuestion());
		return new ResponseEntity<String>(res, HttpStatus.OK);
	}
}
