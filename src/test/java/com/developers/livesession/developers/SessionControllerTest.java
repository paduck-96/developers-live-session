package com.developers.livesession.developers;

import com.developers.livesession.developers.config.RedisConfig;
import com.developers.livesession.developers.session.dto.SessionRedisSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    public void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    public void testEnter() throws Exception {
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("room1", 1L, 30L);

        mockMvc.perform(post("/live-session/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("enter"));
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(get("/live-session/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("list"));
    }

    @Test
    public void testExit() throws Exception {
        // 방 생성 후 삭제
        SessionRedisSaveRequest request = new SessionRedisSaveRequest("room1", 1L, 30L);
        mockMvc.perform(post("/live-session/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        String roomName = request.getRoomId();
        mockMvc.perform(delete("/live-session/exit/{roomName}", roomName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("exit"));
    }
}
