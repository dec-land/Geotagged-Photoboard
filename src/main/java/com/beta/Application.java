package com.beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

//Set up the spring boot project, enable repositories for the images.
@SpringBootApplication
@EnableJpaRepositories
@Import({RepositoryRestMvcConfiguration.class})
public class Application extends SpringBootServletInitializer {
	
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
  {
	  return application.sources(new Class[] { Application.class });
  }
  
  public static void main(String[] args)
  {
	  SpringApplication.run(Application.class, args);
  }
}
