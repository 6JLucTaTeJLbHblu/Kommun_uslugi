package com.example.kommun_uslugi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
    OpenHelper(Context context) {
        super(context, "Database.db", null, 10);
    }
    private String[] names_of_columns = {"id", "Period", "Gas", "Price_gas", "Water", "Outwater", "Water_price", "Electricity1", "Electricity2", "Electricity3", "Price_electricity"};
    public static final String KEY_ID = "_id";
    public static final String KEY_PERIOD = "Period";
    public static final String KEY_GAS = "Gas";
    public static final String KEY_PRICE_GAS = "Price_gas";
    public static final String KEY_WATER = "Water";
    public static final String KEY_OUTWATER = "Outwater";
    public static final String KEY_WATER_PRICE = "Water_price";
    public static final String KEY_ELECTRICITY1 = "Electricity1";
    public static final String KEY_ELECTRICITY2 = "Electricity2";
    public static final String KEY_ELECTRICITY3 = "Electricity3";
    public static final String KEY_ELECTRICITY_PRICE = "Price_electricity";
    public static final String TABLE_NAME = "value";

    public static final String SETTINGS_KEY_ID = "_id";
    public static final String SETTINGS_KEY_PERIOD = "Settings_Period";
    public static final String SETTINGS_KEY_GAS = "Settings_Gas";
    public static final String SETTINGS_KEY_WATER = "Settings_Water";
    public static final String SETTINGS_KEY_OUTWATER = "Settings_Outwater";
    public static final String SETTINGS_KEY_ELECTRICITY1 = "Settings_Electricity1";
    public static final String SETTINGS_KEY_ELECTRICITY2 = "Settings_Electricity2";
    public static final String SETTINGS_KEY_ELECTRICITY3 = "Settings_Electricity3";
    public static final String SETTINGS_TABLE_NAME = "settings";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_PERIOD + " DATE, " +
                KEY_GAS + " LONG, " +
                KEY_PRICE_GAS + " LONG, " +
                KEY_WATER + " LONG, " +
                KEY_OUTWATER + " LONG, " +
                KEY_WATER_PRICE + " LONG, " +
                KEY_ELECTRICITY1 + " LONG, " +
                KEY_ELECTRICITY2 + " LONG, " +
                KEY_ELECTRICITY3 + " LONG, " +
                KEY_ELECTRICITY_PRICE + " LONG); ";
        db.execSQL(query);
        query = "CREATE TABLE " + SETTINGS_TABLE_NAME + " (" +
                SETTINGS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SETTINGS_KEY_PERIOD + " DATE, " +
                SETTINGS_KEY_GAS + " LONG, " +
                SETTINGS_KEY_WATER + " LONG, " +
                SETTINGS_KEY_OUTWATER + " LONG, " +
                SETTINGS_KEY_ELECTRICITY1 + " LONG, " +
                SETTINGS_KEY_ELECTRICITY2 + " LONG, " +
                SETTINGS_KEY_ELECTRICITY3 + " LONG); ";
        db.execSQL(query);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS value");
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS value");
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }
}