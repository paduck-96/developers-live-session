package com.developers.livesession.developers.session.dto;

import com.developers.livesession.developers.session.constant.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionRedisSaveRequest {
    @NotNull
    private String roomId;
    @NotNull
    private Long userId;
}
