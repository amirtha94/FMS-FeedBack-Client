package com.fms.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipatedDetails {

	private int employeeId;
	
	private String emailId;
	
	private String eventId;
	
	private String eventName;
	
	private LocalDate eventDate;
	
	private List<FeedbackQuestion> feedback;
}
