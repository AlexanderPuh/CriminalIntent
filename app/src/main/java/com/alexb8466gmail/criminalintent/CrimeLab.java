package com.alexb8466gmail.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeCursorWrapper;
import database.CrimeDbSchema.CrimeTable;

/**
 * Created by User on 09.02.2017.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private File mPhotoFile;

    private CrimeLab(Context context) {
        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();



    }


    public List<Crime> getCrimes() {
            List<Crime> crimes=new ArrayList<>();

            CrimeCursorWrapper cursor=queryCrimes(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }



        return crimes;
    }

    public static CrimeLab getCrimeLab(Context context) {

        if(sCrimeLab==null){

            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    public Crime getCrime(UUID id){
CrimeCursorWrapper cursor=queryCrimes(CrimeTable.cols.UUID+"=?", new String[]{id.toString()});
        try {
            if (cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }


    }
    public void addCrime(Crime c) {
ContentValues values=getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);

    }

private static ContentValues getContentValues(Crime crime){
    ContentValues values=new ContentValues();
    values.put(CrimeTable.cols.UUID, crime.getId().toString());
    values.put(CrimeTable.cols.TITLE, crime.getTitle());
    values.put(CrimeTable.cols.DATE, crime.getDate().getTime());
    values.put(CrimeTable.cols.SOLVED, crime.isSolved()?1:0);
    values.put(CrimeTable.cols.SUSPECT, crime.getSuspect());

    return values;


}
    public void updateCrime(Crime crime){
        String uuidString=crime.getId().toString();
        ContentValues values=getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.cols.UUID + "= ?",new  String[]{uuidString});

    }

private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){

    Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
return new CrimeCursorWrapper(cursor);
}
    public File getPhotoFile(Crime crime){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFileName());
    }
}
