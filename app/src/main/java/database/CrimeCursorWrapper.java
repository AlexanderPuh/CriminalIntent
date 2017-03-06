package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.alexb8466gmail.criminalintent.Crime;

import java.util.Date;
import java.util.UUID;

import database.CrimeDbSchema.CrimeTable;

/**
 * Created by User on 19.02.2017.
 */

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime(){
        String uuidString=getString(getColumnIndex(CrimeTable.cols.UUID));
        String title=getString(getColumnIndex(CrimeTable.cols.TITLE));
        long date=getLong(getColumnIndex(CrimeTable.cols.DATE));
        int isSolved=getInt(getColumnIndex(CrimeTable.cols.SOLVED));
        String suspect=getString(getColumnIndex(CrimeTable.cols.SUSPECT));

   Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved!=0);
        crime.setSuspect(suspect);
        return crime;


    }
}
