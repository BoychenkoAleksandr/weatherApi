package com.boic.weatherapi.service;

import com.boic.weatherapi.client.ExternalWeatherApi;
import com.boic.weatherapi.mode.Mode;
import com.boic.weatherapi.model.WeatherApiResponse;
import com.boic.weatherapi.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final ExternalWeatherApi externalWeatherApi;
    private final WeatherRepository repository;
    static String APPID = null;
    static private Mode mode = null;
    public WeatherApiResponse getWeather(String city) {
        WeatherApiResponse response = repository.findCity(city, mode);
        if (response == null)
            return repository.addCity(city, externalWeatherApi.getWeather(city, APPID, "metric", "ru"));
        else
            return response;
    }

    public void getPollingWeather() {
        Timer timer = new Timer();
        timer.schedule((new TimerTask() {
            public void run() {
                System.out.println("Автоматический запрос");
                for (Map.Entry<String, WeatherApiResponse> entry : WeatherRepository.getCityMap().entrySet()) {
                    WeatherApiResponse response = externalWeatherApi.getWeather(entry.getKey(), APPID, "metric", "ru");
                    response.setDate(LocalDateTime.now());
                    entry.setValue(response);
                }
            }
        }), 0, 60000);

    }

    public void getWeatherInit(String APIKey, String modeStr) {
        APPID = APIKey;
        mode = Mode.valueOf(modeStr);
        externalWeatherApi.getWeather("London", APPID, "metric", "ru");
        if (mode.equals(Mode.POLLING)) this.getPollingWeather();
    }
}
