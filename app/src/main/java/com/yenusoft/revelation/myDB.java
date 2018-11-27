package com.yenusoft.revelation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SofKaints on 2017. 7. 29..
 */

public class myDB extends SQLiteOpenHelper
{
    @SuppressLint("SdCardPath")
    public myDB(Context context) {
        super(context, "/mnt/sdcard/YenuSoft/rev_db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE `wrong_note` (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t`abb_num`\tINTEGER NOT NULL,\n" + //성경넘버
                "\t`chapter`\tINTEGER NOT NULL,\n" +
                "\t`verse`\tINTEGER NOT NULL,\n" +
                "\t`count`\tINTEGER NOT NULL\n" + //틀린 갯수
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS wrong_note");
        onCreate(sqLiteDatabase);
    }

    public Cursor funcSelect(String Query)
    {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor c = sql.rawQuery(Query, null);
        boolean hasNext = c.moveToFirst();
        if (!hasNext)
            c = null;
        sql.close();
        return c;
    }

    public void funcUseQuery(String Query)
    {
        SQLiteDatabase sql = getReadableDatabase();
        sql.execSQL(Query);
        sql.close();
    }
}

//        my.funcUseQuery(String.format("INSERT INTO wrong_note (abb_num, chapter, verse, count) VALUES (%d, %d, %d, %d);",
//                66,
//                1,
//                1,
//                3));
//        my.funcUseQuery(String.format("INSERT INTO wrong_note (abb_num, chapter, verse, count) VALUES (%d, %d, %d, %d);",
//                66,
//                2,
//                4,
//                1));
//        my.funcUseQuery(String.format("INSERT INTO wrong_note (abb_num, chapter, verse, count) VALUES (%d, %d, %d, %d);",
//                66,
//                22,
//                2,
//                2));