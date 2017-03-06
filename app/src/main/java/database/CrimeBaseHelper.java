package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import database.CrimeDbSchema.CrimeTable;

/**
 * Created by User on 19.02.2017.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";

    String CREATE_DATABASE = "CREATE TABLE " + CrimeTable.NAME + "( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CrimeTable.cols.UUID + ", " +
            CrimeTable.cols.TITLE + ", " +
            CrimeTable.cols.DATE + ", "  +
            CrimeTable.cols.SOLVED + ", " + CrimeTable.cols.SUSPECT + ")";
    public CrimeBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {


    }
}
