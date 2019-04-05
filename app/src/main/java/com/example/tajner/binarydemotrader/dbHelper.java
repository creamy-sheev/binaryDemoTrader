package com.example.tajner.binarydemotrader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Tajner on 2018-04-08.
 */

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Data12.db";
    private static final int DB_VERSION = 1;
    private final String TAG = "GRAPH ACTIVITY";

    private static final String TABLE_NAME1 = "bitcoin_data";
    private static final String TABLE_NAME2 = "ethereum_data";
    private static final String TABLE_NAME3 = "wallet";
    private static final String TABLE_NAME4 = "transactions";
    private static final String TABLE_NAME5 = "msft_stock_data";
    private static final String TABLE_NAME6 = "cat_stock_data";


    private static final String COL_ID = "ID";
    private static final String COL_DATE = "DATE";
    private static final String COL_VALUE_HIGH = "VALUE_HIGH";
    private static final String COL_VALUE_LOW = "VALUE_LOW";
    private static final String COL_VALUE_OPEN = "VALUE_OPEN";
    private static final String COL_VALUE_CLOSE = "VALUE_CLOSE";

    private static final String COL_WALLET_BALANCE = "WALLET_BALANCE";
    private static final String COL_TR_ID = "TRANSACTION_ID";
    private static final String COL_TR_VAL = "TRANSACTION_VALUE";
    private static final String COL_TR_DATE = "TRANSACTION_DATE";
    private static final String COL_TR_RES = "TRANSACTION_RESULT";
    private static final String COL_TR_ASSET = "TRANSACTION_ASSET";


    private SQLiteDatabase data;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_bitcoin = "CREATE TABLE " + TABLE_NAME1 + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_DATE + " TEXT," +
                COL_VALUE_HIGH + " REAL," + COL_VALUE_LOW + " REAL," + COL_VALUE_OPEN + " REAL," + COL_VALUE_CLOSE + " REAL);";

        String create_table_ethereum = "CREATE TABLE " + TABLE_NAME2 + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_DATE + " TEXT," +
                COL_VALUE_HIGH + " REAL," + COL_VALUE_LOW + " REAL," + COL_VALUE_OPEN + " REAL," + COL_VALUE_CLOSE + " REAL);";

        String create_table_wallet = "CREATE TABLE " + TABLE_NAME3 + "(" + COL_WALLET_BALANCE + " INTEGER);";

        String create_table_transactions = "CREATE TABLE " + TABLE_NAME4 + "(" + COL_TR_ID + " INTEGER PRIMARY KEY," +
                COL_TR_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP," + COL_TR_VAL + " INTEGER," + COL_TR_ASSET + " TEXT," + COL_TR_RES + " INTEGER);";

        String create_table_msft = "CREATE TABLE " + TABLE_NAME5 + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_DATE + " TEXT," +
                COL_VALUE_HIGH + " REAL," + COL_VALUE_LOW + " REAL," + COL_VALUE_OPEN + " REAL," + COL_VALUE_CLOSE + " REAL);";

        String create_table_cat = "CREATE TABLE " + TABLE_NAME6 + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_DATE + " TEXT," +
                COL_VALUE_HIGH + " REAL," + COL_VALUE_LOW + " REAL," + COL_VALUE_OPEN + " REAL," + COL_VALUE_CLOSE + " REAL);";


        db.execSQL(create_table_bitcoin);
        db.execSQL(create_table_ethereum);
        db.execSQL(create_table_wallet);
        db.execSQL(create_table_transactions);
        db.execSQL(create_table_msft);
        db.execSQL(create_table_cat);

        ContentValues values = new ContentValues();
        values.put(COL_WALLET_BALANCE, 20000);
        db.insert(TABLE_NAME3, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e("db", "table exists, dropping and recreating");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME6);
        Log.e("db", "Done");

        onCreate(db);
    }

    public void insertData(String date, Double valueHigh, Double valueLow, Double valueOpen, Double valueClose, String TABLE_NAME) {

        data = getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_VALUE_HIGH, valueHigh);
        values.put(COL_VALUE_LOW, valueLow);
        values.put(COL_VALUE_OPEN, valueOpen);
        values.put(COL_VALUE_CLOSE, valueClose);

        data.insert(TABLE_NAME, null, values);

        Log.e("db", "test");

    }

    public void insertTransactionData(int value, int result, String asset){

        data = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TR_VAL, value);
        values.put(COL_TR_ASSET, asset);
        values.put(COL_TR_RES, result);

        data.insert(TABLE_NAME4, null, values);

        Log.i(TAG, "tr data inserted");

    }

    public String[] getTrDate(){
        data = getReadableDatabase();
        String query = "select " + COL_TR_DATE + " from " + TABLE_NAME4 + " limit 10";

        Cursor cursor = data.rawQuery(query, null);
        String[] values = new String[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()){
            do {
                values[index] = cursor.getString(cursor.getColumnIndex(COL_TR_DATE));
                index++;
            }while (cursor.moveToNext());
        }cursor.close();
        return values;
    }

    public String[] getTrValue(){
        data = getReadableDatabase();
        String query = "select " + COL_TR_VAL + " from " + TABLE_NAME4 + " limit 10";

        Cursor cursor = data.rawQuery(query, null);
        String[] values = new String[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()){
            do {
                values[index] = Integer.toString(cursor.getInt(cursor.getColumnIndex(COL_TR_VAL)));
                index++;
            }while (cursor.moveToNext());
        }cursor.close();
        return values;
    }

    public String[] getTrAsset(){
        data = getReadableDatabase();
        String query = "select " + COL_TR_ASSET + " from " + TABLE_NAME4 + " limit 10";

        Cursor cursor = data.rawQuery(query, null);
        String[] values = new String[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()){
            do {
                values[index] = cursor.getString(cursor.getColumnIndex(COL_TR_ASSET));
                index++;
            }while (cursor.moveToNext());
        }cursor.close();
        return values;
    }

    public String[] getTrRes(){
        data = getReadableDatabase();
        String query = "select " + COL_TR_RES + " from " + TABLE_NAME4 + " limit 10";

        Cursor cursor = data.rawQuery(query, null);
        String[] values = new String[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()){
            do {
                values[index] = Integer.toString(cursor.getInt(cursor.getColumnIndex(COL_TR_RES)));
                index++;
            }while (cursor.moveToNext());
        }cursor.close();
        return values;
    }

    public void resetUserData() {

        String create_table_wallet = "CREATE TABLE " + TABLE_NAME3 + "(" + COL_WALLET_BALANCE + " INTEGER);";

        String create_table_transactions = "CREATE TABLE " + TABLE_NAME4 + "(" + COL_TR_ID + " INTEGER PRIMARY KEY," +
                COL_TR_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP," + COL_TR_VAL + " INTEGER," + COL_TR_ASSET + " TEXT," + COL_TR_RES + " INTEGER);";

        data = getWritableDatabase();

        data.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        data.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);

        data.execSQL(create_table_wallet);
        data.execSQL(create_table_transactions);
        ContentValues values = new ContentValues();
        values.put(COL_WALLET_BALANCE, 20000);
        data.insert(TABLE_NAME3, null, values);

    }


    public boolean checkIfTableExists () {

        data = getReadableDatabase();
        Cursor cursorBtc = data.rawQuery("select * from bitcoin_data limit 1", null);
        Cursor cursorEth = data.rawQuery("select * from ethereum_data limit 1", null);
        Cursor cursorMsft = data.rawQuery("select * from msft_stock_data limit 1", null);
        Cursor cursorCat = data.rawQuery("select * from cat_stock_data limit 1", null);

        if (cursorBtc.getCount() > 0 && cursorEth.getCount() > 0
                && cursorMsft.getCount() > 0 && cursorCat.getCount() > 0) {
            cursorBtc.close();
            cursorEth.close();
            cursorMsft.close();
            cursorCat.close();
            return true;

        }
        else {
            cursorBtc.close();
            cursorEth.close();
            cursorMsft.close();
            cursorCat.close();
            return false;
        }

    }

    public String getWalletBalance() {

        data = getReadableDatabase();

        Cursor cursor = data.rawQuery("select " + COL_WALLET_BALANCE + " from " + TABLE_NAME3, null);
        Integer dbData = 0;

        if (cursor.moveToFirst()) {
            do {
                dbData = cursor.getInt(cursor.getColumnIndex(COL_WALLET_BALANCE));

            } while (cursor.moveToNext());
        }

        String balance = dbData.toString();
        cursor.close();
        return balance;
    }

    public void updateWallet(int value, boolean b) {
        data = getWritableDatabase();
        String walletBalance = getWalletBalance();
        int wb = Integer.parseInt(walletBalance);
        if(b){
            wb = wb + value;
        }else{
            wb = wb - value;
        }

        data = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WALLET_BALANCE, wb);
        data.update(TABLE_NAME3, values, null, null);
    }

    public float[] getData(String TABLE_NAME) {

        data = getReadableDatabase();

        Cursor cursor = data.rawQuery("select "+ COL_VALUE_HIGH + ", " + COL_VALUE_LOW + ", " + COL_VALUE_OPEN +
                ", " + COL_VALUE_CLOSE + " from " + TABLE_NAME, null);
        int count = cursor.getCount()*4;
        float[] values = new float[count];
        int index = 0;

        if(cursor.moveToFirst()) {
            do {
                values[index] = cursor.getFloat(cursor.getColumnIndex(COL_VALUE_HIGH));
                index++;
                values[index] = cursor.getFloat(cursor.getColumnIndex(COL_VALUE_LOW));
                index++;
                values[index] = cursor.getFloat(cursor.getColumnIndex(COL_VALUE_OPEN));
                index++;
                values[index] = cursor.getFloat(cursor.getColumnIndex(COL_VALUE_CLOSE));
                index++;
            } while (cursor.moveToNext());
        }

        // Ustalamy żeby random był od początku rekordów do końca rekordów - 10
        int i = ThreadLocalRandom.current().nextInt(0, (cursor.getCount() - 10));
        cursor.close();
        // mnożymy *4 ponieważ są 4 tabele
        int startOfRandomIndex = i*4;
        // dodajemy 43 bo chcemy 10 + 1 kolejnych wyników (39 to 10 wyników)
        int endOfRandomIndex = startOfRandomIndex + 43;
        // tworzymy tablice wynikową dla metody
        float[] finalValues = new float[44];
        // tworzymy index dla tablicy wynikowej
        int finalValuesIndex = 0;

        for (int j = startOfRandomIndex; j<endOfRandomIndex; j++){
            finalValues[finalValuesIndex] = values[j];
            finalValuesIndex++;
        }
        return finalValues;

    }
}
