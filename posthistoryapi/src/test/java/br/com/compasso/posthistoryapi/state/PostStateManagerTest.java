package br.com.compasso.posthistoryapi.state;

import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.state.classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static br.com.compasso.posthistoryapi.constants.GlobalConstants.DISABLE_QUEUE;
import static br.com.compasso.posthistoryapi.constants.GlobalConstants.REPROCESS_QUEUE;
import static org.junit.jupiter.api.Assertions.*;

class PostStateManagerTest {
    private PostStateManager processStateManager;
    private PostStateManager reprocessStateManager;
    private PostStateManager disableStateManager;

    @BeforeEach
    void setUp() {
        this.processStateManager = new PostStateManager(1L);
        this.reprocessStateManager = new PostStateManager(new Post(2L), REPROCESS_QUEUE);
        this.disableStateManager = new PostStateManager(new Post(3L), DISABLE_QUEUE);
    }


    @Test
    void handleState() {
        PostState postStateBeforeHandle = processStateManager.getState();
        processStateManager.handleState();
        PostState postStateAfterHandle = processStateManager.getState();
        assertNotEquals(postStateBeforeHandle, postStateAfterHandle);
    }

    @Test
    void handleFailed() {
        processStateManager.handleState();
        processStateManager.handleFailed();
        PostState postStateAfterHandle = processStateManager.getState();
        assertEquals(FailedState.class, postStateAfterHandle.getClass());
    }

    @Test
    void testHandleStateFluxForProcess(){
        PostState postStateCreated = processStateManager.getState();
        processStateManager.handleState();
        Status statusCreated = processStateManager.getLastHistoryStatus();

        PostState postStatePostFind = processStateManager.getState();
        processStateManager.handleState();
        Status statusPostFind = processStateManager.getLastHistoryStatus();

        PostState postStatePostOk = processStateManager.getState();
        processStateManager.handleState();
        Status statusPostOk = processStateManager.getLastHistoryStatus();

        PostState postStateCommentFind = processStateManager.getState();
        processStateManager.handleState();
        Status statusCommentFind = processStateManager.getLastHistoryStatus();

        PostState postStateCommentOk = processStateManager.getState();
        processStateManager.handleState();
        Status statusCommentOk = processStateManager.getLastHistoryStatus();

        PostState postStateEnabled = processStateManager.getState();
        processStateManager.handleState();
        Status statusEnable = processStateManager.getLastHistoryStatus();

        assertEquals(CreatedState.class, postStateCreated.getClass());
        assertEquals(PostFindState.class, postStatePostFind.getClass());
        assertEquals(PostOkState.class, postStatePostOk.getClass());
        assertEquals(CommentFindState.class, postStateCommentFind.getClass());
        assertEquals(CommentOkState.class, postStateCommentOk.getClass());
        assertEquals(EnabledState.class, postStateEnabled.getClass());

        assertEquals(Status.CREATED, statusCreated);
        assertEquals(Status.POST_FIND, statusPostFind);
        assertEquals(Status.POST_OK, statusPostOk);
        assertEquals(Status.COMMENTS_FIND, statusCommentFind);
        assertEquals(Status.COMMENTS_OK, statusCommentOk);
        assertEquals(Status.ENABLED, statusEnable);
    }

    @Test
    void testHandleStateFluxforReprocess(){
        PostState postStateUpdating = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusUpdating = reprocessStateManager.getLastHistoryStatus();

        PostState postStatePostFind = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusPostFind = reprocessStateManager.getLastHistoryStatus();

        PostState postStatePostOk = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusPostOk = reprocessStateManager.getLastHistoryStatus();

        PostState postStateCommentFind = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusCommentFind = reprocessStateManager.getLastHistoryStatus();

        PostState postStateCommentOk = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusCommentOk = reprocessStateManager.getLastHistoryStatus();

        PostState postStateEnabled = reprocessStateManager.getState();
        reprocessStateManager.handleState();
        Status statusEnable = reprocessStateManager.getLastHistoryStatus();

        assertEquals(UpdatingState.class, postStateUpdating.getClass());
        assertEquals(PostFindState.class, postStatePostFind.getClass());
        assertEquals(PostOkState.class, postStatePostOk.getClass());
        assertEquals(CommentFindState.class, postStateCommentFind.getClass());
        assertEquals(CommentOkState.class, postStateCommentOk.getClass());
        assertEquals(EnabledState.class, postStateEnabled.getClass());

        assertEquals(Status.UPDATING, statusUpdating);
        assertEquals(Status.POST_FIND, statusPostFind);
        assertEquals(Status.POST_OK, statusPostOk);
        assertEquals(Status.COMMENTS_FIND, statusCommentFind);
        assertEquals(Status.COMMENTS_OK, statusCommentOk);
        assertEquals(Status.ENABLED, statusEnable);
    }

    @Test
    void testHandleStateFluxforDisable(){
        PostState PostStateDisable = disableStateManager.getState();
        disableStateManager.handleState();
        Status statusDisabled = disableStateManager.getLastHistoryStatus();
        assertEquals(DisableState.class, PostStateDisable.getClass());
        assertEquals(Status.DISABLED, statusDisabled);
    }
}
