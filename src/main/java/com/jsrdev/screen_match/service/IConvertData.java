package com.jsrdev.screen_match.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> myGenericClass);
}
