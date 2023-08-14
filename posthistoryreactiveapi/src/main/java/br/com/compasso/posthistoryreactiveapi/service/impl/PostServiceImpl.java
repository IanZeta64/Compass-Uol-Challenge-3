package br.com.compasso.posthistoryreactiveapi.service.impl;

import br.com.compasso.posthistoryreactiveapi.client.PostClient;
import br.com.compasso.posthistoryreactiveapi.client.dto.CommentDto;
import br.com.compasso.posthistoryreactiveapi.client.dto.PostDto;
import br.com.compasso.posthistoryreactiveapi.entity.Comment;
import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.entity.Post;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.DuplicatePostException;
import br.com.compasso.posthistoryreactiveapi.exceptions.HistoryNotFoundException;
import br.com.compasso.posthistoryreactiveapi.exceptions.PostNotFoundException;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
import br.com.compasso.posthistoryreactiveapi.message.publisher.MessageProducer;
import br.com.compasso.posthistoryreactiveapi.repository.CommentInMemoryRep;
//import br.com.compasso.posthistoryreactiveapi.repository.CommentRepository;
import br.com.compasso.posthistoryreactiveapi.repository.HistoryInMemoryRepo;
//import br.com.compasso.posthistoryreactiveapi.repository.HistoryRepository;
import br.com.compasso.posthistoryreactiveapi.repository.PostInMemoryRepo;
//import br.com.compasso.posthistoryreactiveapi.repository.PostRepository;
import br.com.compasso.posthistoryreactiveapi.response.PostResponse;
import br.com.compasso.posthistoryreactiveapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.List;

import static br.com.compasso.posthistoryreactiveapi.constants.GlobalConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
  private final PostClient client;
  private final MessageProducer mqProducer;
  private final PostInMemoryRepo postRepository;
  private final CommentInMemoryRep commentRepository;
  private final HistoryInMemoryRepo historyRepository;

  @Override
  public Mono<Void> process(Long id) {
    return Mono.defer(() -> {
      log.info("PROCESS - Check post history - {}", id);
      return postRepository.existsById(id)
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(new DuplicatePostException(""));
          } else {
            return mqProducer.sendMessage(PROCESS_QUEUE, id);
          }
        })
        .subscribeOn(Schedulers.boundedElastic());
    });
  }

  @Override
  public Mono<Void> disable(Long id) {
    return Mono.defer(() -> {
      log.info("DISABLE - Check post history - {}", id);
      return postRepository.existsById(id)
        .flatMap(exists -> {
          if (!exists) {
            return Mono.error(new HistoryNotFoundException(""));
          } else {
            return mqProducer.sendMessage(DISABLE_QUEUE, id);
          }
        })
        .subscribeOn(Schedulers.boundedElastic());
    });
  }

  @Override
  public Mono<Void> reprocess(Long id) {
    return Mono.defer(() -> {
      log.info("REPROCESS - Check post history - {}", id);
      return postRepository.existsById(id)
        .flatMap(exists -> {
          if (!exists) {
            return Mono.error(new HistoryNotFoundException(""));
          } else {
            return mqProducer.sendMessage(REPROCESS_QUEUE, id);
          }
        })
        .subscribeOn(Schedulers.boundedElastic());
    });
  }

  @Override
  public Flux<PostResponse> findAll() {
//    return Flux.defer(() -> {
//        log.info("FINDALL - find all posts");
//        return postRepository.findAll().map(PostResponse::new);
//      }).switchIfEmpty(Flux.error(new PostNotFoundException("")))
//      .subscribeOn(Schedulers.boundedElastic());
    return null;
  }


  @JmsListener(destination = PROCESS_QUEUE)
  private void createPostHistoryChain(Long postId) {
//    Mono.fromRunnable(() ->{
//      log.info("PROCESS - Creating post - {}", postId);
//     createHistoryStatus(new PostManager(postId));
//    }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    Mono.defer(() -> {
      log.info("PROCESS - Creating post - {}", postId);
      return createHistoryStatus(new PostManager(postId)).subscribeOn(Schedulers.boundedElastic());
    }).doOnNext(post -> log.info("PROCESS - Post Saved successfully  - {}", post)).subscribe();
  }

  @JmsListener(destination = REPROCESS_QUEUE)
  private void updatePostHistoryChain(Long postId) {
    Mono.defer(() -> postRepository.findById(postId)
        .flatMap(post -> {
          log.info("REPROCESS - Updating post - {}", postId);
          return setUpdatingStatus(new PostManager(post));
        }))
      .switchIfEmpty(Mono.error(new PostNotFoundException("")))
      .subscribeOn(Schedulers.boundedElastic()).subscribe();
  }

  @JmsListener(destination = DISABLE_QUEUE)
  private void disablePostHistoryChain(Long postId) {
    Mono.defer(() -> postRepository.findById(postId)
        .flatMap(post -> {
          log.info("REPROCESS - Disable post - {}", postId);
          return setDisabledStatus(new PostManager(post));
        }))
      .switchIfEmpty(Mono.error(new PostNotFoundException("")))
      .subscribeOn(Schedulers.boundedElastic()).subscribe();
  }

  private Mono<Void> createHistoryStatus(PostManager postManager) {
    Long postId =postManager.getPostId();
    return Mono.defer(() -> {
        log.info("SAVE - saved post - {}", postId);
        return postRepository.save(new Post(postId));
      })
      .flatMap(savedPost -> {
        log.info("CREATED - saved history post - {}", postId);
        return historyRepository.save(new History(Status.CREATED, postId));
      })
      .flatMap(savedHistory -> {
        log.info("STATUS - change status post - {}", postId);
        postManager.handleState(savedHistory);
        return setPostFindStatus(postManager);
      });
  }

  private Mono<Void> setPostFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    return Mono.defer(() -> {
        log.info("POST_FIND - saved history post - {}", postId);
        History history = new History(Status.POST_FIND, postId);
        postManager.handleState(history);
        return historyRepository.save(history);
      })
      .flatMap(history -> {
        log.info("CLIENT - Searching post - {}", postId);
        return client.findPostById(postId)
          .flatMap(postDto -> {
            log.info("CLIENT - Post found - {}", postId);
            return setPostOkStatus(postManager, postDto);
          })
          .switchIfEmpty(Mono.defer(() -> {
            log.info("CLIENT - Post not found - {}", postId);
            return setFailedStatus(postManager);
          }));
      });
  }

  private Mono<Void> setPostOkStatus(PostManager postManager, PostDto postFound) {
    Long postId = postManager.getPostId();
    return Mono.defer(() -> {
        log.info("POST_OK - saved history post - {}", postId);
      return historyRepository.save(new History(Status.POST_OK, postId));
    })
      .flatMap(savedHistory -> {
        log.info("STATUS - change status post - {}", postId);
        postManager.handleState(savedHistory);
        return postRepository.save(new Post(postFound, postManager.getHistories()));
      })
      .then(Mono.defer(() -> {
        log.info("SAVE - Saved post - {}", postId);
        return setCommentFindStatus(postManager);
      }));
  }

  private Mono<Void> setCommentFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();

    return Mono.defer(() -> {
        log.info("COMMENT_FIND - saved history post - {}", postId);
        History history = new History(Status.COMMENTS_FIND, postId);
        postManager.handleState(history);
        return historyRepository.save(history);
      })
      .flatMap(history -> {
        log.info("CLIENT - Searching comment of post - {}", postId);
        return client.findCommentsByPostId(postId).collectList().map(Flux::fromIterable)
          .flatMap(commentDto -> {
            log.info("CLIENT - Comment found - {}", postId);
            return setCommentOkStatus(postManager, commentDto);
          })
          .switchIfEmpty(Mono.defer(() -> {
            log.info("CLIENT - Comment not found - {}", postId);
            return setFailedStatus(postManager);
          }));
      });
  }


  private Mono<Void> setCommentOkStatus(PostManager postManager, Flux<CommentDto> commentsFound) {
    Long postId = postManager.getPostId();
    return Flux.defer(() -> commentsFound.collectList()
        .map(commentDtos -> commentDtos.stream().map(
        commentDto -> new Comment(commentDto, postId)).toList())
      ).flatMap( comments -> {
        log.info("SAVE - Saved comments - {}", postId);
        return commentRepository.saveAll(comments);
      }).flatMap(commentsSaved -> {
        log.info("STATUS - change status post - {}", postId);
        History history = new History(Status.COMMENTS_OK, postId);
//      postManager.handleState(history);
        return historyRepository.save(history);
      }).then(Mono.defer(() -> {
      log.info("COMMENT_OK - saved history post - {}", postId);
      return setEnabledStatus(postManager);
    }));
  }

  private Mono<Void> setEnabledStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    return Mono.defer(() -> {
      log.info("ENABLED - saved history post - {}", postId);
      History history = new History(Status.ENABLED, postManager.getPostId());
//      postManager.handleState(history);
      return historyRepository.save(history);
    }).then();
  }

  private Mono<Void> setFailedStatus(PostManager postManager) {
    History history = new History(Status.FAILED, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    setDisabledStatus(postManager);
    return Mono.empty();
  }

  private Mono<Void> setDisabledStatus(PostManager postManager) {
    History history = new History(Status.DISABLED, postManager.getPostId());
    historyRepository.save(history);
    postManager.handleDisabled(history);
    return Mono.empty();
  }

  private Mono<Void> setUpdatingStatus(PostManager postManager) {
    History history = new History(Status.UPDATING, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    setPostFindStatus(postManager);
    return Mono.empty();
  }
}
//
//package br.com.compasso.posthistoryreactiveapi.service.impl;
//
//        import br.com.compasso.posthistoryreactiveapi.client.PostClient;
//        import br.com.compasso.posthistoryreactiveapi.client.dto.CommentDto;
//        import br.com.compasso.posthistoryreactiveapi.client.dto.PostDto;
//        import br.com.compasso.posthistoryreactiveapi.entity.Comment;
//        import br.com.compasso.posthistoryreactiveapi.entity.History;
//        import br.com.compasso.posthistoryreactiveapi.entity.Post;
//        import br.com.compasso.posthistoryreactiveapi.enums.Status;
//        import br.com.compasso.posthistoryreactiveapi.exceptions.DuplicatePostException;
//        import br.com.compasso.posthistoryreactiveapi.exceptions.HistoryNotFoundException;
//        import br.com.compasso.posthistoryreactiveapi.exceptions.PostNotFoundException;
//        import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
//        import br.com.compasso.posthistoryreactiveapi.message.publisher.MessageProducer;
//        import br.com.compasso.posthistoryreactiveapi.repository.CommentInMemoryRep;
////import br.com.compasso.posthistoryreactiveapi.repository.CommentRepository;
//        import br.com.compasso.posthistoryreactiveapi.repository.HistoryInMemoryRepo;
////import br.com.compasso.posthistoryreactiveapi.repository.HistoryRepository;
//        import br.com.compasso.posthistoryreactiveapi.repository.PostInMemoryRepo;
////import br.com.compasso.posthistoryreactiveapi.repository.PostRepository;
//        import br.com.compasso.posthistoryreactiveapi.response.PostResponse;
//        import br.com.compasso.posthistoryreactiveapi.service.PostService;
//        import lombok.RequiredArgsConstructor;
//        import lombok.extern.slf4j.Slf4j;
//        import org.springframework.jms.annotation.JmsListener;
//        import org.springframework.stereotype.Service;
//        import reactor.core.publisher.Flux;
//        import reactor.core.publisher.Mono;
//        import reactor.core.scheduler.Schedulers;
//
//        import java.util.Collections;
//        import java.util.List;
//
//        import static br.com.compasso.posthistoryreactiveapi.constants.GlobalConstants.*;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PostServiceImpl implements PostService {
//  private final PostClient client;
//  private final MessageProducer mqProducer;
//  private final PostInMemoryRepo postRepository;
//  private final CommentInMemoryRep commentRepository;
//  private final HistoryInMemoryRepo historyRepository;
//
//  @Override
//  public Mono<Void> process(Long id) {
//    return Mono.defer(() -> {
//      log.info("PROCESS - Check post history - {}", id);
//      return postRepository.existsById(id)
//              .flatMap(exists -> {
//                if (exists) {
//                  return Mono.error(new DuplicatePostException(""));
//                } else {
//                  return mqProducer.sendMessage(PROCESS_QUEUE, id);
//                }
//              })
//              .subscribeOn(Schedulers.boundedElastic());
//    });
//  }
//
//  @Override
//  public Mono<Void> disable(Long id) {
//    return Mono.defer(() -> {
//      log.info("DISABLE - Check post history - {}", id);
//      return postRepository.existsById(id)
//              .flatMap(exists -> {
//                if (!exists) {
//                  return Mono.error(new HistoryNotFoundException(""));
//                } else {
//                  return mqProducer.sendMessage(DISABLE_QUEUE, id);
//                }
//              })
//              .subscribeOn(Schedulers.boundedElastic());
//    });
//  }
//
//  @Override
//  public Mono<Void> reprocess(Long id) {
//    return Mono.defer(() -> {
//      log.info("REPROCESS - Check post history - {}", id);
//      return postRepository.existsById(id)
//              .flatMap(exists -> {
//                if (!exists) {
//                  return Mono.error(new HistoryNotFoundException(""));
//                } else {
//                  return mqProducer.sendMessage(REPROCESS_QUEUE, id);
//                }
//              })
//              .subscribeOn(Schedulers.boundedElastic());
//    });
//  }
//
//  @Override
//  public Flux<PostResponse> findAll() {
////    return Flux.defer(() -> {
////        log.info("FINDALL - find all posts");
////        return postRepository.findAll().map(PostResponse::new);
////      }).switchIfEmpty(Flux.error(new PostNotFoundException("")))
////      .subscribeOn(Schedulers.boundedElastic());
//    return null;
//  }
//
//
//  @JmsListener(destination = PROCESS_QUEUE)
//  private void createPostHistoryChain(Long postId) {
////    Mono.fromRunnable(() ->{
////      log.info("PROCESS - Creating post - {}", postId);
////     createHistoryStatus(new PostManager(postId));
////    }).subscribeOn(Schedulers.boundedElastic()).subscribe();
//    Mono.defer(() -> {
//      log.info("PROCESS - Creating post - {}", postId);
//      return createHistoryStatus(new PostManager(postId)).subscribeOn(Schedulers.boundedElastic());
//    }).doOnNext(post -> log.info("PROCESS - Post Saved successfully  - {}", post)).subscribe();
//  }
//
//  @JmsListener(destination = REPROCESS_QUEUE)
//  private void updatePostHistoryChain(Long postId) {
//    Mono.defer(() -> postRepository.findById(postId)
//                    .flatMap(post -> {
//                      log.info("REPROCESS - Updating post - {}", postId);
//                      return setUpdatingStatus(new PostManager(post));
//                    }))
//            .switchIfEmpty(Mono.error(new PostNotFoundException("")))
//            .subscribeOn(Schedulers.boundedElastic()).subscribe();
//  }
//
//  @JmsListener(destination = DISABLE_QUEUE)
//  private void disablePostHistoryChain(Long postId) {
//    Mono.defer(() -> postRepository.findById(postId)
//                    .flatMap(post -> {
//                      log.info("REPROCESS - Disable post - {}", postId);
//                      return setDisabledStatus(new PostManager(post));
//                    }))
//            .switchIfEmpty(Mono.error(new PostNotFoundException("")))
//            .subscribeOn(Schedulers.boundedElastic()).subscribe();
//  }
//
//  private Mono<Void> createHistoryStatus(PostManager postManager) {
//    Long postId =postManager.getPostId();
//    return Mono.defer(() -> {
//              log.info("SAVE - saved post - {}", postId);
//              return postRepository.save(new Post(postId));
//            })
//            .flatMap(savedPost -> {
//              log.info("CREATED - saved history post - {}", postId);
//              return historyRepository.save(new History(Status.CREATED, postId));
//            })
//            .flatMap(savedHistory -> {
//              log.info("STATUS - change status post - {}", postId);
//              postManager.handleState(savedHistory);
//              return setPostFindStatus(postManager);
//            });
//  }
//
//  private Mono<Void> setPostFindStatus(PostManager postManager) {
//    Long postId = postManager.getPostId();
//    return Mono.defer(() -> {
//              log.info("POST_FIND - saved history post - {}", postId);
//              History history = new History(Status.POST_FIND, postId);
//              postManager.handleState(history);
//              return historyRepository.save(history);
//            })
//            .flatMap(history -> {
//              log.info("CLIENT - Searching post - {}", postId);
//              return client.findPostById(postId)
//                      .flatMap(postDto -> {
//                        log.info("CLIENT - Post found - {}", postId);
//                        return setPostOkStatus(postManager, postDto);
//                      })
//                      .switchIfEmpty(Mono.defer(() -> {
//                        log.info("CLIENT - Post not found - {}", postId);
//                        return setFailedStatus(postManager);
//                      }));
//            });
//  }
//
//  private Mono<Void> setPostOkStatus(PostManager postManager, PostDto postFound) {
//    Long postId = postManager.getPostId();
//    return Mono.defer(() -> {
//              log.info("POST_OK - saved history post - {}", postId);
//              return historyRepository.save(new History(Status.POST_OK, postId));
//            })
//            .flatMap(savedHistory -> {
//              log.info("STATUS - change status post - {}", postId);
//              postManager.handleState(savedHistory);
//              return postRepository.save(new Post(postFound, postManager.getHistories()));
//            })
//            .then(Mono.defer(() -> {
//              log.info("SAVE - Saved post - {}", postId);
//              return setCommentFindStatus(postManager);
//            }));
//  }
//
//  private Mono<Void> setCommentFindStatus(PostManager postManager) {
//    Long postId = postManager.getPostId();
//
//    return Mono.defer(() -> {
//              log.info("COMMENT_FIND - saved history post - {}", postId);
//              History history = new History(Status.COMMENTS_FIND, postId);
//              postManager.handleState(history);
//              return historyRepository.save(history);
//            })
//            .flatMap(history -> {
//              log.info("CLIENT - Searching comment of post - {}", postId);
//              return client.findCommentsByPostId(postId).collectList().map(Flux::fromIterable)
//                      .flatMap(commentDto -> {
//                        log.info("CLIENT - Comment found - {}", postId);
//                        return setCommentOkStatus(postManager, commentDto);
//                      })
//                      .switchIfEmpty(Mono.defer(() -> {
//                        log.info("CLIENT - Comment not found - {}", postId);
//                        return setFailedStatus(postManager);
//                      }));
//            });
//  }
//
//
//  private Mono<Void> setCommentOkStatus(PostManager postManager, Flux<CommentDto> commentsFound) {
//    Long postId = postManager.getPostId();
//    return Flux.defer(() -> commentsFound.collectList()
//            .map(commentDtos -> commentDtos.stream().map(
//                    commentDto -> new Comment(commentDto, postId)).toList())
//    ).flatMap( comments -> {
//      log.info("SAVE - Saved comments - {}", postId);
//      return commentRepository.saveAll(comments);
//    }).flatMap(commentsSaved -> {
//      log.info("STATUS - change status post - {}", postId);
//      History history = new History(Status.COMMENTS_OK, postId);
//      postManager.handleState(history);
//      return historyRepository.save(history);
//    }).then(Mono.defer(() -> {
//      log.info("COMMENT_OK - saved history post - {}", postId);
//      return setEnabledStatus(postManager);
//    }));
//  }
//
//  private Mono<Void> setEnabledStatus(PostManager postManager) {
//    Long postId = postManager.getPostId();
//    return Mono.defer(() -> {
//      log.info("ENABLED - saved history post - {}", postId);
//      History history = new History(Status.ENABLED, postManager.getPostId());
//      postManager.handleState(history);
//      return historyRepository.save(history);
//    }).then();
//  }
//
//  private Mono<Void> setFailedStatus(PostManager postManager) {
//    History history = new History(Status.FAILED, postManager.getPostId());
//    postManager.handleState(history);
//    historyRepository.save(history);
//    setDisabledStatus(postManager);
//    return Mono.empty();
//  }
//
//  private Mono<Void> setDisabledStatus(PostManager postManager) {
//    History history = new History(Status.DISABLED, postManager.getPostId());
//    historyRepository.save(history);
//    postManager.handleDisabled(history);
//    return Mono.empty();
//  }
//
//  private Mono<Void> setUpdatingStatus(PostManager postManager) {
//    History history = new History(Status.UPDATING, postManager.getPostId());
//    postManager.handleState(history);
//    historyRepository.save(history);
//    setPostFindStatus(postManager);
//    return Mono.empty();
//  }
//}
