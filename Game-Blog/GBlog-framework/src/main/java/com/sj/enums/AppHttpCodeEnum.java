package com.sj.enums;

// Organize all codes
public enum AppHttpCodeEnum {
    SUCCESS(200, "Success"),
    NEED_LOGIN(401, "Please login first"),
    NO_OPERATOR_AUTH(403, "No Authorization"),

    SYSTEM_ERROR(500, "System Error"),
    EMAIL_EXIST(503, "The email already exists"),
    REQUIRE_USERNAME(504, "Please enter username"),

    CONTENT_NOT_NULL(506, "Contents must not be blank"),
    FILE_TYPE_ERROR(507, "Please upload JPG files only"),
    FILE_SIZE_ERROR(507, "Please upload file size less than 2MB"),
    REGISTER_NOT_NULL(508, "Please fill in all blanks"),
    USERNAME_EXIST(501, "The user already exists"),
    PHONENUMBER_EXIST(502, "The phone already exists"),
    NICKNAME_EXIST(512, "The nickname already exists"),
    LOGIN_ERROR(505, "Incorrect username or password"),
    COMMENT_NOT_NULL(506, "Contents must not be blank"),

    TAG_IS_EXIST(507, "The tag already exists"),
    TAG_IS_NOEXIST(508, "The tag already exists"),
    CONTENT_IS_BLANK(509, "Contents must not be blank"),
    LINK_IS_EXIST(510, "The link already exists"),
    DELETE_LINK_FAIL(511, "Failed to delete the link"),
    CATEGORY_IS_EXIST(512, "The category already exists"),
    DELETE_CATEGORY_FAIL(513, "Failed to delete the category"),
    DELETE_ARTICLE_FAIL(514, "Failed to delete the article"),
    ADD_MENU_FAIL(515, "The menu already exists"),
    DELETE_MENU_REFUSE(516, "Failed to delete the menu with sub-menu"),
    DELETE_USER_REFUSE(517, "Failed to delete a logged in user"),
    USER_INFO_EXIST(518, "The user already exists"),
    ROLE_INFO_EXIST(518, "The character already exists"),
    ROLEKEY_INFO_EXIST(518, "The key already exists")
    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
