package br.com.compasso.posthistoryapi.message.config;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import jakarta.jms.ConnectionFactory;


import jakarta.jms.Destination;
import jakarta.jms.MessageConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableJms
//@PropertySource("classpath:application.properties")
  public class JmsConfig {

//  @Value("${jms.broker.url}")
//  private String brokerUrl;
//
//  @Value(GlobalConstants.POST_OK_QUEUE)
//  private String queuePostOk;
//
//  @Value(GlobalConstants.FAILED_QUEUE)
//  private String queueFailed;
//
//  @Autowired
//  private MessageConsumer messageConsumer;
//
//
//  @Bean
//  public BrokerService broker() throws Exception {
//    BrokerService broker = new BrokerService();
//    broker.addConnector(brokerUrl);
//    broker.setPersistent(false);
//    return broker;
//  }
//
//  @Bean
//  public ConnectionFactory connectionFactory() {
//    return new CachingConnectionFactory(new ActiveMQConnectionFactory(brokerUrl));
//  }
//
//  @Bean
//  public Destination postOkDestination() {
//    return new ActiveMQQueue(queuePostOk);
//  }
//
//  @Bean
//  public Destination failedDestination() {
//    return new ActiveMQQueue(queueFailed);
//  }
//
//  @Bean
//  public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, Destination destination) {
//    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//    jmsTemplate.setDefaultDestination(destination);
//
//    return jmsTemplate;
//  }
//
//  @Bean
//  public MessageListenerContainer defaultMessageListenerContainer(ConnectionFactory connectionFactory,
//                                                                  Destination destination) {
//    DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
//    defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
//    defaultMessageListenerContainer.setDestination(destination);
//    defaultMessageListenerContainer.setMessageListener(messageConsumer);
//
//    return defaultMessageListenerContainer;
//  }



    @Bean
    public JmsListenerContainerFactory<?> defaultFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configure) {

      DefaultJmsListenerContainerFactory factory =
        new DefaultJmsListenerContainerFactory();
      configure.configure(factory, connectionFactory);
      return factory;
    }

//    public JmsListenerContainerFactory<?> myFactory(
//            ConnectionFactory connectionFactory,
//            DefaultJmsListenerContainerFactoryConfigurer configurer) {
//
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//
//        // anonymous class
//        factory.setErrorHandler(
//                new ErrorHandler() {
//                    @Override
//                    public void handleError(Throwable t) {
//                        System.err.println("An error has occurred in the transaction");
//                    }
//                });
//
//        // lambda function
//        //factory.setErrorHandler(t -> System.err.println("An error has occurred in the transaction"));
//
//
//        // This provides all boot's default to this factory, including the message converter
//        configurer.configure(factory, connectionFactory);
//        // You could still override some of Boot's default if necessary.
//
//        return factory;
//    }

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
      MappingJackson2MessageConverter converter =
        new MappingJackson2MessageConverter();
      converter.setTargetType(MessageType.TEXT);
      converter.setTypeIdPropertyName("_type");
      return converter;
    }
  }

