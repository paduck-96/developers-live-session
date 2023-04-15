package com.developers.livesession.developers.session.dto.dailyco;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DailyCoCreateRequest {
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "이름에는 알파벳, 하이픈 과 언더바만 들어올 수 있습니다")
    @Size(max = 40, message = "이름은 40자를 초과할 수 없습니다")
    private String name;
    @NotNull
    private String privacy;
    private DailyCoCreateRequestProperties properties;
}
