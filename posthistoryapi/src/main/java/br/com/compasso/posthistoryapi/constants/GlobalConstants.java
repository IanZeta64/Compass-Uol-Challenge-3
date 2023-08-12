package br.com.compasso.posthistoryapi.constants;

public class GlobalConstants {
  public static final String PROCESS_QUEUE = "process-queue";
  public static final String REPROCESS_QUEUE = "reprocess-queue";
  public static final String DISABLE_QUEUE = "disable-queue";
  public static final String POST_OK_QUEUE = "post-ok-queue";
  public static final String FAILED_QUEUE = "failed-queue";

  public static final String METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE = "Invalid Field: '%s'. Cause: '%s'.";

  public static final String SAVE_HISTORY_QUEUE = "save-history-queue";
  public static final String SAVE_POST_QUEUE = "save-post-queue";
  public static final String SAVE_COMMENT_QUEUE = "save-comment-queue";
  public static final String TEST_QUEUE = "test-queue";

  public static final long POST_serialVersionUID = 10L;
  public static final long POST_DTO_serialVersionUID = 11L;
  public static final long COMMENT_serialVersionUID = 20L;

  public static final long COMMENT_DTO_serialVersionUID = 21L;

  public static final long HISTORY_serialVersionUID = 30L;

  public static final long POST_MANAGER_serialVersionUID = 40L;

  public static final long POST_STATE_SERVICE_serialVersionUID = 50L;

  public static final long POST_STATE_CREATED_serialVersionUID = 51L;

  public static final long POST_STATE_POST_FIND_serialVersionUID = 52L;

  public static final long POST_STATE_POST_OK_serialVersionUID = 53L;

  public static final long POST_STATE_COMMENT_FIND_serialVersionUID = 54L;

  public static final long POST_STATE_COMMENT_OK_serialVersionUID = 55L;

  public static final long POST_STATE_ENABLED_serialVersionUID = 56L;
  public static final long POST_STATE_UPDATING_serialVersionUID = 57L;
  public static final long POST_STATE_DISABLED_serialVersionUID = 58L;
  public static final long POST_STATE_FAILED_serialVersionUID = 59L;


}
