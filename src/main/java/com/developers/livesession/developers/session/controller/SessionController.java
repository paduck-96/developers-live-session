package com.developers.livesession.developers.session.controller;

import com.developers.livesession.developers.session.dto.*;
import com.developers.livesession.developers.session.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/live-session")
@Controller
public class SessionController {

    private final SessionService sessionService;

    // 멘토링룸 입장하면 Redis에 채팅방 정보 삽입
    @PostMapping("/enter")
    public ResponseEntity<SessionRedisSaveResponse> enter(@Valid @RequestBody SessionRedisSaveRequest request) throws Exception {
        SessionRedisSaveResponse response = sessionService.enter(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멘토링룸 목록 화면에서의 모든 멘토링룸 목록 조회
    @GetMapping("/list")
    public ResponseEntity<SessionRedisFindAllResponse> list() throws Exception {
        SessionRedisFindAllResponse response = sessionService.list();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/exit/{roomName}")
    public ResponseEntity<SessionRedisRemoveResponse> remove(@Valid @PathVariable String roomName) throws Exception{
        SessionRedisRemoveResponse response = sessionService.remove(roomName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
