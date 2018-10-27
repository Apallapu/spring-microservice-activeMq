# spring-microservice-activeMq
Prerequisites
================

step1:Down load Active MQ and Install ActiveMq in local system;
http://localhost:8161/admin/
username:admin
password admin

step2:
Add below code in BootApplication class
@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setErrorHandler(new ErrorHandler() {
			@Override
			public void handleError(Throwable t) {
				System.err.println("An error has occurred in the userRequest");
			}
		});
		factory.setErrorHandler(t -> System.out.println("An error has occurred in the userRequest"));
		configurer.configure(factory, connectionFactory);
		return factory;

	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;

	}
  step3:
  =======
  Auto wired JmsTemplate and use below method in service class.
  Below code for sending userReuest object ActiveMq queue.
  	@Autowired
	private JmsTemplate jmsTemplate;
  example code:
  ============
  public void createUsersForActiveMq(UserRequest userRequest) {
		jmsTemplate.convertAndSend("UserRequestQueue", userRequest);
	}
  
  Step4:
  ========
  create the Listener class get the userRequest json object from activeMq queue.
  please find the example code below.
  @JmsListener(destination = "UserRequestQueue", containerFactory = "myFactory")
	public void receiveMessage(UserRequest userRequest) {

		logger.info("UserServiceListener class receiveMessage method start {}");

		UserResponse userResponse = userClient.createUser(userRequest);
		jmsTemplate.convertAndSend("UserResponseQueue", userResponse);
		logger.info("UserServiceListener class receiveMessage method end {}" + userResponse.getId());

		logger.info("UserServiceListener called{}");
	}
  
  
  step5:
  ======
  Send the response object activeMQ queue,here we are using UserResponseQueue name.
   please find the example code below.
   jmsTemplate.convertAndSend("UserResponseQueue", userResponse);
   
   
   

