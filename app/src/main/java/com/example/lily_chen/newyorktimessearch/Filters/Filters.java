package com.example.lily_chen.newyorktimessearch.Filters;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by lily_chen on 10/27/16.
 */
@Parcel
public class Filters {

    public Date getBeginDate() {
        return beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public Date beginDate;
    public String sortOrder;
    public String newsDesk;

    public Filters() {
        beginDate = null;
        sortOrder = "";
        newsDesk = "";
    }

    public Filters(Date beginDate, String sortOrder, String newsDesk) {
        this.beginDate = beginDate;
        this.sortOrder = sortOrder;
        this.newsDesk = newsDesk;
    }
}
