package br.com.compasso.posthistoryreactiveapi.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@EnableR2dbcRepositories(basePackages = {"br.com.compasso.posthistoryreactiveapi.repository.CommentRepository",
  "br.com.compasso.posthistoryreactiveapi.repository.PostRepository", "br.com.compasso.posthistoryreactiveapi.repository.HistoryRepository"})
public class R2dbcConfig extends AbstractR2dbcConfiguration {

  @Bean
  public ConnectionFactory connectionFactory() {
    return new H2ConnectionFactory(
      H2ConnectionConfiguration.builder()
        .inMemory("./database/historyPost")
        .username("admin")
        .password("admin")
        .build()
    );
  }
//  @Bean
//  public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
//    return DatabaseClient.builder()
//      .connectionFactory(connectionFactory)
//      .build();
//  }

  @Bean
  public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }
}
