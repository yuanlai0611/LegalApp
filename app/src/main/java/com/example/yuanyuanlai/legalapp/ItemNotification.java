package com.example.yuanyuanlai.legalapp;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Creste by GongYunHao on 2018/5/31
 */
public class ItemNotification extends DataSupport{

    public static final int SHOW_DATE = 1;
    public static final int SHOW_MESSAGES = 2;

    private String detail_notification;
    private Date date;
    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDetail_notification() {
        return detail_notification;
    }

    public void setDetail_notification(String detail_notification) {
        this.detail_notification = detail_notification;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
