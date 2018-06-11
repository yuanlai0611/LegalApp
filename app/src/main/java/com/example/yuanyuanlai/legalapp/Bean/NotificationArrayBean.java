package com.example.yuanyuanlai.legalapp.Bean;

public class NotificationArrayBean {

    public NotificationBean[] getObject() {
        return object;
    }

    public void setObject(NotificationBean[] mObject) {
        object = mObject;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status mStatus) {
        status = mStatus;
    }

    private NotificationBean[] object;

    private Status status;

}
