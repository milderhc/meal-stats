package com.mealstats.mealstats.util;

/**
 * Created by milder on 10/09/16.
 */
public class Constants {

    public static final String PICTURE_URI;

    public static final int REQUEST_IMAGE_CAPTURE;
    public static final int REQUEST_IMAGE_SELECT;
    public static final String PICTURE_DIRECTORY_NAME;
    public static final String PICTURE_SELECTED_NAME;

    public static final int COMPRESS_QUALITY;

    public static final String NUTRITIONAL_INFO_ARGS;
    public static final String NAME_MEAL_STAT_RESPONSE;

    public static final String IS_LOGGED;
    public static final boolean IS_LOGGED_DEFAULT_VALUE;
    public static final String EMAIL;

    public static final String FACEBOOK_FIRST_NAME;
    public static final String FACEBOOK_LASTNAME;
    public static final String FACEBOOK_EMAIL;
    public static final String REQUESTS_HISTORY = "REQUESTS_HISTORY";

    static {
        NUTRITIONAL_INFO_ARGS = "NUTRITIONAL_INFO_ARGS";
        NAME_MEAL_STAT_RESPONSE = "category";

        PICTURE_URI = "pictureUri";
        PICTURE_DIRECTORY_NAME = "img";
        PICTURE_SELECTED_NAME = "img_to_analyze.jpg";

        REQUEST_IMAGE_SELECT = 2;
        REQUEST_IMAGE_CAPTURE = 1;

        //0-100
        COMPRESS_QUALITY = 0;

        IS_LOGGED = "is_logged";
        IS_LOGGED_DEFAULT_VALUE = false;
        EMAIL = "email";

        FACEBOOK_FIRST_NAME = "first_name";
        FACEBOOK_LASTNAME = "last_name";
        FACEBOOK_EMAIL = "email";
    }
}
