package ch.ebu.opentools.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONMessage {

    public static String generate(JSONProperties properties) throws IOException {
        if (properties != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(System.out, properties);
            return mapper.writeValueAsString(properties);
        }
        return null;
    }
}
