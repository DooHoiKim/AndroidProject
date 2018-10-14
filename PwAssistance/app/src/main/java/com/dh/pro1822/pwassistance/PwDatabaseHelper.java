package com.dh.pro1822.pwassistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class PwDatabaseHelper extends SQLiteOpenHelper {

    private static PwDatabaseHelper sInstance;

    private static final String DB_NAME = "pwAssist";
    private static final int DB_VERSION = 1;

    public static synchronized PwDatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PwDatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private PwDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    public static long insertPwList(SQLiteDatabase db, String name, String id,
                                    String pw, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractDB.PwListEntry.COLUMN_NAME, name);
        contentValues.put(ContractDB.PwListEntry.COLUMN_LOG_IN_ID, id);
        contentValues.put(ContractDB.PwListEntry.COLUMN_LOG_IN_PW, pw);
        contentValues.put(ContractDB.PwListEntry.COLUMN_DESCRIPTION, description);
        return db.insert(ContractDB.PwListEntry.TABLE_NAME, null, contentValues);
    }

    public static int updatePwList(SQLiteDatabase db, int _id, String name, String id,
                                   String pw, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractDB.PwListEntry.COLUMN_NAME, name);
        contentValues.put(ContractDB.PwListEntry.COLUMN_LOG_IN_ID, id);
        contentValues.put(ContractDB.PwListEntry.COLUMN_LOG_IN_PW, pw);
        contentValues.put(ContractDB.PwListEntry.COLUMN_DESCRIPTION, description);
        return db.update(ContractDB.PwListEntry.TABLE_NAME, contentValues,
                ContractDB.PwListEntry.COLUMN_ID + " = " + _id, null);
    }

    public static int deletePwList(SQLiteDatabase db, int _id) {
        return db.delete(ContractDB.PwListEntry.TABLE_NAME,
                ContractDB.PwListEntry.COLUMN_ID + " = " + _id, null);
    }

    public static Cursor getPwListWhereCursor(SQLiteDatabase db, String parm) {
        String query_str = "SELECT "
                + ContractDB.PwListEntry.COLUMN_ID + ", "
                + ContractDB.PwListEntry.COLUMN_NAME + ", "
                + ContractDB.PwListEntry.COLUMN_LOG_IN_ID + ", "
                + ContractDB.PwListEntry.COLUMN_LOG_IN_PW
                + " FROM " + ContractDB.PwListEntry.TABLE_NAME
                + " WHERE " + ContractDB.PwListEntry.COLUMN_NAME + " LIKE '%" + parm + "%'"
                + " OR " + ContractDB.PwListEntry.COLUMN_LOG_IN_ID + " LIKE '%" + parm + "%'"
                + " OR " + ContractDB.PwListEntry.COLUMN_LOG_IN_PW + " LIKE '%" + parm + "%'"
                + " ORDER BY " + ContractDB.PwListEntry.COLUMN_NAME;
        Cursor cursor;
        try {
            cursor = db.rawQuery(query_str, null);
        } catch (SQLiteException e) {
            cursor = null;
        }

        return cursor;
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE LOGIN (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "LOG_IN_PW TEXT);");

            ContentValues contentValues = new ContentValues();
            contentValues.put(ContractDB.LoginEntry.COLUMN_LOG_IN_PW, "111111");
            db.insert(ContractDB.LoginEntry.TABLE_NAME, null, contentValues);

            db.execSQL("CREATE TABLE PWLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "LOG_IN_ID TEXT, "
                    + "LOG_IN_PW TEXT, "
                    + "DESCRIPTION TEXT);");

            insertPwList(db, "test", "pro1822", "testPw", "단순 테스트 초기입력");
            insertPwList(db, "test2", "problem2", "testPw2", "단순 테스트 초기입력2");
        }
    }

    /*
    private Cursor getPwListCursor() {
        PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(this);
        Cursor cursor;
        try {
            cursor = pwDatabaseHelper.getReadableDatabase()
                    .query(ContractDB.PwListEntry.TABLE_NAME,
                            new String[]{ContractDB.PwListEntry.COLUMN_ID,
                                    ContractDB.PwListEntry.COLUMN_NAME,
                                    ContractDB.PwListEntry.COLUMN_LOG_IN_ID,
                                    ContractDB.PwListEntry.COLUMN_LOG_IN_PW},
                            null, null, null, null, ContractDB.PwListEntry.COLUMN_NAME);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            cursor = null;
        }

        return cursor;
    }
    */
}
