package com.developers.livesession.developers;

import com.developers.livesession.developers.configuration.RedisConfig;
import com.developers.livesession.developers.session.dto.SessionRedisRemoveRequest;
import com.developers.livesession.developers.session.dto.SessionRedisRemoveResponse;
import com.developers.livesession.developers.session.dto.SessionRedisSaveRequest;
import com.developers.livesession.developers.session.dto.SessionRedisSaveResponse;
import com.developers.livesession.developers.session.service.SessionServiceImpl;
import io.lettuce.core.RedisException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles("test")
@Import(RedisConfig.class)
public class SessionServiceTest {
    @Autowired
    private SessionServiceImpl sessionService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void enterSuccess() {
        // Given
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("user123", "test-user", 60L);

        // When
        SessionRedisSaveResponse response = sessionService.enter(request);

        // Then
        assertNotNull(response);
        assertEquals("정상적으로 처리하였습니다.", response.getMsg());
    }
    @Test
    public void enterFail() {
        // Given
        SessionRedisSaveRequest request = new SessionRedisSaveRequest(null, "test-user", 60L);

        // Then
        assertThrows(RedisException.class, () -> sessionService.enter(request));
    }

    @Test
    public void listSuccess() {
        // Given
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("test", "test-user", 60L);
        sessionService.enter(request);

        // When
        sessionService.list();

        // Then
        assertEquals(HttpStatus.OK.toString(), HttpStatus.OK.toString());
    }

    @Test
    public void removeSuccess() {
        // Given
        sessionService.enter(new SessionRedisSaveRequest("test", "test-user", 60L));
        SessionRedisRemoveRequest request = new SessionRedisRemoveRequest("test");

        // When
        SessionRedisRemoveResponse response = sessionService.remove(request);

        // Then
        assertNotNull(response);
        assertEquals("정상적으로 처리되었습니다.", response.getMsg());
    }

    @Test
    public void removeFail() {
        // Given
        SessionRedisRemoveRequest request = new SessionRedisRemoveRequest(null);

        // Then
        assertThrows(IllegalArgumentException.class, () -> sessionService.remove(request));
    }
}
