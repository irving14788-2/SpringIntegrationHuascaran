package com.bbva.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageDeliveryException;
//import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;

import com.bbva.integration.DAO.LogDAO;



@Component
public class ErrorHandlingService {
 
	@Autowired
	LogDAO logDAO;
	
	@ServiceActivator
	public void handleFailed(Message<MessageExceptionHandler> message) {
	  
	  System.out.println("ENTRO AL MEOTODO PARA GUARDAR EN BD " + message.getPayload());
	  System.out.println("NO PINTA ");
	}

}