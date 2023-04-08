package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.session.dto.*;

public interface SessionService {
    SessionRedisSaveResponse enter(SessionRedisSaveRequest request);
    SessionRedisFindAllResponse list();

    SessionRedisRemoveResponse remove(String roomName);
}
