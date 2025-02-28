package com.boic.weatherapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class WeatherApiResponse {

    LocalDateTime date;
    String name;
    Main main;
    public record Main(double temp, double feels_like) {
    }
    List<Weather> weather;
    public record Weather(String description) {

    }
}
