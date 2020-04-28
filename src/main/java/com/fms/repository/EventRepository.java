package com.fms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fms.model.Events;

public interface EventRepository extends MongoRepository<Events, String>{

	List<Events> findByeventIdIn(List<String> eventId);
}
