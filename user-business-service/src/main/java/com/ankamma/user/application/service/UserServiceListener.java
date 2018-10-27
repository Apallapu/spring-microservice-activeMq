package com.ankamma.user.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.ankamma.user.application.feign.UserClient;
import com.ankamma.user.application.model.UserRequest;
import com.ankamma.user.application.model.UserResponse;

@Service
public class UserServiceListener {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceListener.class);

	@Autowired
	UserClient userClient;
	@Autowired
	private JmsTemplate jmsTemplate;

	@JmsListener(destination = "UserRequestQueue", containerFactory = "myFactory")
	public void receiveMessage(UserRequest userRequest) {

		logger.info("UserServiceListener class receiveMessage method start {}");

		UserResponse userResponse = userClient.createUser(userRequest);
		jmsTemplate.convertAndSend("UserResponseQueue", userResponse);
		logger.info("UserServiceListener class receiveMessage method end {}" + userResponse.getId());

		logger.info("UserServiceListener called{}");
	}
}
