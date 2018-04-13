package de.hybris.platform.customerreview.validate.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import de.hybris.platform.customerreview.constants.CustomerReviewConstants;
import de.hybris.platform.customerreview.validate.CustomerReviewValidator;
// Trying to reuse existing exception; it will show an error as source code is not given
import de.hybris.platform.jalo.JaloInvalidParameterException;

/**
 * Validator to review a customer review
 *
 */
public class GeneratedCustomerReviewValidator implements CustomerReviewValidator {
	
	private Double rating;
	
	private String comment;
	
	final Properties properties;
	
	final InputStream inputStream;	
	
	public GeneratedCustomerReviewValidator(String comment, Double rating) {
		
		this.comment = comment;
		this.rating = rating;
		
		// read properties file that contains curse strings
		this.properties = new Properties();
		this.inputStream = this.getClass().getResourceAsStream("customerreviewvalidator.properties");
			properties.load(inputStream);		
	}

	@Override
	public void validate() throws JaloInvalidParameterException {
		
	    // if rating is less than zero (assuming min rating in this case), generate exception
		if(rating < CustomerReviewConstants.getInstance().MINRATING)
		   throw new JaloInvalidParameterException("Rating cannot be less than " + CustomerReviewConstants.getInstance().MINRATING);
		    
	    if(null!=comment) {
	       // Split the incoming comment
	 	   String[] words = comment.split(" ");
	 	   String curseString = properties.getProperty("curse.values");
	 	   // Get the list of curse strings
	 	   String[] curses = curseString.split(",");
	 	   long count;
	 	   // Check if any of curse strings is present in comment
	 	   for(String curseWord:curses) {
	 		   // Using streams API to count number of occurences of a curse
	 		   // Ignore any special characters like comma, apostrophe, double-quotes; more characters can be listed in this list
	 		   count = Arrays.asList(words).stream().filter(w->w.replaceAll("[,\'\"]", "").equalsIgnoreCase(curseWord)).collect(Collectors.counting());
	 		   // Even if 1 occurrence of a curse is found, throw an exception 
	 		   if(count>0) {
	 			   throw new JaloInvalidParameterException("User comment is not in acceptable language");
	 		   }
	 	   }
	    }
	}
}
