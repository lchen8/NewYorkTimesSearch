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

    public Date getEndDate() {
        return endDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public Date beginDate;
    public Date endDate;
    public String sortOrder;
    public String newsDesk;

    public Filters() {
        beginDate = null;
        endDate = null;
        sortOrder = "";
        newsDesk = "";
    }

    public Filters(Date beginDate, Date endDate, String sortOrder, String newsDesk) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.sortOrder = sortOrder;
        this.newsDesk = newsDesk;
    }
}
