package com.example.kgeu_main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.TextView;

public class MyFactory implements RemoteViewsFactory {
    DBHelper dbHelper;
    ArrayList<String> dayWidg;
    ArrayList<String> time_start;
    ArrayList<String> time_end;
    ArrayList<String> type;
    ArrayList<String> subject;
    ArrayList<String> teacher;
    ArrayList<String> space;

    Context context;
    SimpleDateFormat sdf;
    int widgetID;

    int[] OpenClose = new int[2];




    MyFactory(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        time_end = new ArrayList<String>();
        time_start=new ArrayList<String>();
        type=new ArrayList<String>();
        subject=new ArrayList<String>();
        teacher=new ArrayList<String>();
        space=new ArrayList<String>();
        dayWidg=new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return time_start.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        OpenClose[0] = Color.parseColor("#4592B8");//закрыт
        OpenClose[1] = Color.parseColor("#347B9B");//открыт

        int[] colors = new int[2];

        colors[0] = Color.parseColor("#E4F4F6");
        colors[1] = Color.parseColor("#FFFFFF");

        int[] predmetColor = new int[4];

        predmetColor[0] = Color.parseColor("#ED090A");//Лекция цвет
        predmetColor[1] = Color.parseColor("#03B402");//Практика цвет
        predmetColor[2] = Color.parseColor("#9E0EBF");//Лабораторная цвет
        predmetColor[3] = Color.parseColor("#810000");//Что-то иное цвет

        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.predmet_widget);


        //rView.setTextViewText(R.id.WidgettextView1, dayWidg.get(position));
        rView.setTextViewText(R.id.time_start,time_start.get(position));
        rView.setTextViewText(R.id.time_end,time_end.get(position));
        rView.setTextViewText(R.id.type,type.get(position));
        rView.setTextViewText(R.id.subject, subject.get(position));
        rView.setTextViewText(R.id.teacher, teacher.get(position));
        rView.setTextViewText(R.id.space, space.get(position));

        if(type.get(position).equals("Лекция"))
            rView.setInt(R.id.type, "setBackgroundColor", predmetColor[0]);
        else if(type.get(position).equals("Практика"))
            rView.setInt(R.id.type, "setBackgroundColor", predmetColor[1]);
        else if(type.get(position).equals("Лабораторная"))
            rView.setInt(R.id.type, "setBackgroundColor", predmetColor[2]);
        else rView.setInt(R.id.type, "setBackgroundColor", predmetColor[3]);

        rView.setInt(R.id.One_block, "setBackgroundColor", colors[position%2]);



        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        ContentValues cv = new ContentValues();
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("contacts",
                null, null, null, null, null, null);
        time_start.clear();
        time_end.clear();
        type.clear();
        subject.clear();
        teacher.clear();
        space.clear();
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int TSColIndex = c.getColumnIndex("time_start");
            int TEColIndex = c.getColumnIndex("time_end");
            int SubjectColIndex = c.getColumnIndex("subject");
            int SpaceColIndex = c.getColumnIndex("space");
            int TeacherColIndex = c.getColumnIndex("teacher");
            int TZColIndex = c.getColumnIndex("time_request");
            int GroupColIndex = c.getColumnIndex("groupp");
            int TPColIndex = c.getColumnIndex("typePara");

            java.util.Date currentDate = new Date();
            // Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());//Сейчас время
            String dateText = dateFormat.format(currentDate);

            Calendar CTimeTable = Calendar.getInstance(); // creates calendar
            CTimeTable.setTime(currentDate); // Календарь с сегодняшней датой

            int day=CTimeTable.get(Calendar.DATE);
            int mon=CTimeTable.get(Calendar.MONTH)+1;
            int yer=CTimeTable.get(Calendar.YEAR);


            Log.d("myDAY", "day " + day);
            Log.d("myDAY", "mon " + mon);
            Log.d("myDAY", "yer " +yer);
            String Thisdays="";
            Thisdays += day;
            Thisdays += "-";
            if (mon < 10) {
                Thisdays += "0" + mon;

            } else
                Thisdays += mon;
            Thisdays += "-";
            Thisdays += yer;

            Integer dayOfWeek = CTimeTable.get(Calendar.DAY_OF_WEEK);
            String thisDay = null;
            if(dayOfWeek==1)
                thisDay=("ВОСКРЕСЕНЬЕ "+Thisdays);
            if(dayOfWeek==2)
                thisDay=("ПОНЕДЕЛЬНИК "+Thisdays);
            if(dayOfWeek==3)
                thisDay=("ВТОРНИК "+Thisdays);
            if(dayOfWeek==4)
                thisDay=("СРЕДА "+Thisdays);
            if(dayOfWeek==5)
                thisDay=("ЧЕТВЕРГ "+Thisdays);
            if(dayOfWeek==6)
                thisDay=("ПЯТНИЦА "+Thisdays);
            if(dayOfWeek==7)
                thisDay=("СУББОТА "+Thisdays);
            Log.d("myDAYWidg", "day " + thisDay);

        do{

            if (c.getString(TZColIndex).equals(Thisdays)) {
                //dayWidg.add(thisDay);
               time_start.add(c.getString(TSColIndex));
               time_end.add(c.getString(TEColIndex));
               type.add(c.getString(TPColIndex));
               subject.add(c.getString(SubjectColIndex));
               teacher.add(c.getString(TeacherColIndex));
               space.add(c.getString(SpaceColIndex));

            }

        } while (c.moveToNext());

    }




    }

    @Override
    public void onDestroy() {

    }

}