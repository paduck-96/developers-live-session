package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.*;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisTemplate<String, Object> redisTemplate; // 실제 서비스 redisTemplate
    private static final Logger logger = LogManager.getLogger(SessionServiceImpl.class); // 서비스 코드에서 오류 날 경우 로깅

    @Override
    public SessionRedisSaveResponse enter(SessionRedisSaveRequest request){
        String roomId = request.getRoomId();
        Long userId = request.getUserId();
        Long expireTime = request.getTime();

        try{
            // 1. Redis 데이터 삽입 로직 수행
            redisTemplate.opsForSet().add(roomId, userId);
            redisTemplate.expire(roomId, Duration.ofMinutes(expireTime));
            redisTemplate.opsForHash().put("rooms", roomId, userId);

            // 2. 삽입한 데이터 클라이언트에 전달
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 처리하였습니다.")
                    .data(redisTemplate.opsForSet().members(roomId).toString()).build();
            return response;
        }catch (Exception e){
            logger.error("Redis 세션 저장 오류! 방 정보: "+roomId+" 사용자 정보: "+userId, e);
            throw new RedisException("Redis 세션 저장에 요류가 발생하였습니다. ", e);
        }
    }

    @Override
    public SessionRedisFindAllResponse list(){
        try{
            // 1. Redis에서 모든 채팅방 정보를 가져온다.
            Set<Object> rooms = redisTemplate.opsForHash().keys("rooms");
            Map<Object, Object> roomsInfo = new HashMap();
            for(Object room : rooms){
                roomsInfo.put(room, redisTemplate.opsForSet().members(room.toString()));
            }

            if(rooms == null || rooms.isEmpty()){
                logger.error("Redis 세션 전체 출력 오류! ");
                throw new InvalidDataAccessApiUsageException("현재 Redis 세션이 없습니다. ");
            }
            // 2. Redis에 있는 모든 채팅방 정보를 응답해야 한다.
            SessionRedisFindAllResponse response = SessionRedisFindAllResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 처리되었습니다.")
                    .data(roomsInfo.toString())
                    .build();
            return response;
        }catch(RedisException e){
            logger.error("Redis 세션 전체 출력 오류! ",e);
            throw new IllegalArgumentException("Redis 세션 전체를 불러오는데 오류가 발생했습니다. ",e);
        }
    }

    @Override
    public SessionRedisRemoveResponse remove(String roomName){
        try{
            if(!redisTemplate.opsForHash().hasKey("rooms", roomName)){
                logger.error("Redis 세션 삭제 오류! ", roomName);
                throw new IllegalArgumentException("Redis에서 해당 세션은 존재하지 않습니다. ");
            }
            // 1. Redis에 request.getRoomId()를 가지고 가서 해당하는 데이터 삭제
            redisTemplate.opsForHash().delete("rooms", roomName);

            // 2. 삭제한 데이터
            SessionRedisRemoveResponse response = SessionRedisRemoveResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 처리되었습니다.")
                    .data(String.valueOf(redisTemplate.delete(roomName)))
                    .build();
            return response;
        }catch (Exception e){
            logger.error("Redis 세션 삭제 오류! ", e);
            throw new IllegalArgumentException("Redis에서 세션을 삭제하는데 오류가 발생했습니다. ", e);
        }
    }
}
