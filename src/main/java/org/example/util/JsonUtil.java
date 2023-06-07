package org.example.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static ObjectMapper objMapper;

    static {
        initObjectMapper();
    }

    public static ObjectMapper initObjectMapper() {
        if (!ObjectUtils.isEmpty(objMapper)) {
            return objMapper;
        }
        objMapper = new ObjectMapper();
        objMapper.findAndRegisterModules();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objMapper;
    }

    public static String toString(Object object) throws JsonProcessingException {
        return objMapper.writer()
                .withDefaultPrettyPrinter()
                .writeValueAsString(object);
    }

    public static <T> List<T> toList(String jsonString) throws JsonProcessingException {
        List<T> result = objMapper.readValue(jsonString, new TypeReference<>() {
        });
        return result;
    }

    public static Map<String, Object> toMap(String jsonString) throws JsonProcessingException {
        return objMapper.readValue(jsonString, Map.class);
    }
}
