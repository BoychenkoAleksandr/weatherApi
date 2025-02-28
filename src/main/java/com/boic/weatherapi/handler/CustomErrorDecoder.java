package com.boic.weatherapi.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if (response.reason().equals("Not Found"))
            return new RuntimeException("Введеный вами город не найден");
        else if (response.reason().equals("Unauthorized"))
            return new RuntimeException("Введен неверный API ключ");
        else return new RuntimeException("Неизвестная ошибка");
    }
}
