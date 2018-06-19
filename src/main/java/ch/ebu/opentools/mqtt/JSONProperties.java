package ch.ebu.opentools.mqtt;

import ch.ebu.opentools.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class JSONProperties {

    private String timestamp;
    private String payload;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public JSONProperties(String payload) {
        this.timestamp = DateUtil.stringFrom(LocalDateTime.now(ZoneOffset.UTC), "yyyy-MM-dd HH:mm:ss").concat(" UTC");
        this.payload = payload;
    }
}
