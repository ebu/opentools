package ch.ebu.opentools.mqtt;

import ch.ebu.opentools.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class JSONProperties {

    private String timestamp;
    private String projectId;
    private String stageId;
    private String message;

    public JSONProperties(String projectId, String message) {
        this.timestamp = DateUtil.stringFrom(LocalDateTime.now(ZoneOffset.UTC), "yyyy-MM-dd HH:mm:ss").concat(" UTC");
        this.projectId = projectId;
        this.stageId = "";
        this.message = message;
    }

    public JSONProperties(String projectId, String stageId, String message) {
        this.timestamp = DateUtil.stringFrom(LocalDateTime.now(ZoneOffset.UTC), "yyyy-MM-dd HH:mm:ss").concat(" UTC");
        this.projectId = projectId;
        this.stageId = stageId;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
