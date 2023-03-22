package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.*;

public class SessionServiceImpl implements SessionService {
    @Override
    public SessionRedisSaveResponse enter(SessionRedisSaveRequest request) {
        String roomId = request.getRoomId();
        if(request.getType().equals("FIRST")) {
            // 1. Redis 데이터 삽입 로직 수행

            // 2. 삽입한 데이터 클라이언트에 전달
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code("200 OK")
                    .msg("정상적으로 처리하였습니다.")
                    .data("삽입한 Redis의 채팅방 정보를 응답해야 한다.").build();
            return response;
        } else {
            // 1. Redis 데이터 수정 로직 수행

            // 2. 수정한 데이터 클라이언트에 전달
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code("200 OK")
                    .msg("정상적으로 처리하였습니다.")
                    .data("수정한 Redis의 채팅방 정보를 응답해야 한다.").build();
            return response;
        }
    }

    @Override
    public SessionRedisFindAllResponse list() {
        // 1. Redis에서 모든 채팅방 정보를 가져온다.

        // 2. Redis에 있는 모든 채팅방 정보를 응답해야 한다.
        SessionRedisFindAllResponse response = SessionRedisFindAllResponse.builder()
                .code("200 OK")
                .msg("정상적으로 처리되었습니다.")
                .data("가져온 Redis의 모든 채팅장 정보, Hash? Map?")
                .build();
        return response;
    }

    @Override
    public SessionRedisRemoveResponse remove(SessionRedisRemoveRequest request) {
        // 1. Redis에 request.getRoomId()를 가지고 가서 해당하는 데이터 삭제

        // 2. 삭제한 데이터
        SessionRedisRemoveResponse response = SessionRedisRemoveResponse.builder()
                .code("200 OK")
                .msg("정상적으로 처리되었습니다.")
                .data("삭제한 채팅방 정보")
                .build();
        return response;
    }
}
