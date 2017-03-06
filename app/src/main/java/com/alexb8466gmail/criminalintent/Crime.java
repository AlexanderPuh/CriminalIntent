package com.alexb8466gmail.criminalintent;

import android.widget.TimePicker;

import java.util.Date;
import java.util.Timer;
import java.util.UUID;

/**
 * Created by User on 06.02.2017.
 */

public class Crime {


    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime(){
//Генерация
        this(UUID.randomUUID());

    }
public Crime(UUID id){
    mId=id;
    mDate=new Date();
}
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }
public String getPhotoFileName(){

    return "IMG_"+getId().toString()+".jpg";
}
}
