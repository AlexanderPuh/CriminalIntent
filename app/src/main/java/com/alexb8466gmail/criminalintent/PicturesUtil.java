package com.alexb8466gmail.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by User on 25.02.2017.
 */

public class PicturesUtil {

    public static Bitmap getScaledBitMap(String path, int destWidth, int destHeight){

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if (srcHeight>destHeight||srcWidth>destWidth){
            if (srcWidth>srcHeight){
                inSampleSize=Math.round(srcHeight/destHeight);
            }else {
                inSampleSize= Math.round(srcWidth/destWidth);
            }

        }
        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        return BitmapFactory.decodeFile(path,options);
    }
    public static Bitmap getScaledBitMap(String path, Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitMap(path, size.x, size.y);
    }
}