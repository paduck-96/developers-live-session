package com.developers.livesession.developers;

import com.developers.livesession.developers.exception.DailyCoException;
import io.lettuce.core.RedisException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //응답을 json로 처리 가능
@Log4j2
public class GlobalExceptionHandler {

    // dto로 인지값이 전달되고 있고, @Valid로 체킹하고 있지만 기본적으로 매개변수 오류에 대한 로깅이 들어온다고 함
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        log.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("허용되지 않은 매개변수 값이 입력 되었습니다. "+e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) //@valid 오류
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 매개변수 값이 입력 되었습니다. " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        log.error("API 요청 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 API 요청입니다. " + e.getMessage());
    }

    @ExceptionHandler(Exception.class) //나머지 예외 처리
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error("비정상적 예외 발생: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비정상적인 예외가 발생했습니다. " + e.getMessage());
    }

    // redis 관련 오류 처리
    @ExceptionHandler(RedisConnectionFailureException.class) //연결 실패
    public ResponseEntity<?> handleRedisConnectionFailureException(RedisConnectionFailureException e){
        log.error("Redis 연결 오류: ", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Redis 연결에 오류가 발생했습니다. "+e.getMessage());
    }
    @ExceptionHandler(RedisException.class) //추가 예외 처리
    public ResponseEntity<?> handleRedisException(RedisException e) {
        log.error("Redis 오류: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redis에서 오류가 발생했습니다. " + e.getMessage());
    }

    // dailyco 관련 오류 처리
    @ExceptionHandler(DailyCoException.class)
    public ResponseEntity<?> handleDailyCoException(DailyCoException e){
        log.error("DailyCo 오류ㅣ ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DailyCo에서 오류가 발생하였습니다. "+e.getMessage());
    }

}
