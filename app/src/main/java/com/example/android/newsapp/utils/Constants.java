package com.example.android.newsapp.utils;

public class Constants {

    private Constants() {
    }

    static final String JSON_KEY_RESPONSE = "response";
    static final String JSON_KEY_RESULTS = "results";
    static final String JSON_KEY_WEB_TITLE = "webTitle";
    static final String JSON_KEY_SECTION_NAME = "sectionName";
    static final String JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
    static final String JSON_KEY_WEB_URL = "webUrl";
    static final String JSON_KEY_BYLINE = "byline";
    static final String JSON_KEY_FIELDS = "fields";
    static final String JSON_KEY_THUMBNAIL = "thumbnail";

    static final int READ_TIMEOUT = 10000; /* milliseconds */

    static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    static final int SUCCESS_RESPONSE_CODE = 200;

    static final String REQUEST_METHOD_GET = "GET";

    public static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search";
}
