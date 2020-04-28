package com.fms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.model.FeedbackQuestion;
import com.fms.repository.FeedbackRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	FeedbackRepository feedbackRepo;

	@Override
	public FeedbackQuestion insertFeedback(FeedbackQuestion feedback) throws Exception {
		try {
			return feedbackRepo.save(feedback);
		} catch (Exception e) {
			log.error("Exception occured while saving data {}", e.getMessage());
			throw new Exception();
		}
	}

	@Override
	public List<FeedbackQuestion> getAllFeedback() throws Exception {
		List<FeedbackQuestion> feedbackQues = null;
		try {
			List<FeedbackQuestion> questions = feedbackRepo.findAll();
			feedbackQues = questions.stream().map(i -> {
				if (i.getAnswers() != null && !i.getAnswers().isEmpty()) {
					i.setTotal_answers(String.valueOf(i.getAnswers().size()));
				} else {
					i.setTotal_answers("0");
				}
				return i;
			}).collect(Collectors.toList());

		} catch (Exception e) {
			log.error("Exception at removeFeedback() {}", e.getMessage());
			throw new Exception();
			
		}
		return feedbackQues;
	}

	@Override
	public String removeFeedback(String feedback) throws Exception {
		String result = null;
		try {
			feedbackRepo.deleteById(feedback);
			result = "Successfully Deleted !";
		} catch (Exception e) {
			log.error("Exception at removeFeedback() {}", e.getMessage());
			result = "Deletion Failed";
			throw new Exception(result);
		}
		return result;
	}
}
