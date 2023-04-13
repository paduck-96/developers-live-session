package com.developers.livesession.developers;

import io.lettuce.core.RedisException;
import org.apache.coyote.Response;
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
public class GlobalExceptionHandler {
    // log4j2 로 처리하고 있다보니 해당 class의 라이브러리를 잘 가져와야 함
    //로깅을 위한 Logger 인스턴스 생성
    //설정한 전역예외처리 클래스를 기준으로 적절한 값을 반환
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    // dto로 인지값이 전달되고 있고, @Valid로 체킹하고 있지만 기본적으로 매개변수 오류에 대한 로깅이 들어온다고 함
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        logger.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("허용되지 않은 매개변수 값이 입력 되었습니다. "+e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) //@valid 오류
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 매개변수 값이 입력 되었습니다. " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        logger.error("API 요청 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 API 요청입니다. " + e.getMessage());
    }

    @ExceptionHandler(Exception.class) //나머지 예외 처리
    public ResponseEntity<?> handleGeneralException(Exception e) {
        logger.error("비정상적 예외 발생: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비정상적인 예외가 발생했습니다. " + e.getMessage());
    }

    // redis 관련 오류 처리
    @ExceptionHandler(RedisConnectionFailureException.class) //연결 실패
    public ResponseEntity<?> handleRedisConnectionFailureException(RedisConnectionFailureException e){
        logger.error("Redis 연결 오류: ", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Redis 연결에 오류가 발생했습니다. "+e.getMessage());
    }
    @ExceptionHandler(RedisException.class) //추가 예외 처리
    public ResponseEntity<?> handleRedisException(RedisException e) {
        logger.error("Redis 오류: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redis에서 오류가 발생했습니다. " + e.getMessage());
    }
}
