package com.developers.livesession.developers;

import com.developers.livesession.developers.config.RedisConfig;
import com.developers.livesession.developers.session.dto.SessionRedisRemoveRequest;
import com.developers.livesession.developers.session.dto.SessionRedisSaveRequest;
import com.developers.livesession.developers.session.service.SessionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(RedisConfig.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @MockBean
    private SessionServiceImpl sessionService;


    @BeforeEach
    public void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    public void testEnter() throws Exception {
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("room1", "test-user", 30L);

        mockMvc.perform(post("/api/live-session/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("enter"));
    }

    @Test
    void testEnterException() throws Exception {
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("user123", "test-user",60L);

        Mockito.doThrow(new RedisException("Redis 세션 저장에 요류가 발생하였습니다. "))
                .when(sessionService).enter(Mockito.any(SessionRedisSaveRequest.class));

        mockMvc.perform(post("/api/live-sessions/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andDo(document("error/enter"));
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(get("/api/live-session/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("list"));
    }
    @Test
    void testListException() throws Exception {
        Mockito.doThrow(new RedisException("현재 Redis 세션이 없습니다. "))
                .when(sessionService).list();

        mockMvc.perform(get("/api/live-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("현재 Redis 세션이 없습니다. "))
                .andDo(document("error/list"));
    }

    @Test
    public void testExit() throws Exception{
        // 방 생성 후 삭제
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("room1", "test-user", 30L);
        mockMvc.perform(post("/api/live-session/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        SessionRedisRemoveRequest request2 = new SessionRedisRemoveRequest("room1");
        mockMvc.perform(delete("/api/live-session/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andDo(document("exit"));
    }
    @Test
    void removeLiveSession_withError() throws Exception {
        SessionRedisRemoveRequest request = new SessionRedisRemoveRequest("session-room-test");

        Mockito.doThrow(new IllegalArgumentException("Redis에서 해당 세션은 존재하지 않습니다. "))
                .when(sessionService).remove(request);

        mockMvc.perform(delete("/api/live-sessions/{sessionId}", request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Redis에서 해당 세션은 존재하지 않습니다. "))
                .andDo(document("error/exit"));
    }
}
