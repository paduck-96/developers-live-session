package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public SessionRedisSaveResponse enter(SessionRedisSaveRequest request) {
        String roomId = request.getRoomId();
        Long userId = request.getUserId();
            // 1. Redis 데이터 삽입 로직 수행(캐쉬)
            redisTemplate.opsForSet().add(roomId, userId);
            redisTemplate.opsForHash().put("rooms", roomId, userId);

            // 2. 삽입한 데이터 클라이언트에 전달
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code("200 OK")
                    .msg("정상적으로 처리하였습니다.")
                    .data(redisTemplate.opsForSet().members(roomId).toString()).build();
            return response;
    }

    @Override
    public SessionRedisFindAllResponse list() {
        // 1. Redis에서 모든 채팅방 정보를 가져온다.
        Set<Object> rooms = redisTemplate.opsForHash().keys("rooms");
        Map<Object, Object> roomsInfo = new HashMap();
        for(Object room : rooms){
            roomsInfo.put(room, redisTemplate.opsForSet().members(room.toString()));
        }

        // 2. Redis에 있는 모든 채팅방 정보를 응답해야 한다.
        SessionRedisFindAllResponse response = SessionRedisFindAllResponse.builder()
                .code("200 OK")
                .msg("정상적으로 처리되었습니다.")
                .data(roomsInfo.toString())
                .build();
        return response;
    }

    @Override
    public SessionRedisRemoveResponse remove(SessionRedisRemoveRequest request) {
        String roomId = request.getRoomId();
        // 1. Redis에 request.getRoomId()를 가지고 가서 해당하는 데이터 삭제
        redisTemplate.opsForHash().delete("rooms", roomId);

        // 2. 삭제한 데이터
        SessionRedisRemoveResponse response = SessionRedisRemoveResponse.builder()
                .code("200 OK")
                .msg("정상적으로 처리되었습니다.")
                .data(redisTemplate.delete(roomId).toString())
                .build();
        return response;
    }
}
