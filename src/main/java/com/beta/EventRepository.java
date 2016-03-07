package com.beta;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//Create a rest repository which runs constantly on the server.
@RepositoryRestResource
public abstract interface EventRepository extends CrudRepository<Event, Long> {
	
	//Automatically create the sql queries to return the events below.
	public abstract List<Event> findByEventID(@Param("id") long paramLong);

	public abstract List<Event> findAll();
}
