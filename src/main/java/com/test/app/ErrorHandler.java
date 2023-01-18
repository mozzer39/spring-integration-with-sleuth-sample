package com.test.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
	
	
	public ResponseEntity<?> handleError(ErrorMessage errorMessage){
		System.out.println("ERROR HANDLER HANDLING ERROR MESSAGE "+errorMessage);
		return new ResponseEntity<>(errorMessage.getPayload().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
