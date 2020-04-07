package com.example.kgeu_main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.kgeu_main.utils.NetworkUtils.generateURL;

import static com.example.kgeu_main.utils.NetworkUtils.getResponceFromUrl;

public class Loading extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    final String LOG_TAGS = "myTime";
    DBHelper dbHelper;
    String time_request="null";
    String group=null;
    TextView textView;
    Boolean stopTime=true;
    int zapr=0;
    int zapr2=0;

    ProgressBar progressBar;

    SharedPreferences sPref;

    final String Pgroup= "group";

    class QueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... urls) {
            String response=null;
            try {
                response = getResponceFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response)
        {
            String timeStart=null;
            String timeEnd=null;
            String typePara=null;
            String subject=null;
            String space=null;
            String teacher=null;

            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                //JSONObject jsonRespose = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(response);


                //Добавление предмета

                for(int i=0; i < jsonArray.length(); i++) {

                    JSONObject predmetInfo=jsonArray.getJSONObject(i);



                    timeStart = predmetInfo.getString("time_start");
                    timeEnd = predmetInfo.getString("time_end");
                    typePara=predmetInfo.getString("type_para");
                    subject=predmetInfo.getString("subject");
                    space=predmetInfo.getString("space");
                    teacher=predmetInfo.getString("teacher");


                    if(typePara.equals("л."))
                        typePara="Лекция";
                    if(typePara.equals("пр."))
                        typePara="Практика";
                    if(typePara.equals("лаб."))
                        typePara="Лабораторная";
                    cv.put("time_start", timeStart);
                    cv.put("time_end", timeEnd);
                    cv.put("subject", subject);
                    cv.put("space", space);
                    cv.put("teacher", teacher);
                    cv.put("time_request", time_request);
                    cv.put("groupp", group);
                    cv.put("typePara", typePara);
                    // вставляем запись и получаем ее ID

                    long rowID = db.insert("contacts", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                    Log.d("ZAPROSDNYA", "time_request " + time_request);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            zapr++;
            textView.setText("Загрузка "+zapr*2+"%");
            stopTime=false;
            if(zapr2==zapr)
            {

            }
        }

    }

    @Override
    public void onBackPressed() {

//Ваш код
    }

    void writeToMySQL () {
        Thread threadwriteMySQL = new Thread(runwriteMySQL);
        threadwriteMySQL.start();
    }
    final Runnable runwriteMySQL = new Runnable() {
        @Override
        public void run() {

            Date currentDate = new Date();
            // Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());//Сейчас время
            String dateText = dateFormat.format(currentDate);
            Log.d(LOG_TAGS, "Time = " + dateText);

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(currentDate); // Календарь с сегодняшней датой
            int day1=cal.get(Calendar.DATE);
            int mon1=cal.get(Calendar.MONTH)+1;
            int yer1=cal.get(Calendar.YEAR);
            Log.d("myDAY", "day " + day1);
            Log.d("myDAY", "mon " + mon1);
            Log.d("myDAY", "yer " +yer1);


            GregorianCalendar calendar_start = new GregorianCalendar(2020,
                    Calendar.JANUARY, 1);
            calendar_start.setTime(currentDate);
            calendar_start.add(Calendar.DATE, -14);
            GregorianCalendar calendar_end = new GregorianCalendar(2020,
                    Calendar.JUNE, 30);
            calendar_end.setTime(currentDate);
            calendar_end.add(Calendar.MONTH, 1);

            // Убедимся, что возвращает 1 - февраль


            int month=cal.get(Calendar.MONTH)+1;//Узнаем какой сейчас месяц
            int start=month-1;
            int end=month+1;

            int check=0;

            for(int kol=0; kol<50; kol++)
            {

                check++;
                String days="";
                //создаем строку типа 20-02-2020
                int day=calendar_start.get(Calendar.DATE);
                int mon=calendar_start.get(Calendar.MONTH);
                int yer=cal.get(Calendar.YEAR);

                days+=day;
                mon++;
                days+="-";
                if(mon<10) {
                    days += "0" + mon;

                }
                else
                    days+=mon;
                days+="-";
                days+=yer;
                sPref = getSharedPreferences("info", Context.MODE_PRIVATE);
                Log.d("ZAPROS", "номер "+days);
                Log.d("POTOK", "Начало ");

                String group = sPref.getString(Pgroup, "");
                stopTime=true;
                time_request=days;
                URL generatedURL=generateURL(group,days);
                new QueryTask().execute(generatedURL);

                while (stopTime==true)
                {
                    Log.d("POTOK", "ждем " +zapr+" "+zapr2);
                }
                Log.d("POTOK", "Конец ");
                //Прибавляем 1 день
                Log.d(LOG_TAGS, "День " + days);
                calendar_start.add(Calendar.DATE, 1); // adds one day
                start=calendar_start.get(Calendar.MONTH);
                zapr2++;

            }
            //После загрузки всех данных вызываем новый экран
            Intent intent= new Intent(Loading.this, MainMenu.class);
            startActivity(intent);
            dbHelper.close();
            Log.d(LOG_TAGS, "YES " + check);



        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("contacts", null, null);
        dbHelper.close();
        writeToMySQL ();

        dbHelper = new DBHelper(this);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textView=(TextView)findViewById(R.id.textView);
        // подключаемся к БД


    }
}
