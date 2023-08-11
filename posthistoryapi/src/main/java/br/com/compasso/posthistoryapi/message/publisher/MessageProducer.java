package br.com.compasso.posthistoryapi.message.publisher;

import br.com.compasso.posthistoryapi.manager.PostManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer<T> {

  @Autowired
  private JmsTemplate jmsTemplate;

  public void sendMessage(String queueName, Long postId) {
    jmsTemplate.convertAndSend(queueName, postId);
  }

  public void sendObjectMessage(String queueName, PostManager postManager) {
    jmsTemplate.convertAndSend(queueName, postManager);
  }
}
