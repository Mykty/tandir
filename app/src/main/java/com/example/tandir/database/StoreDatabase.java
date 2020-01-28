package com.example.tandir.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StoreDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tandir.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FOOD = "food_store";
    public static final String TABLE_ORDER_HISTORY = "order_history_store";

    public static final String COLUMN_FKEY = "fkey";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FDESC = "description";
    public static final String COLUMN_FAVAILABLE = "true";
    public static final String COLUMN_FTYPE = "foodType";
    public static final String COLUMN_FPRICE = "price";
    public static final String TABLE_VER = "versions";
    public static final String COLUMN_FOOD_VER = "food_ver";



    Context context;

    public StoreDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_FOOD + "(" +
                COLUMN_FKEY + " TEXT, " +
                COLUMN_PHOTO + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_FDESC + " TEXT, " +
                COLUMN_FAVAILABLE + " TEXT, " +
                COLUMN_FTYPE + " TEXT, " +
                COLUMN_FPRICE + " TEXT )");

        db.execSQL("CREATE TABLE " + TABLE_VER + "(" +
                COLUMN_FOOD_VER + " TEXT)");

        addVersions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VER);

        onCreate(db);
    }

    public void cleanFoods(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_FOOD);

    }

    public void cleanVersions(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_VER);

    }

    public void addVersions(SQLiteDatabase db) {
        ContentValues versionValues = new ContentValues();
        versionValues.put(COLUMN_FOOD_VER, "0");

        db.insert(TABLE_VER, null, versionValues);
    }
    /*
    public Cursor getSinlgeEntry(String idNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " +
                COLUMN_ID_NUMBER + "=?", new String[]{idNumber});
        return res;

    }

    public void deleteBook(SQLiteDatabase db, Book book) {
        db.delete(TABLE_BOOKS, COLUMN_FKEY + "='" + book.getFirebaseKey()+"'", null);
    }

    public void updateBook(SQLiteDatabase db, Book book) {
        ContentValues updateValues = new ContentValues();

        updateValues.put(COLUMN_BNAME, book.getName());
        updateValues.put(COLUMN_BAUTHOR, book.getAuthor());
        updateValues.put(COLUMN_BDESC, book.getDesc());
        updateValues.put(COLUMN_BPAGE_NUMBER, book.getPage_number());
        updateValues.put(COLUMN_BRATING, book.getRating());
        updateValues.put(COLUMN_BCOUNT, book.getBookCount());
        updateValues.put(COLUMN_PHOTO, book.getPhoto());
        updateValues.put(COLUMN_BRESERVED, book.getReserved());
        updateValues.put(COLUMN_QR_CODE, book.getQr_code());
        updateValues.put(COLUMN_IMG_STORAGE_NAME, book.getImgStorageName());

        db.update(TABLE_BOOKS, updateValues, COLUMN_FKEY + "='" + book.getFirebaseKey()+"'", null);
        Log.i("child", "db: "+book.getName());

    }
    */
}