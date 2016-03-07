package com.beta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//entity anotation used to signify this is going to be a table created in the database.
@Entity
public class Event {
	
	//Make eventID the id and also an auto increment field.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long eventID;
	private String name = "";
	private String description = "";

	protected Event() {
	}

	Event(String name, String description) {
		this.name = name;
		this.description = description;
	}

	Event(String name) {
		this.name = name;
	}

	public long getID() {
		return this.eventID;
	}

	public String getDesc() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}
}
