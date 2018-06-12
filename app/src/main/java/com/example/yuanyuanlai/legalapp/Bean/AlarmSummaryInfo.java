package com.example.yuanyuanlai.legalapp.Bean;

import java.util.Date;

public class AlarmSummaryInfo {

    private int typeId;
    private String deviceId;
    private String longitude; // 经度
    private String latitude;  // 纬度
    private String timestamp;

    public AlarmSummaryInfo(int typeId, String deviceId, String longitude, String latitude, String timestamp) {
        this.typeId = typeId;
        this.deviceId = deviceId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
