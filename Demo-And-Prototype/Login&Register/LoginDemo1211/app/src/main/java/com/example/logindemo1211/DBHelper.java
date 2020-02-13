package com.example.logindemo1211;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATBASE_NAME = "DataBase.db";
    private static final int DATABSE_VERSION = 1;
    private static final String SQL_NAME = "DataBase.sql";
    private Context context;


    public DBHelper(@Nullable Context context) {
        super(context, DATBASE_NAME, null, DATABSE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        executeAssetsSQL(db, SQL_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    private void executeAssetsSQL(SQLiteDatabase db, String dbfilepath) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(context.getAssets().open(dbfilepath)));
            String line;
            String buffer = "";
            db.beginTransaction();
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("db-error", e.toString());
        } finally {
            db.endTransaction();
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                Log.e("db-error", e.toString());
            }
        }
    }
}


