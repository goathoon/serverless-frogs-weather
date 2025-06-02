package com.weather.model.utils;

import lombok.Getter;

@Getter
public class ForecastTime {
    private final String baseDate;
    private final String baseTime;

    public ForecastTime(String baseDate, String baseTime) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
    }
}
