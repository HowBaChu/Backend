package com.HowBaChu.howbachu.domain.constants;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ResponseCode {

    /* OPIN */
    OPIN_LIST(HttpStatus.OK, "200 OK", "Opin - Find successfully"),
    OPIN_SAVE(HttpStatus.CREATED, "201 Created", "Opin - Create successfully"),
    OPIN_UPDATE(HttpStatus.NO_CONTENT, "204 No Content", "Opin - Modified successfully"),
    OPIN_DELETE(HttpStatus.NO_CONTENT, "204 No Content", "Opin - Delete successfully"),

    /* MEMBER */
    MEMBER_SAVE(HttpStatus.CREATED,"201 Created","Memeber - Signup successfully"),
    MEMBER_LOGIN(HttpStatus.OK,"200 OK","Memeber - Login successfully"),

    /* TOPIC */
    TOPIC_GET(HttpStatus.OK, "200 OK", "Topic - Find successfully"),
    TOPIC_CREATE(HttpStatus.CREATED, "201 Created", "Topic - Create successfully"),
    TOPIC_UPDATE(HttpStatus.NO_CONTENT, "204 No Content", "Topic - Modified successfully"),
    TOPIC_DELETE(HttpStatus.NO_CONTENT,"204 No Content", "Topic - Delete successfully"),

    VOTING_SUCCESS(HttpStatus.CREATED, "201 Created", "Vote - successfully");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
