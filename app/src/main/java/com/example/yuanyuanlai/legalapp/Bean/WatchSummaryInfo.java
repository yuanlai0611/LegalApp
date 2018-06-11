package com.example.yuanyuanlai.legalapp.Bean;

import java.util.Date;

public class WatchSummaryInfo {
    private String deviceId;

    private int gpsId;

    private int heartRate;

    private String latitude;//纬度

    private String longitude;//经度

    private int sportCal;

    private int sportEnergy;

    private int sportSteps;

    private String timestamp;

    public WatchSummaryInfo(String deviceId, int gpsId, int heartRate, String latitude, String longitude, int sportCal, int sportEnergy, int sportSteps, String timestamp) {
        this.deviceId = deviceId;
        this.gpsId = gpsId;
        this.heartRate = heartRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sportCal = sportCal;
        this.sportEnergy = sportEnergy;
        this.sportSteps = sportSteps;
        this.timestamp = timestamp;
    }

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }
    public String getDeviceId(){
        return this.deviceId;
    }
    public void setGpsId(int gpsId){
        this.gpsId = gpsId;
    }
    public int getGpsId(){
        return this.gpsId;
    }
    public void setHeartRate(int heartRate){
        this.heartRate = heartRate;
    }
    public int getHeartRate(){
        return this.heartRate;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public void setSportCal(int sportCal){
        this.sportCal = sportCal;
    }
    public int getSportCal(){
        return this.sportCal;
    }
    public void setSportEnergy(int sportEnergy){
        this.sportEnergy = sportEnergy;
    }
    public int getSportEnergy(){
        return this.sportEnergy;
    }
    public void setSportSteps(int sportSteps){
        this.sportSteps = sportSteps;
    }
    public int getSportSteps(){
        return this.sportSteps;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public String getTimestamp(){
        return this.timestamp;
    }

}
