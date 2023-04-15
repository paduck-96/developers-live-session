package com.developers.livesession.developers.session.dto.dailyco;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DailyCoCreateRequestProperties {
    private Long nbf;
    private Long exp;
}
