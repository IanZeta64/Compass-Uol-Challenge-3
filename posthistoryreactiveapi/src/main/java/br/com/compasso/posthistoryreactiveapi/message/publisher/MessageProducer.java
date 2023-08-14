package br.com.compasso.posthistoryreactiveapi.message.publisher;
import br.com.compasso.posthistoryreactiveapi.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MessageProducer {

  @Autowired
  private JmsTemplate jmsTemplate;

  public Mono<Void> sendMessage(String queueName, Long postId) {
    return Mono.fromRunnable(() -> jmsTemplate.convertAndSend(queueName, postId));
  }

  public Mono<Void> sendObjectMessage(String queueName, History postManager) {
    return Mono.fromRunnable(() -> jmsTemplate.convertAndSend(queueName, postManager));
  }
}
