package com.developers.livesession.developers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DailyCoException extends RuntimeException{

    private final String msg;
    private final int statusCode;
}
