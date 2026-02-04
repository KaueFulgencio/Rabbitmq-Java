package kaue.dev.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "td_bus_status")
public class BusStatusEntity {

    @Id
    private String id;

    private String lineName;
    private String lineNumber;
    private boolean active;
    private double latitude;
    private double longitude;
    private Instant timestamp;

    public BusStatusEntity() {}

    public BusStatusEntity(String lineName, String lineNumber, boolean active,
                           double latitude, double longitude, Instant timestamp) {
        this.lineName = lineName;
        this.lineNumber = lineNumber;
        this.active = active;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
