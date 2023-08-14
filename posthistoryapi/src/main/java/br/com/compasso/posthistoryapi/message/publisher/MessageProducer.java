package br.com.compasso.posthistoryapi.message.publisher;

import br.com.compasso.posthistoryapi.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageProducer<T> {

  @Autowired
  private JmsTemplate jmsTemplate;

  public void sendMessage(String queueName, Long postId) {
    jmsTemplate.convertAndSend(queueName, postId);
  }

  public void sendObjectMessage(String queueName, History history) {
    jmsTemplate.convertAndSend(queueName, history);
  }
}
