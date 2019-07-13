package com.ugc.wrapper;

public interface Constants
{
    String SNG = "SNG";

    //---------------------------------- SNG TAGS  ------------------------------------------

    String TAG_DELIMITER = ",";

    //---------------------------------- SNG CONTROLLER  ----------------------------------------------

    int SNG_MAX_PAGE_SIZE = 100;

    //----------------------------------------- SNS CONNECTOR -----------------------------------------

    String SNS_ERROR = "-2";
    String SNS_DEFAULT_CHARSET_ENCODING = "UTF-8";
    int SNS_TOKEN_MIN_VALIDATION_TIME = 60000;//1 minute

    //----------------------------------------- HTTP HEADERS ------------------------------------------

    String HTTP_HEADER_USER_AGENT = "user-agent";

    //----------------------------------------- HTTP PARAMS -------------------------------------------

    String HTTP_PARAM_AUTH_API_KEY = "authApiKey";

    String HTTP_PARAM_REQUEST_BASE_URL = "baseUrl";
    String HTTP_PARAM_PAGE_NUMBER = "pageNum";
    String HTTP_PARAM_PAGE_SIZE = "pageSize";
    String HTTP_PARAM_AUTO_REGISTRATION_ENABLED = "autoRegistrationEnabled";
    String HTTP_PARAM_SESSION_USER_UID = "sessionUserUid";
    String HTTP_PARAM_SEARCH_FIRST_LAST_NAME = "searchFirstLastName";
    String HTTP_PARAM_SEARCH_FIRST_NAME = "searchFirstName";
    String HTTP_PARAM_SEARCH_DISPLAY_NAME = "searchDisplayName";
    String HTTP_PARAM_SEARCH_EMAIL = "searchEmail";
    String HTTP_PARAM_SEARCH_PHONE_NUMBER = "searchPhoneNumber";

    String HTTP_PARAM_SESSION_TOKEN = "sessionToken";
    String HTTP_PARAM_SNS_UID = "snsUid";
    String HTTP_PARAM_SNS_UIDS = "snsUids[]";
    String HTTP_PARAM_SNS_USER_UID = "snsUserUid";

    String SNS_SESSION_UID = "snsSessionUid";
    String SNS_SESSION_TOKEN = "snsSessionToken";

    String SNS_SESSION_UIDS = "snsSessionUids[]";
    String SNS_SESSION_TOKENS = "snsSessionTokens[]";

    String HTTP_PARAM_SNS_AUTH_STORE_CREDENTIALS = "storeCredentials";

    String HTTP_PARAM_SPLITTER = ",";

    //----------------------------------------- SNS AUTH FIELDS ----------------------------------------

    String SNS_AUTH_KEY = "snsAuth";
    String SNS_AUTH_KEY_TOKEN = "token";
    String SNS_AUTH_KEY_UID = "uid";
    String SNS_AUTH_KEY_TOKEN_VALIDATION_TIME = "tokenValidationTime";

    //----------------------------------------- SNG AUTH FIELDS ----------------------------------------

    String SNG_AUTH_KEY = "sngAuth";
    String SNG_AUTH_KEY_TOKEN = "token";

    String HTTP_HEADER_AUTHORIZATION = "Authorization";
    String AUTH_HEADER_PREFIX = "SngAuth";
    String AUTH_API_KEY = "authApiKey";
    String AUTH_SIGNATURE = "authSig";
    String AUTH_VERSION = "authVersion";

    //----------------------------------------- ACTIVITY LOG FIELDS ----------------------------------------

    String ACTIVITY_LOG_KEY = "activityLog";
    String ACTIVITY_LOG_ID = "id";
    String ACTIVITY_LOG_USER_UID = "userUid";
    String ACTIVITY_LOG_SESSION_TOKEN = "sessionToken";
    String ACTIVITY_LOG_CLIENT_API_KEY = "clientApiKey";
    String ACTIVITY_LOG_USER_AGENT = "userAgent";
    String ACTIVITY_LOG_METHOD = "method";
    String ACTIVITY_LOG_URI = "uri";
    String ACTIVITY_LOG_REQUEST_DATE = "requestDate";
    String ACTIVITY_LOG_RESPONSE_DATE = "responseDate";
    String ACTIVITY_LOG_RESPONSE_TYPE = "responseType";
    String ACTIVITY_LOG_RESPONSE_CODE = "responseCode";
    String ACTIVITY_LOG_ERROR_MESSAGE = "errorMessage";

    //----------------------------------------- SNS FIELDS ---------------------------------------------

    String SNS_KEY = "sns";
    String SNS_KEY_ID = "id";
    String SNS_KEY_ACTIVE = "active";
    String SNS_KEY_NAME = "name";
    String SNS_KEY_TYPE = "type";
    String SNS_KEY_URL = "url";

    //----------------------------------------- SNS DETAIL FIELDS --------------------------------------

    String SNS_KEY_FEATURE_CODE = "sns.feature.code";
    String SNS_KEY_FEATURE_VALUE = "sns.feature.value";

    //----------------------------------------- BATCH FIELDS --------------------------------------

    String BATCH_KEY = "batch";
    String BATCH_KEY_STATUS = "batch.status";
    String BATCH_KEY_ERROR = "batch.error";

    //----------------------------------------- VIEW ---------------------------------------------------

    String VIEW_DEFAULT = "default";
    String VIEW_SNSS = "snss";
    String VIEW_SNG_AUTH = "sngAuth";
    String VIEW_SNS_AUTH = "snsAuth";
    String VIEW_FRIENDS = "friends";
    String VIEW_FRIEND_REQUESTS_SUMMARY = "friendRequestsSummary";
    String VIEW_FRIEND_REQUESTS = "friendRequests";
    String VIEW_FRIEND_REQUEST = "friendRequest";
    String VIEW_FRIEND_SEARCH_RESULT = "friendSearchResult";
    String VIEW_MESSAGES_SUMMARY = "messagesSummary";
    String VIEW_MESSAGE_SUMMARY = "messageSummary";
    String VIEW_MESSAGE_THREADS = "messageThreads";
    String VIEW_MESSAGE_THREAD = "messageThread";
    String VIEW_MESSAGE = "message";
    String VIEW_PROFILE = "profile";
    String VIEW_PROFILES = "profiles";
    String VIEW_PROFILE_COMMENT = "profileComment";
    String VIEW_PROFILE_COMMENTS = "profileComments";
    String VIEW_PROFILE_COMMENTS_SUMMARY = "profileCommentsSummary";
    String VIEW_PROFILE_STATUS = "profileStatus";
    String VIEW_PROFILE_STATUSES = "profileStatuses";
    String VIEW_MEDIA_BROWSE_FOLDER = "mediaBrowseFolder";
    String VIEW_MEDIA_BROWSE_FILE = "mediaBrowseFile";
    String VIEW_MEDIA_FILE_COMMENTS = "mediaComments";
    String VIEW_MEDIA_FILE_COMMENT = "mediaComment";
    String VIEW_MEDIA_UPLOAD = "mediaUpload";
    String VIEW_BATCH = "batch";
    String VIEW_ACTIVITY_LOG = "activitylog";
    String VIEW_ACTIVITY_LOG_INDIVIDUAL = "activitylogIndividual";
    String VIEW_MINIFEED = "minifeed";


    String LINK_TYPE_APPLICATION_ATOM_XML = "application/atom+xml";
    String LINK_TYPE_APPLICATION_XML = "application/xml";

    /**
     * Constant variables that represent a particulair sns connector
     */
    String SNS_BEBO = "BB";
    String SNS_FACEBOOK = "FB";
    String SNS_MYSPACE = "MS";
    String SNS_PVA = "PV";
    String SNS_FAKECONNECTOR = "FC";

    //----------------------------------------- SNS PROFILE FIELDS ----------------------------------------

    String PROFILE_KEY = "snsUserProfile";
    String PROFILE_KEY_ID = "id";
    String PROFILE_KEY_SNS_ID = "snsId";
    String PROFILE_KEY_USERNAME = "username";
    String PROFILE_KEY_DISPLAY_NAME = "displayName";
    String PROFILE_KEY_AVATAR = "avatar";
    String PROFILE_KEY_EMAIL = "email";
    String PROFILE_KEY_PHONE_NUMBER = "phoneNumber";
    String PROFILE_KEY_STATUS = "status";
    String PROFILE_KEY_GENDER = "gender";
    String PROFILE_KEY_SAYING = "saying";
    String PROFILE_KEY_RELATIONSHIP_STATUS = "relationshipStatus";
    String PROFILE_KEY_UNREAD_MAIL_CNT = "unreadMailCnt";
    String PROFILE_KEY_HAS_LUV_IND = "hasLuvInd";
    String PROFILE_KEY_LAST_SIGN_IN_DTTM = "lastSignInDttm";
    String PROFILE_KEY_LAST_ACTIVE_TIMESTAMP = "lastActiveTimestamp";
    String PROFILE_KEY_BIRTH_DT = "birthDt";
    String PROFILE_KEY_FIRST_NAME = "firstName";
    String PROFILE_KEY_LAST_NAME = "lastName";
    String PROFILE_KEY_URL = "profileUrl";
    String PROFILE_KEY_AGE = "age";
    String PROFILE_KEY_ABOUT_ME = "aboutMe";
    String PROFILE_KEY_HOMETOWN = "hometown";
    String PROFILE_KEY_COUNTRY = "country";
    String PROFILE_KEY_OTHER_HALF_MEMBER_ID = "otherHalfMemberId";
    String PROFILE_KEY_OTHER_HALF = "otherHalf";
    String PROFILE_KEY_BLOG_CNT = "blogCnt";
    String PROFILE_KEY_FRIEND_CNT = "friendCnt";
    String PROFILE_KEY_PROFILE_VIEW_CNT = "profileViewCnt";
    String PROFILE_KEY_WHITE_BOARD_CNT = "whiteBoardCnt";
    String PROFILE_KEY_POLL_CNT = "pollCnt";
    String PROFILE_KEY_QUIZ_CNT = "quizCnt";
    String PROFILE_KEY_FLASH_BOX_ID = "flashBoxId";
    String PROFILE_KEY_LUZ_CNT = "luzCnt";
    String PROFILE_KEY_FAN_CNT = "fanCnt";
    String PROFILE_KEY_PROFILE_TYPE_CD = "profileTypeCD";
    String PROFILE_KEY_LONG_DISPLAY_NAME = "longDisplayName";

    String DEFAULT_FEED_DESCRIPTION = "This feed has been created by Social Networking Gateway";

    //----------------------------------------- BROWSING MEDIA FIELDS ----------------------------------

    String MEDIA_FILE_KEY = "snsMediaFile";
    String MEDIA_FILE_KEY_ID = "id";
    String MEDIA_FILE_KEY_SNS_ID = "snsId";
    String MEDIA_FILE_KEY_AUTHOR_ID = "authorId";
    String MEDIA_FILE_KEY_AUTHOR_USERNAME = "username";
    String MEDIA_FILE_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String MEDIA_FILE_KEY_PUBLISHED = "published";
    String MEDIA_FILE_KEY_FILE_SIZE_SMALL = "fileSizeSmall";
    String MEDIA_FILE_KEY_FILE_SIZE_MEDIUM = "fileSizeMedium";
    String MEDIA_FILE_KEY_FILE_SIZE_LARGE = "fileSizeLarge";
    String MEDIA_FILE_KEY_FILE_URL_SMALL = "fileUrlSmall";
    String MEDIA_FILE_KEY_FILE_URL_MEDIUM = "fileUrlMedium";
    String MEDIA_FILE_KEY_FILE_URL_LARGE = "fileUrlLarge";
    String MEDIA_FILE_KEY_DESCRIPTION = "description";
    String MEDIA_FILE_KEY_TITLE = "title";
    String MEDIA_FILE_KEY_NUM_OF_VIEWS = "numberOfViews";
    String MEDIA_FILE_KEY_PARENT_FOLDER_ID = "parentFolderId";
    String MEDIA_FILE_KEY_PARENT_FOLDER_NAME = "parentFolderName";
    String MEDIA_FILE_KEY_MIMETYPE = "mimetype";
    String MEDIA_FILE_KEY_VISIBILITY = "visibility";
    String MEDIA_FILE_KEY_TAGS = "tags";
    String MEDIA_FILE_KEY_FTAGS = "ftags";
    String MEDIA_FILE_KEY_PTAGS = "ptags";

    //----------------------------------------- PROFILE STATUS FIELDS ---------------------------------

    String PROFILE_STATUS_KEY = "snsUserProfileStatus";
    String PROFILE_STATUS_KEY_ID = "id";
    String PROFILE_STATUS_KEY_SNS_ID = "snsId";
    String PROFILE_STATUS_KEY_AUTHOR_ID = "authorId";
    String PROFILE_STATUS_KEY_AUTHOR_USERNAME = "username";
    String PROFILE_STATUS_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String PROFILE_STATUS_KEY_PUBLISHED = "published";
    String PROFILE_STATUS_KEY_STATUS = "status";

    //------------------------------------------ MEDIA COMMENT FIELDS ----------------------------------

    String MEDIA_COMMENT_KEY = "snsMediaComment";
    String MEDIA_COMMENT_KEY_ID = "id";
    String MEDIA_COMMENT_KEY_SNS_ID = "snsId";
    String MEDIA_COMMENT_KEY_AUTHOR_ID = "authorId";
    String MEDIA_COMMENT_KEY_AUTHOR_USERNAME = "username";
    String MEDIA_COMMENT_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String MEDIA_COMMENT_KEY_PUBLISHED = "published";
    String MEDIA_COMMENT_KEY_COMMENT = "comment";

    //----------------------------------------- PROFILE COMMENT FIELDS ---------------------------------

    String PROFILE_COMMENT_KEY = "snsUserProfileComment";
    String PROFILE_COMMENT_KEY_ID = "id";
    String PROFILE_COMMENT_KEY_SNS_ID = "snsId";
    String PROFILE_COMMENT_KEY_AUTHOR_ID = "authorId";
    String PROFILE_COMMENT_KEY_AUTHOR_USERNAME = "username";
    String PROFILE_COMMENT_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String PROFILE_COMMENT_KEY_PUBLISHED = "published";
    String PROFILE_COMMENT_KEY_COMMENT = "comment";
    String ALBUM_KEY = "snsAlbumData";
    String ALBUM_NUMBER_OF_PHOTOS = "numPhotos";
    String ALBUM_COVER_URL = "coverUrl";

    //----------------------------------------- FRIEND REQUEST FIELDS ---------------------------------

    String FRIEND_REQUEST_KEY = "snsFriendRequest";
    String FRIEND_REQUEST_KEY_ID = "id";
    String FRIEND_REQUEST_KEY_SNS_ID = "snsId";
    String FRIEND_REQUEST_KEY_AUTHOR_ID = "authorId";
    String FRIEND_REQUEST_KEY_AUTHOR_USERNAME = "username";
    String FRIEND_REQUEST_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String FRIEND_REQUEST_KEY_PUBLISHED = "published";
    String FRIEND_REQUEST_KEY_MESSAGE = "message";

    String SUMMARY_TOTAL = "summary_total";
    String VALUE = "value";
    String SUMMARY_NEW = "new";
    String SNG_REQUEST_DATE_TIME = "SNG_REQUEST_DATE_TIME";

    //----------------------------------------- MESSAGE FIELDS ---------------------------------

    String MESSAGE_KEY = "snsMessage";
    String MESSAGE_KEY_ID = "id";
    String MESSAGE_KEY_THREAD_ID = "threadId";
    String MESSAGE_KEY_SNS_ID = "snsId";
    String MESSAGE_KEY_AUTHOR_ID = "authorId";
    String MESSAGE_KEY_AUTHOR_USERNAME = "username";
    String MESSAGE_KEY_AUTHOR_DISPLAY_NAME = "displayName";
    String MESSAGE_KEY_PUBLISHED = "published";
    String MESSAGE_KEY_BODY = "body";
    String MESSAGE_KEY_SUBJECT = "subject";
    String MESSAGE_KEY_UNREAD = "unread";

    //----------------------------------------- MINIFEED CATEGORIES ---------------------------------

    String MINIFEED_CATEGORY_FRIENDSHIP = "friendship";
    String MINIFEED_CATEGORY_PROFILE_STATUS = "profileStatus";
    String MINIFEED_CATEGORY_PHOTO = "photo";
    String MINIFEED_CATEGORY_VIDEO = "video";
    String MINIFEED_CATEGORY_PROFILE_COMMENT = "profileComment";
    String MINIFEED_CATEGORY_PHOTO_COMMENT = "photoComment";
    String MINIFEED_CATEGORY_VIDEO_COMMENT = "videoComment";
    String MINIFEED_CATEGORY_EVENT = "event";
    String MINIFEED_CATEGORY_PROFILE = "profile";
    String MINIFEED_CATEGORY_SNS = "sns";
    String MINIFEED_CATEGORY_GROUP = "group";
    String MINIFEED_CATEGORY_CONTACT = "contact";
    String MINIFEED_CATEGORY_FRIEND_REQUEST = "friendRequest";
    String MINIFEED_CATEGORY_MESSAGE = "message";
    String MINIFEED_CATEGORY_TEXT = "Minifeed category: ";

}
