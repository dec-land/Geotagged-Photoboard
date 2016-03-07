package com.beta;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//Create a rest repository which runs constantly on the server.
@RepositoryRestResource
public abstract interface PhotoRepository extends CrudRepository<Photo, Long> {
	
	
	//Automatically creates the sql queries for the methods below, mapped to a url.
	public abstract List<Photo> findByPhotoID(@Param("id") long paramLong);

	public abstract List<Photo> findAll();

	public abstract List<Photo> findByDate(@Param("date") String paramString);

	public abstract List<Photo> findByEvent(@Param("event") String paramString);

	public abstract List<Photo> findByStatus(@Param("status") int paramInt);

	public abstract List<Photo> findByDateAndStatus(@Param("date") String paramString, @Param("status") int paramInt);

	public abstract List<Photo> findByDateAndStatusAndEvent(@Param("date") String paramString1,
			@Param("status") int paramInt, @Param("event") String paramString2);

	public abstract List<Photo> findByDateAndEvent(@Param("date") String paramString1,
			@Param("event") String paramString2);

	public abstract List<Photo> findByStatusAndEvent(@Param("status") int paramInt, @Param("event") String paramString);
}
