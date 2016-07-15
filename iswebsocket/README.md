# iswebsocket

1. 일정간격으로 스케쥴을 돌려서 BroadCasting Message (WebSocket over STOMP)
2. DB 조회시 SQL은 xml과 db 둘중 하나에서 로딩하도록 
3. reference URL : 
  http://www.concretepage.com/spring-4/spring-4-websocket-sockjs-stomp-tomcat-example

      	1) connect : 	/iswebsocket/add
    	2) subscribe : 	/topic/showResult
    	3) send : 		/calcApp/add
    	
    	@MessageMapping("/add" )
        @SendTo("/topic/showResult")
        
        @Override
		public void configureMessageBroker(MessageBrokerRegistry config) {
			config.enableSimpleBroker("/topic");
			config.setApplicationDestinationPrefixes("/calcApp");
		}
	
		@Override
		public void registerStompEndpoints(StompEndpointRegistry registry) {
			registry.addEndpoint("/add").withSockJS();
		}
