package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
//Утилитный класс, который преобразует из Json в объект и наоборот
public class JsonHelper {// делаем упрощенную версию jackson для чтения json

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String jsonPath, Class<T> out) throws IOException {
       return mapper.readValue(new File(jsonPath), out);

    }
    public static String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}

