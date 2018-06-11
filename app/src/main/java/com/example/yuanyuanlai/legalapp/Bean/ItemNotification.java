package com.example.yuanyuanlai.legalapp.Bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Creste by GongYunHao on 2018/5/31
 */
public class ItemNotification extends DataSupport {

    public static final int SHOW_DATE = 1;

    public static final int SHOW_MESSAGES = 2;

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        content = mContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String mCreateTime) {
        createTime = mCreateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    private String content;
    private String createTime;
    private String title;
    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


}

