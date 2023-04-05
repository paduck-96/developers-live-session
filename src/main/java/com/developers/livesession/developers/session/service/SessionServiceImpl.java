package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.*;
import lombok.RequiredArgsConstructor;
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

    @Override
    public SessionRedisSaveResponse enter(SessionRedisSaveRequest request) {
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
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .msg(e.getMessage())
                    .data(null).build();
            return response;
        }
    }

    @Override
    public SessionRedisFindAllResponse list() {
        try{
            // 1. Redis에서 모든 채팅방 정보를 가져온다.
            Set<Object> rooms = redisTemplate.opsForHash().keys("rooms");
            Map<Object, Object> roomsInfo = new HashMap();
            for(Object room : rooms){
                roomsInfo.put(room, redisTemplate.opsForSet().members(room.toString()));
            }

            if(roomsInfo.isEmpty()){
                return SessionRedisFindAllResponse.builder()
                        .code(HttpStatus.NO_CONTENT.toString())
                        .msg("생성된 방이 없습니다")
                        .data("{}")
                        .build();
            }
            // 2. Redis에 있는 모든 채팅방 정보를 응답해야 한다.
            SessionRedisFindAllResponse response = SessionRedisFindAllResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 처리되었습니다.")
                    .data(roomsInfo.toString())
                    .build();
            return response;
        }catch(Exception e){
            return SessionRedisFindAllResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .msg(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public SessionRedisRemoveResponse remove(String roomName) {
        try{
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
            return SessionRedisRemoveResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .msg(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
