package com.simplilearn.spring.endpoint;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.simplilearn.soap.GetUserRequest;
import com.simplilearn.soap.GetUserResponse;
import com.simplilearn.spring.service.UserService;

@Endpoint
public class UserEndpoint {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NAMESPACE_URI = "http://simplilearn.com/soap";

    @Autowired
    UserService userService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
	@ResponsePayload
	public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
    	
        logger.debug("Getting User: {}", request.getUsername());

		GetUserResponse response = new GetUserResponse();
		response.setUser(userService.findUser(request.getUsername()));

		return response;
	}
}
