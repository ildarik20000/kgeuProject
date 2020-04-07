package com.example.kgeu_main;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.RenderProcessGoneDetail;
import android.widget.RemoteViews;

public class MyProvider extends AppWidgetProvider {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {




            updateWidget(context, appWidgetManager, i);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        setUpdateTV(rv, context, appWidgetId);

        setList(rv, context, appWidgetId);

        //setListClick(rv, context, appWidgetId);
       // rv.setTextViewText(R.id.time_start, "MY WIDGET2");//Странно работает
       // rv.setTextViewText(R.id.teacher, "MY WIDGET3");//Странно работает
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lvList);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {

        java.util.Date currentDate = new java.util.Date();
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

        rv.setTextViewText(R.id.WidgettextView1,
                thisDay);
        Intent updIntent = new Intent(context, MyProvider.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { appWidgetId });
        PendingIntent updPIntent = PendingIntent.getBroadcast(context,
                appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        //rv.setTextViewText(R.id.time_start,"wow");
        Intent adapter = new Intent(context, MyService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.lvList, adapter);
    }

    void setListClick(RemoteViews rv, Context context, int appWidgetId) {

    }

}