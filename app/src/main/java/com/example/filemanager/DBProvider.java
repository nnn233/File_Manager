package com.example.filemanager;

import static com.example.filemanager.Contracts.CODE;
import static com.example.filemanager.Contracts.PATH;
import static com.example.filemanager.Contracts.TABLE_NAME_FOR_FILES_CODES;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBProvider {
    private final DBHelper dbHelper;
    private final SQLiteDatabase db;

    public DBProvider(Context context){
        dbHelper = new DBHelper(context.getApplicationContext());
        db = dbHelper.getWritableDatabase();
    }

    public void insert(String code, String path) {
        String insertion = "INSERT OR IGNORE INTO " + TABLE_NAME_FOR_FILES_CODES + " (" + CODE+", "+PATH + ") VALUES ('" + code + "', '"+path+"');";
        db.execSQL(insertion);
    }

    public void update(String code, String path) {
        String update = "UPDATE " + TABLE_NAME_FOR_FILES_CODES + " SET " + CODE + " = '" + code + "' WHERE "+PATH+" = '"+path+"';";
        db.execSQL(update);
    }

    public void delete(String path) {
        String deletion = "DELETE FROM " + TABLE_NAME_FOR_FILES_CODES + " WHERE "+PATH+" = '"+path+"';";
        db.execSQL(deletion);
    }

    public boolean isNullData(){
        String query = "SELECT * FROM " + TABLE_NAME_FOR_FILES_CODES+ ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            if(cursor.getString(0)==null)
                return true;
        }
        cursor.close();
        return false;
    }

   public String getCodeByPath(String path){
        String query = "SELECT " + CODE + " FROM " + TABLE_NAME_FOR_FILES_CODES+ " WHERE "+PATH+" = '"+path+"';";
        String code="";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                code = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return code;
    }

    public void close(){
        db.close();
        dbHelper.close();
    }
}
