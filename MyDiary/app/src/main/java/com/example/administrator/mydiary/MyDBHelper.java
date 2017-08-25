package com.example.administrator.mydiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017-07-31.
 */

public class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "MyData.db", null, 5);
        }

        public void onCreate(SQLiteDatabase db) {
            String query = String.format("CREATE TABLE %s ("
                    + "_id INTEGER primary key autoincrement, "
                    + "%s TEXT, "
                    + "%s TEXT, "
                    + "%s TEXT, "
                    + "%s DATE );", "TBL_Diary", "title", "imgloc","texts","writedate");
            db.execSQL(query);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = String.format("DROP TABLE IF EXISTS %s", "TBL_Diary");
            db.execSQL(query);
            onCreate(db);

        }
    }

