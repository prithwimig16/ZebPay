package com.example.prithwi.zebpay.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Prithwi on 07/02/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "users";


    public static final String COL_1 = "userName";
    public static final String COL_2 = "reputation";
    public static final String COL_3 = "image";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table " +TABLE_NAME + " (userName text PRIMARY KEY,reputation text,image text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String userName,String reputation,String image){
        SQLiteDatabase db=this.getWritableDatabase();
        long rows = 0;
        ContentValues cv=new ContentValues();

        cv.put(COL_1,userName);
        cv.put(COL_2,reputation);
        cv.put(COL_3,image);

       rows = db.insertWithOnConflict(TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
       if(rows ==-1){
           return false;
       }
       else{
           return true;
       }
    }

    public Cursor getallData(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
    public void deleteAllRecords(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"1",null);
        db.close();
    }

    public Cursor getPropIds(String bhkType,String propType,String furType){

        SQLiteDatabase db=this.getReadableDatabase();


        String bhk=bhkType.replaceAll("\\[", "(").replaceAll("\\]",")");
        String type=propType.replaceAll("\\[", "(").replaceAll("\\]",")");
        String furn=furType.replaceAll("\\[", "(").replaceAll("\\]",")");

        String q="";
        q="select DISTINCT propId from property where type IN "+bhk+" AND "+" proprtyType IN "+type+
                " AND furnishType IN "+furn;

        Cursor cursor1=db.rawQuery(q, null);
        return cursor1;

    }



}





