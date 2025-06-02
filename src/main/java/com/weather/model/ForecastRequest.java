package com.weather.model;

import com.weather.model.utils.ForecastTime;
import lombok.Getter;

import static com.weather.utils.ForecastTimeUtils.getLatestForecastTime;

@Getter
public class ForecastRequest {
    private final String dataType = "JSON";
    private final String serviceKey = System.getenv("ENCODING_API_KEY");   // 공공데이터 인증키
    private final int numOfRows = 10;        // 한 페이지 결과 수 (기본값 10)
    private final int pageNo = 1;            // 페이지 번호 (기본값 1)

    private final String baseDate;     // 발표일자 (yyyyMMdd)
    private final String baseTime;     // 발표시각 (HHmm)
    private final int nx;              // 예보지점 X 좌표
    private final int ny;              // 예보지점 Y 좌표

    private ForecastRequest(String baseDate, String baseTime, int nx, int ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }

    public static ForecastRequest ofNow(int nx, int ny) {
        ForecastTime latestForecastTime = getLatestForecastTime();
        return new ForecastRequest(
                latestForecastTime.getBaseDate(),
                latestForecastTime.getBaseTime(),
                nx,
                ny
        );
    }
}

