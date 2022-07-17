package com.apache.camelmicroservicea.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MyFirstTimerRouter extends RouteBuilder {
		// now after this even if someone changes our bean clas  no issue i will work bcoz we are using instance of  component
		@Autowired
		private GetTodaysDateBean getTodaysDate;
		@Autowired
		private SimpleLoggingProcessingComponent loggingComponent;
		@Override
		public void  configure() throws Exception{
			//queue //timer
			//transformation
			//database //logging output instead of using database
			
			from("timer:first-timer")
			.transform().constant("My message value")
			.log("${body}")
			
			//bean for dynamic tranformations
			
			.bean(getTodaysDate)
			.log("${body}")
			.bean(loggingComponent)
			.log("${body}")
			.bean(new SimpleLoggingProcessor())
			.to("log:first-timer");
		}
}

// using a spring bean to use dynamic changes in our Route
@Component
 class GetTodaysDateBean{
	public String getTodaysDate(){
		return  "Todays Date "+LocalDateTime.now();
	}
}

// we are processing data heree not making any changes
@Component
class SimpleLoggingProcessingComponent{
	
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
	public void process(String message) {
		logger.info("Simple logging processing compnent {}",message);
	}
}

class SimpleLoggingProcessor{
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
	public void process(Exchange  exchange) {
		logger.info("SimpleLoggingProcessor {}",exchange.getMessage().getBody());
	}
}