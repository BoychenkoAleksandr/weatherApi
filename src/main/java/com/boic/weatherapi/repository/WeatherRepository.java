package com.boic.weatherapi.repository;

import com.boic.weatherapi.mode.Mode;
import com.boic.weatherapi.model.WeatherApiResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class WeatherRepository {

    @Getter
    private static CustomLinkedHashMap<String, WeatherApiResponse> cityMap = new CustomLinkedHashMap<>(10);

    public WeatherApiResponse addCity(String city, WeatherApiResponse response) {
        response.setDate(LocalDateTime.now());
        cityMap.put(city, response);
        return response;
    }

    public WeatherApiResponse findCity(String city, Mode mode) {
        if (cityMap.containsKey(city)) {
            if (mode.equals(Mode.POLLING) || cityMap.get(city).getDate().isAfter(LocalDateTime.now().minusMinutes(10L)))
                return cityMap.get(city);
            else
                cityMap.remove(city);
        }
        return null;
    }

}
