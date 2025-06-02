package com.weather.utils;

import com.weather.model.utils.ForecastTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ForecastTimeUtils {

    private static final long SPAN_MINUTES = 11;

    public static ForecastTime getLatestForecastTime() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(SPAN_MINUTES);

        return new ForecastTime(
                String.format("%02d00",now.getHour()),
                now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
    }


}
