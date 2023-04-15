package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.dailyco.DailyCoCreateRequest;
import com.developers.livesession.developers.session.dto.dailyco.DailyCoResponse;

public interface DailyCoService {
    DailyCoResponse create(DailyCoCreateRequest request);
    DailyCoResponse delete(String roomName);
}
