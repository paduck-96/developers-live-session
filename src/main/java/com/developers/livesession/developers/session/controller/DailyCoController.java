package com.developers.livesession.developers.session.controller;

import com.developers.livesession.developers.session.dto.dailyco.DailyCoCreateRequest;
import com.developers.livesession.developers.session.dto.dailyco.DailyCoResponse;
import com.developers.livesession.developers.session.service.DailyCoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/dailyco")
@Controller
public class DailyCoController {
    private final DailyCoService dailyCoService;

    @PostMapping
    public ResponseEntity<DailyCoResponse> create(@Valid @RequestBody DailyCoCreateRequest request) throws Exception{
        DailyCoResponse response = dailyCoService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<DailyCoResponse> delete(@PathVariable String roomName) throws Exception{
        DailyCoResponse response = dailyCoService.delete(roomName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
