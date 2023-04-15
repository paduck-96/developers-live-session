package com.developers.livesession.developers.session.dto.dailyco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DailyCoResponse {
    private String code;
    private String msg;
    private DailyCoAnswer data;
}
