package com.developers.livesession.developers.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionRedisSaveRequest {
    @NotNull
    private String roomName;
    @NotNull
    private String userName;

    @NotNull
    private Long time;
}
