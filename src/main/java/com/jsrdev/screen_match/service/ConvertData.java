package com.jsrdev.screen_match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> myGenericClass) {
        try {
            return objectMapper.readValue(json, myGenericClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
