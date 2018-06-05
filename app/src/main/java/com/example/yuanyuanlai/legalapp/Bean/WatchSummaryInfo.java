package com.example.yuanyuanlai.legalapp.Bean;

import java.util.Date;

public class WatchSummaryInfo {

    private String deviceId;
    private Integer heartRate;
    private String longitude;// 经度
    private String latitude;// 纬度
    private Integer sportSteps;
    private Integer sportCal;
    private Integer sportEnergy;
    private Integer gpsId;
    private Date timestamp;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
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

    public Integer getSportSteps() {
        return sportSteps;
    }

    public void setSportSteps(Integer sportSteps) {
        this.sportSteps = sportSteps;
    }

    public Integer getSportCal() {
        return sportCal;
    }

    public void setSportCal(Integer sportCal) {
        this.sportCal = sportCal;
    }

    public Integer getSportEnergy() {
        return sportEnergy;
    }

    public void setSportEnergy(Integer sportEnergy) {
        this.sportEnergy = sportEnergy;
    }

    public Integer getGpsId() {
        return gpsId;
    }

    public void setGpsId(Integer gpsId) {
        this.gpsId = gpsId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
