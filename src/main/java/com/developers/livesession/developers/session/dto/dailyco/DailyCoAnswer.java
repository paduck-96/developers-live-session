package com.developers.livesession.developers.session.dto.dailyco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DailyCoAnswer {
    private String id;
    private String name;
    private boolean api_created;
    private String privacy;
    private String url;
    private String created_at;
    private Object config;
}
