package br.com.compasso.posthistoryapi.services.impl;

import br.com.compasso.posthistoryapi.client.dto.CommentDto;
import br.com.compasso.posthistoryapi.client.dto.PostDto;
import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.Comment;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.message.publisher.MessageProducer;
import br.com.compasso.posthistoryapi.repositories.CommentRepository;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.repositories.PostRepostory;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.compasso.posthistoryapi.constants.GlobalConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PostHistoryServiceImplTest {

    @InjectMocks
    private PostHistoryServiceImpl service;

    @Mock
    private PostRepostory postRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MessageProducer messageProducer;

    @Mock
    private PostClient client;

    private PostStateManager postStateManager;

    private PostDto postDto;
    private List<History> histories;
    private List<CommentDto> commentDtoList;

    @BeforeEach
    void setUp() {
        this.postStateManager = new PostStateManager(1L);
        this.histories = new ArrayList<>();
        this.postDto = new PostDto(1L,
                "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\n" +
                        "reprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        this.commentDtoList = List.of(
                new CommentDto(1L, "id labore ex et quam laborum"),
                new CommentDto(2L, "quo vero reiciendis velit similique earum"),
                new CommentDto(3L, "odio adipisci rerum aut animi"),
                new CommentDto(4L, "alias odio sit"),
                new CommentDto(5L, "vero eaque aliquid doloribus et culpa"));
    }

    @Test
    void shouldTestProcessWithSuccess() {
        History historyCreated = postStateManager.handleState();
        History historyPostFind = postStateManager.handleState();

        Post createdPost = new Post(1L, List.of(historyCreated));
        Post postOkPost = new Post(postDto, postStateManager.getHistories());
        doReturn(false).when(postRepository).existsById(anyLong());
        doReturn(createdPost, postOkPost).when(postRepository).save(any(Post.class));
        doReturn(postDto).when(client).findPostById(anyLong());
        doReturn(historyPostFind).when(historyRepository).save(any(History.class));
        doNothing().when(messageProducer).sendMessage(POST_OK_QUEUE, 1L);
        service.process(1L);
        verify(postRepository, times(1)).existsById(anyLong());
        verify(postRepository, times(2)).save(any(Post.class));
        verify(client, times(1)).findPostById(anyLong());
        verify(historyRepository, times(1)).save(any(History.class));
        verify(messageProducer, times(1)).sendMessage(anyString(), anyLong());
    }

    @Test
    void shouldTestProcessWithFailed() {
        History historyCreated = postStateManager.handleState();
        History historyPostFind = postStateManager.handleFailed();
        Post createdPost = new Post(1L, List.of(historyCreated));

        doReturn(false).when(postRepository).existsById(anyLong());
        doReturn(createdPost).when(postRepository).save(any(Post.class));
        doThrow(FeignException.class).when(client).findPostById(anyLong());
        doReturn(historyPostFind).when(historyRepository).save(any(History.class));
        doNothing().when(messageProducer).sendObjectMessage(eq(FAILED_QUEUE), any(History.class));

        service.process(1L);

        verify(postRepository, times(1)).existsById(anyLong());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(client, times(1)).findPostById(anyLong());
        verify(historyRepository, times(1)).save(any(History.class));
        verify(messageProducer, times(1)).sendObjectMessage(eq(FAILED_QUEUE), any(History.class));
    }

    @Test
    void shouldTestDisableWithSuccess() {
        histories = getHistories();
        Post disabledPost = new Post(postDto, histories);
        PostStateManager postStateManagerDisable = new PostStateManager(disabledPost, DISABLE_QUEUE);
        History historyPostDisable = postStateManagerDisable.handleState();
        histories.add(historyPostDisable);

        doReturn(true).when(postRepository).existsById(anyLong());
        doReturn(Optional.of(disabledPost)).when(postRepository).findById(anyLong());
        doReturn(historyPostDisable).when(historyRepository).save(any(History.class));

        service.disable(1L);

        verify(postRepository, times(1)).existsById(anyLong());
        verify(postRepository, times(1)).findById(anyLong());
        verify(historyRepository, times(1)).save(any(History.class));
    }


    @Test
    void shouldTestReprocessWithSuccess() {
        histories = getHistories();
        Post updatingPost = new Post(postDto, histories);
        PostStateManager postStateManagerUpdating = new PostStateManager(updatingPost, REPROCESS_QUEUE);

        History historyPostUpdating = postStateManagerUpdating.handleState();
        History historyPostFind = postStateManagerUpdating.handleState();
        History historyPostOk = postStateManagerUpdating.handleState();
        histories.addAll(List.of(historyPostUpdating, historyPostFind));

        Post postOkPost = new Post(postDto, histories);

        doReturn(true).when(postRepository).existsById(anyLong());
        doReturn(Optional.of(updatingPost)).when(postRepository).findById(anyLong());
        doReturn(postOkPost).when(postRepository).save(any(Post.class));
        doReturn(postDto).when(client).findPostById(anyLong());
        doReturn(historyPostUpdating, historyPostFind, historyPostOk).when(historyRepository).save(any(History.class));
        doNothing().when(messageProducer).sendMessage(POST_OK_QUEUE, 1L);
        service.reprocess(1L);
        verify(postRepository, times(1)).existsById(anyLong());
        verify(postRepository, times(1)).findById(anyLong());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(client, times(1)).findPostById(anyLong());
        verify(historyRepository, times(2)).save(any(History.class));
        verify(messageProducer, times(1)).sendMessage(anyString(), anyLong());
    }


    @Test
    void shouldTestReprocessWithFailed() {
        histories = getHistories();
        Post updatingPost = new Post(postDto, histories);

        PostStateManager postStateManagerUpdating = new PostStateManager(updatingPost, REPROCESS_QUEUE);
        History historyPostUpdating = postStateManagerUpdating.handleState();
        History historyPostFind = postStateManagerUpdating.handleState();
        histories.addAll(List.of(historyPostUpdating, historyPostFind));

        doReturn(true).when(postRepository).existsById(anyLong());
        doReturn(Optional.of(updatingPost)).when(postRepository).findById(anyLong());
        doThrow(FeignException.class).when(client).findPostById(anyLong());
        doReturn(historyPostUpdating, historyPostFind).when(historyRepository).save(any(History.class));
        doNothing().when(messageProducer).sendObjectMessage(eq(FAILED_QUEUE), any(History.class));

        service.reprocess(1L);

        verify(postRepository, times(1)).existsById(anyLong());
        verify(postRepository, times(1)).findById(anyLong());
        verify(client, times(1)).findPostById(anyLong());
        verify(historyRepository, times(2)).save(any(History.class));
        verify(messageProducer, times(1)).sendObjectMessage(eq(FAILED_QUEUE), any(History.class));
    }


    @Test
    void shouldTestFindAll() {
        histories = getHistories();
        Post post = new Post(postDto.getId(), postDto.getTitle(), postDto.getBody(),
                commentDtoList.stream().map(commentDto -> new Comment(commentDto, 1L)).toList(), histories);
        List<PostDtoResponse> postDtoResponseList = new ArrayList<>();
        postDtoResponseList.add(new PostDtoResponse(post));

        doReturn(Arrays.asList(post)).when(postRepository).findAll();
        List<PostDtoResponse> historiesFindAll = service.findAll();
        verify(postRepository, times(1)).findAll();

        assertEquals(postDtoResponseList, historiesFindAll);

    }

    @Test
    void shouldTestCommentFindSequenceWithSuccess() {
        PostStateManager postStateManager = new PostStateManager(new Post(1L), POST_OK_QUEUE);
        History historyCommentfind = postStateManager.handleState();
        History historyCommentOk = postStateManager.handleState();
        History historyEnabled = postStateManager.handleState();
        List<Comment> comments = commentDtoList.stream().map(commentDto -> new Comment(commentDto, 1L)).toList();

        doReturn(commentDtoList).when(client).findCommentByPostId(1L);
        doReturn(historyCommentfind, historyCommentOk, historyEnabled).when(historyRepository).save(any(History.class));
        doReturn(comments).when(commentRepository).saveAll(anyList());

        service.setCommentFindStatus(1L);

        verify(client, times(1)).findCommentByPostId(anyLong());
        verify(historyRepository, times(3)).save(any(History.class));
        verify(commentRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldTestCommentFindSequenceWithError() {
        PostStateManager postStateManager = new PostStateManager(new Post(1L), POST_OK_QUEUE);
        History historyCommentfind = postStateManager.handleFailed();
        History historyFailed = postStateManager.handleState();
        History historyDisabled = postStateManager.handleState();

        doThrow(FeignException.class).when(client).findCommentByPostId(1L);
        doReturn(historyCommentfind, historyFailed, historyDisabled).when(historyRepository).save(any(History.class));

        service.setCommentFindStatus(1L);

        verify(client, times(1)).findCommentByPostId(anyLong());
        verify(historyRepository, times(3)).save(any(History.class));
    }

    private List<History> getHistories() {
        List<History> histories = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            histories.add(postStateManager.handleState());
        }
        return histories;
    }


}
