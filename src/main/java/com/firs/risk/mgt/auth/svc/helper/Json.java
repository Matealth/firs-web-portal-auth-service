package com.firs.risk.mgt.auth.svc.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

public class Json {
    static public <T> T mapFromString(String json, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper.readValue(json, clazz);
    }
}
