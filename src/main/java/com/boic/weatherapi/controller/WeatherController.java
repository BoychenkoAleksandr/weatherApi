package com.boic.weatherapi.controller;

import com.boic.weatherapi.model.WeatherApiResponse;
import com.boic.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService service;

    @GetMapping("/")
    public String auth() {
        return "auth";
    }

    @GetMapping("/mainPageWithoutAuth")
    public String mainPageWithoutAuth(@RequestParam(name="APIKey") String APIKey,
                                      @RequestParam(name="mode") String mode,
                                      Model model) {
        try {
            service.getWeatherInit(APIKey, mode);
        } catch (RuntimeException e) {
            if (e.getClass().getName().equals("feign.RetryableException"))
                model.addAttribute("exception", "Проблема доступа к серверу. Проверьте ваше подключение к интернету");
            else
                model.addAttribute("exception", e.getMessage());
            return "exceptionWithoutAuth";
        }
        return "mainPage";
    }

    @GetMapping("/mainPage")
    public String mainPage() {
        return "mainPage";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam(name="city") String city, Model model) {
        WeatherApiResponse response;
        try {
            response = service.getWeather(city);
        } catch (RuntimeException e) {
            if (e.getClass().getName().equals("feign.RetryableException"))
                model.addAttribute("exception", "Проблема доступа к серверу. Проверьте ваше подключение к интернету");
            else model.addAttribute("exception", e.getMessage());
            if (e.getMessage().equals("Введен неверный API ключ")) {
                return "exceptionWithoutAuth";
            } else return "exception";
        }
        model.addAttribute("city", response.getName());
        model.addAttribute("description", response.getWeather().get(0).description());
        model.addAttribute("temp", response.getMain().temp());
        model.addAttribute("feelsLike", response.getMain().feels_like());
        return "info";
    }
}
