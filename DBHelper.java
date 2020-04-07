package com.example.kgeu_main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.kgeu_main.ui.timetable.TimetableFragment;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="contactsDB";
    public static final String TABLE_CONTACTS="contacts";

    public static final String KEY_ID="_id";//id элемента
    public static final String KEY_TS="time_start";//Начало занятия
    public static final String KEY_TE="time_end";//Конец занятия
    public static final String KEY_SUBJECT="subject";//Название предмета
    public static final String KEY_SPACE="space";//Кабинет
    public static final String KEY_TEACHER="teacher";//ФИО препода
    public static final String KEY_TYPEPARA="typePara";
    public static final String KEY_TZ="time_request";//Дата занятия ПРИМЕР: 20-03-2020
    public static final String KEY_GROUP="groupp";//Название группы у которой занятие

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
               + " integer primary key," + KEY_TS + " text,"
        + KEY_TE + " text,"
        + KEY_SUBJECT + " text,"
        + KEY_SPACE + " text,"
        + KEY_TYPEPARA + " text,"
        + KEY_TEACHER + " text,"
        + KEY_TZ + " text,"
        + KEY_GROUP + " text" + ")");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);

        onCreate(db);
    }
}
