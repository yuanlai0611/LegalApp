package com.example.yuanyuanlai.legalapp.Bean;

import java.util.List;

public class NotificationArrayBean {

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status mStatus) {
        status = mStatus;
    }

    public List<NotificationBean> getObject() {
        return object;
    }

    public void setObject(List<NotificationBean> mObject) {
        object = mObject;
    }

    public List<NotificationBean> object;

    private Status status;

}
