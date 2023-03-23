package com.developers.livesession.developers.session.controller;

import com.developers.livesession.developers.session.dto.*;
import com.developers.livesession.developers.session.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/live-session")
@RestController
public class SessionController {

    private final SessionService sessionService;

    // 멘토링룸 입장하면 Redis에 채팅방 정보 삽입
    // Redis에 삽입할 더미 데이터 형식: key(채팅장 id): value({userName, userName})
    // ex) 3fcd5bad-2004-41a8-acc9-56a12e7f755f: moon, jiduk
    @PostMapping("/enter")
    public ResponseEntity<SessionRedisSaveResponse> enter(@Valid @RequestBody SessionRedisSaveRequest request) {
        SessionRedisSaveResponse response = sessionService.enter(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멘토링룸 목록 화면에서의 모든 멘토링룸 목록 조회
    @GetMapping("/list")
    public ResponseEntity<SessionRedisFindAllResponse> list() {
        SessionRedisFindAllResponse response = sessionService.list();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/exit")
    public ResponseEntity<SessionRedisRemoveResponse> remove(@Valid @RequestBody SessionRedisRemoveRequest request) {
        SessionRedisRemoveResponse response = sessionService.remove(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
