package com.beta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//Create a spring boot controller which is automatically detected by spring boot, used for mapping methods to different urls
@Controller
public class EventFormController {

	//Automatically create an event repository to store the events in the database.
	@Autowired
	EventRepository repository;

	//Whenever a get method to either /geotagged/event or /event is used then run this method, return the "event" template to event.html
	@RequestMapping(value = { "/geotagged/event", "/event" }, method = RequestMethod.GET)
	String geotagged() {
		return "event";
	}
	
	//Whenever a post method to event is called thenrun this method, get the name and description and then save the event from the form.
	@RequestMapping(value = { "/geotagged/event", "/event" }, method = RequestMethod.POST)
	public String handleAddEvent(@RequestParam("name") String name, @RequestParam("desc") String desc) {
		if (desc.length() != 0) {
			this.repository.save(new Event(name, desc));
		} else {
			this.repository.save(new Event(name));
		}
		return "event";
	}
}
