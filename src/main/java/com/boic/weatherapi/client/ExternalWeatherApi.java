package com.boic.weather.client;

import com.boic.weather.model.WeatherApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "weather")
public interface ExternalWeatherApi {
    @GetMapping
    WeatherApiResponse getWeather(
            @RequestParam(name = "q") String cityName,
            @RequestParam(name = "APPID") String APIKey,
            @RequestParam(name = "units", required = false) String units,
            @RequestParam(name = "lang", required = false) String lang
    );
}
