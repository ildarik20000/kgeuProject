package com.example.kgeu_main.ui.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v4.app.Fragment;


import com.example.kgeu_main.DBHelper;
import com.example.kgeu_main.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimetableFragment extends Fragment implements View.OnClickListener {

    private TimetableViewModel timetableViewModel;


    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    Button mines_week, plus_week;
    LinearLayout pn, vt,sr,ch,pt,sb,vs;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, group;
    LinearLayout raspPN,raspVT,raspSR,raspCH,raspPT,raspSB,raspVS;

    ImageView imageRotPN,imageRotVT,imageRotSR,imageRotCH,imageRotPT,imageRotSB,imageRotVS;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        View root = inflater.inflate(R.layout.fragment_timetable, container, false);
        pn=(LinearLayout)root.findViewById(R.id.pn);
        vt=(LinearLayout)root.findViewById(R.id.vt);
        sr=(LinearLayout)root.findViewById(R.id.sr);
        ch=(LinearLayout)root.findViewById(R.id.ch);
        pt=(LinearLayout)root.findViewById(R.id.pt);
        sb=(LinearLayout)root.findViewById(R.id.sb);
        vs=(LinearLayout)root.findViewById(R.id.vs);

        mines_week=(Button)root.findViewById(R.id.mines_week);
        plus_week=(Button)root.findViewById(R.id.plus_week);

        raspPN=(LinearLayout)root.findViewById(R.id.raspPN);
        raspVT=(LinearLayout)root.findViewById(R.id.raspVT);
        raspSR=(LinearLayout)root.findViewById(R.id.raspSR);
        raspCH=(LinearLayout)root.findViewById(R.id.raspCH);
        raspPT=(LinearLayout)root.findViewById(R.id.raspPT);
        raspSB=(LinearLayout)root.findViewById(R.id.raspSB);
        raspVS=(LinearLayout)root.findViewById(R.id.raspVS);

        imageRotPN=(ImageView)root.findViewById(R.id.imageRotPN);
        imageRotVT=(ImageView) root.findViewById(R.id.imageRotVT);
        imageRotSR=(ImageView) root.findViewById(R.id.imageRotSR);
        imageRotCH=(ImageView) root.findViewById(R.id.imageRotCH);
        imageRotPT=(ImageView) root.findViewById(R.id.imageRotPT);
        imageRotSB=(ImageView) root.findViewById(R.id.imageRotSB);
        imageRotVS=(ImageView) root.findViewById(R.id.imageRotVS);

        SharedPreferences sPref=this.getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String groupTX = sPref.getString("group", "");
        group=(TextView)root.findViewById(R.id.group);
        group.setText(groupTX);


        textView1=(TextView)root.findViewById(R.id.textView1);
        textView2=(TextView)root.findViewById(R.id.textView2);
        textView3=(TextView)root.findViewById(R.id.textView3);
        textView4=(TextView)root.findViewById(R.id.textView4);
        textView5=(TextView)root.findViewById(R.id.textView5);
        textView6=(TextView)root.findViewById(R.id.textView6);
        textView7=(TextView)root.findViewById(R.id.textView7);

        pn.setOnClickListener(this);
        vt.setOnClickListener(this);
        sr.setOnClickListener(this);
        ch.setOnClickListener(this);
        pt.setOnClickListener(this);
        sb.setOnClickListener(this);
        vs.setOnClickListener(this);

        plus_week.setOnClickListener(this);
        mines_week.setOnClickListener(this);




        newWeekTimeTable(0);

        return root;
    }




    //обновление расписания
    void newWeekTimeTable (int PlusMines) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        dbHelper = new DBHelper(getActivity());

        textView1.setText("ПОНЕДЕЛЬНИК Нет расписания");
        textView2.setText("ВТОРНИК Нет расписания");
        textView3.setText("СРЕДА Нет расписания");
        textView4.setText("ЧЕТВЕРГ Нет расписания");
        textView5.setText("ПЯТНИЦА Нет расписания");
        textView6.setText("СУББОТА Нет расписания");
        textView7.setText("ВОСКРЕСЕНЬЕ Нет расписания");

        imageRotPN.setRotation(90);
        imageRotVT.setRotation(90);
        imageRotSR.setRotation(90);
        imageRotCH.setRotation(90);
        imageRotPT.setRotation(90);
        imageRotSB.setRotation(90);
        imageRotVS.setRotation(90);


        raspPN.setVisibility(View.GONE);
        raspVT.setVisibility(View.GONE);
        raspSR.setVisibility(View.GONE);
        raspCH.setVisibility(View.GONE);
        raspPT.setVisibility(View.GONE);
        raspSB.setVisibility(View.GONE);
        raspVS.setVisibility(View.GONE);




        raspPN.removeAllViews();
        raspVT.removeAllViews();
        raspSR.removeAllViews();
        raspCH.removeAllViews();
        raspPT.removeAllViews();
        raspSB.removeAllViews();
        raspVS.removeAllViews();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("myLogs", "BD");


        int[] OpenClose = new int[2];

        OpenClose[0] = Color.parseColor("#4592B8");//закрыт
        OpenClose[1] = Color.parseColor("#347B9B");//открыт



        pn.setBackgroundColor(OpenClose[0]);
        vt.setBackgroundColor(OpenClose[0]);
        sr.setBackgroundColor(OpenClose[0]);
        ch.setBackgroundColor(OpenClose[0]);
        pt.setBackgroundColor(OpenClose[0]);
        sb.setBackgroundColor(OpenClose[0]);
        vs.setBackgroundColor(OpenClose[0]);


        int[] colors = new int[2];

        colors[0] = Color.parseColor("#E4F4F6");
        colors[1] = Color.parseColor("#FFFFFF");

        int[] predmetColor = new int[4];

        predmetColor[0] = Color.parseColor("#ED090A");//Лекция цвет
        predmetColor[1] = Color.parseColor("#03B402");//Практика цвет
        predmetColor[2] = Color.parseColor("#9E0EBF");//Лабораторная цвет
        predmetColor[3] = Color.parseColor("#810000");//Что-то иное цвет

        LayoutInflater ltInflater = getLayoutInflater();
// создаем объект для создания и управления версиями БД

        Cursor c = db.query("contacts",
                null, null, null, null, null, null);

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

            int i=0;

            //Делаем чтоб с понедельника до воскресенья запоминались дни

            Date currentDate = new Date();
            // Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());//Сейчас время
            String dateText = dateFormat.format(currentDate);


            Calendar CTimeTable = Calendar.getInstance(); // creates calendar
            CTimeTable.setTime(currentDate); // Календарь с сегодняшней датой
            CTimeTable.add(Calendar.DATE, 7*PlusMines); // adds one day
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
            while (dayOfWeek!=2)
            {
                CTimeTable.add(Calendar.DATE, -1); // adds one day
                dayOfWeek = CTimeTable.get(Calendar.DAY_OF_WEEK);

            }

            String [] Day_Week= new String [7];

            for(int schet=0; schet<7; schet++) {
                //Делаем день недели чтоб выглядел как в бд
                String days = "";
                //создаем строку типа 20-02-2020
                day = CTimeTable.get(Calendar.DATE);
                mon = CTimeTable.get(Calendar.MONTH);
                yer = CTimeTable.get(Calendar.YEAR);


                days += day;
                mon++;
                days += "-";
                if (mon < 10) {
                    days += "0" + mon;

                } else
                    days += mon;
                days += "-";
                days += yer;
                Day_Week[schet]=days;
                CTimeTable.add(Calendar.DATE, 1);
            }





            do {
                //Log.d("myDAY", "c " + c.getString(TZColIndex));
                //Log.d("myDAY", "days " + days);
                if (c.getString(TZColIndex).equals(Day_Week[6])) {

                    //Log.d("myDAY", "daysvs " +days);
                    //CTimeTable.add(Calendar.DATE, 1);
                    textView7.setText("ВОСКРЕСЕНЬЕ "+Day_Week[6]);
                    if(Day_Week[6].equals(Thisdays)&&PlusMines==0){
                        raspVS.setVisibility(View.VISIBLE);
                        imageRotVS.setRotation(0);
                        vs.setBackgroundColor(OpenClose[1]);
                    }
                    //Заполнение Воскресенье
                    View predmet = ltInflater.inflate(R.layout.predmet, raspVS, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspVS.addView(predmet);


                }

                if (c.getString(TZColIndex).equals(Day_Week[5])) {

                    //CTimeTable.add(Calendar.DATE, 1);
                    textView6.setText("СУББОТА "+Day_Week[5]);
                    if(Day_Week[5].equals(Thisdays)&&PlusMines==0){
                        raspSB.setVisibility(View.VISIBLE);
                        imageRotSB.setRotation(0);
                        pn.setBackgroundColor(OpenClose[1]);
                    }
                    //Заполнение Суббота
                    View predmet = ltInflater.inflate(R.layout.predmet, raspSB, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspSB.addView(predmet);
                }

                if (c.getString(TZColIndex).equals(Day_Week[4])) {
                    // CTimeTable.add(Calendar.DATE, 1);
                    textView5.setText("ПЯТНИЦА "+Day_Week[4]);
                    //Заполнение Пятница
                    if(Day_Week[4].equals(Thisdays)&&PlusMines==0){
                        raspPT.setVisibility(View.VISIBLE);
                        imageRotPT.setRotation(0);
                        pt.setBackgroundColor(OpenClose[1]);
                    }
                    View predmet = ltInflater.inflate(R.layout.predmet, raspPT, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspPT.addView(predmet);
                }

                if (c.getString(TZColIndex).equals(Day_Week[3])) {
                    //CTimeTable.add(Calendar.DATE, 1);
                    textView4.setText("ЧЕТВЕРГ "+Day_Week[3]);
                    //Заполнение Четверг
                    if(Day_Week[3].equals(Thisdays)&&PlusMines==0){
                        raspCH.setVisibility(View.VISIBLE);
                        imageRotCH.setRotation(0);
                        ch.setBackgroundColor(OpenClose[1]);
                    }
                    View predmet = ltInflater.inflate(R.layout.predmet, raspCH, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspCH.addView(predmet);
                }

                if (c.getString(TZColIndex).equals(Day_Week[2])) {
                    //CTimeTable.add(Calendar.DATE, 1);
                    textView3.setText("СРЕДА "+Day_Week[2]);
                    //Заполнение Среда
                    if(Day_Week[2].equals(Thisdays)&&PlusMines==0){
                        raspSR.setVisibility(View.VISIBLE);
                        imageRotSR.setRotation(0);
                        sr.setBackgroundColor(OpenClose[1]);
                    }
                    View predmet = ltInflater.inflate(R.layout.predmet, raspSR, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspSR.addView(predmet);
                }

                if (c.getString(TZColIndex).equals(Day_Week[1])) {
                    //CTimeTable.add(Calendar.DATE, 1);
                    textView2.setText("ВТОРНИК "+Day_Week[1]);
                    //Заполнение Вторника
                    if(Day_Week[1].equals(Thisdays)&&PlusMines==0){
                        raspVT.setVisibility(View.VISIBLE);
                        imageRotVT.setRotation(0);
                        vt.setBackgroundColor(OpenClose[1]);
                    }
                    View predmet = ltInflater.inflate(R.layout.predmet, raspVT, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspVT.addView(predmet);
                }

                if (c.getString(TZColIndex).equals(Day_Week[0])) {

                    //CTimeTable.add(Calendar.DATE, 1);
                    //Добавление на экран
                    // Log.d("myDAY", "dayspn " +days);
                    Log.d("myLogs", "i = " + idColIndex);
                    //Заполнение понедельника
                    if(Day_Week[0].equals(Thisdays)&&PlusMines==0){
                        raspPN.setVisibility(View.VISIBLE);
                        imageRotPN.setRotation(0);
                        pn.setBackgroundColor(OpenClose[1]);
                    }
                    textView1.setText("ПОНЕДЕЛЬНИК "+Day_Week[0]);
                    View predmet = ltInflater.inflate(R.layout.predmet, raspPN, false);
                    TextView TS = (TextView) predmet.findViewById(R.id.time_start);
                    TS.setText(c.getString(TSColIndex));

                    TextView TE = (TextView) predmet.findViewById(R.id.time_end);
                    TE.setText(c.getString(TEColIndex));

                    TextView sub = (TextView) predmet.findViewById(R.id.subject);
                    sub.setText(c.getString(SubjectColIndex));

                    TextView space = (TextView) predmet.findViewById(R.id.space);
                    space.setText(c.getString(SpaceColIndex));

                    TextView teacher = (TextView) predmet.findViewById(R.id.teacher);
                    teacher.setText(c.getString(TeacherColIndex));

                    TextView TP = (TextView) predmet.findViewById(R.id.type);
                    TP.setText(c.getString(TPColIndex));

                    if(c.getString(TPColIndex).equals("Лекция"))
                        TP.setBackgroundColor(predmetColor[0]);
                    else if(c.getString(TPColIndex).equals("Практика"))
                        TP.setBackgroundColor(predmetColor[1]);
                    else if(c.getString(TPColIndex).equals("Лабораторная"))
                        TP.setBackgroundColor(predmetColor[2]);
                    else TP.setBackgroundColor(predmetColor[3]);

                    predmet.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    predmet.setBackgroundColor(colors[i % 2]);
                    raspPN.addView(predmet);
                }
                i++;



            } while (c.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");

        c.close();

        dbHelper.close();
    }


    int dayWeek=0;
    @Override
    public void onClick(View v) {
//Анимация +>-
        final Animation mines = AnimationUtils.loadAnimation(
                getContext(), R.anim.rotate_mines);
        int[] OpenClose = new int[2];

        OpenClose[0] = Color.parseColor("#4592B8");//закрыт
        OpenClose[1] = Color.parseColor("#347B9B");//открыт

        switch(v.getId()){

            case R.id.plus_week:
                dayWeek++;
                newWeekTimeTable(dayWeek);


                break;

            case R.id.mines_week:
                dayWeek--;
                newWeekTimeTable(dayWeek);
                break;

            case R.id.pn:

                if (raspPN.getVisibility() == View.VISIBLE) {

                    raspPN.setVisibility(View.GONE);
                    imageRotPN.startAnimation(mines);
                    imageRotPN.setRotation(90);
                    pn.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotPN.startAnimation(mines);
                    raspPN.setVisibility(View.VISIBLE);
                    imageRotPN.setRotation(0);
                    pn.setBackgroundColor(OpenClose[1]);
                    // действия, когда невидим.
                }
                break;
            case R.id.vt:
                if (raspVT.getVisibility() == View.VISIBLE) {

                    raspVT.setVisibility(View.GONE);
                    imageRotVT.startAnimation(mines);
                    imageRotVT.setRotation(90);
                    vt.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotVT.startAnimation(mines);
                    raspVT.setVisibility(View.VISIBLE);
                    imageRotVT.setRotation(0);
                    vt.setBackgroundColor(OpenClose[1]);

                    // действия, когда невидим.
                }
                break;
            case R.id.sr:
                if (raspSR.getVisibility() == View.VISIBLE) {

                    raspSR.setVisibility(View.GONE);
                    imageRotSR.startAnimation(mines);
                    imageRotSR.setRotation(90);
                    sr.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotSR.startAnimation(mines);
                    raspSR.setVisibility(View.VISIBLE);
                    imageRotSR.setRotation(0);
                    sr.setBackgroundColor(OpenClose[1]);

                    // действия, когда невидим.
                }
                break;
            case R.id.ch:
                if (raspCH.getVisibility() == View.VISIBLE) {

                    raspCH.setVisibility(View.GONE);
                    imageRotCH.startAnimation(mines);
                    imageRotCH.setRotation(90);
                    ch.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotCH.startAnimation(mines);
                    raspCH.setVisibility(View.VISIBLE);
                    imageRotCH.setRotation(0);
                    ch.setBackgroundColor(OpenClose[1]);

                    // действия, когда невидим.
                }
                break;
            case R.id.pt:
                if (raspPT.getVisibility() == View.VISIBLE) {

                    raspPT.setVisibility(View.GONE);
                    imageRotPT.startAnimation(mines);
                    imageRotPT.setRotation(90);
                    pt.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotPT.startAnimation(mines);
                    raspPT.setVisibility(View.VISIBLE);
                    imageRotPT.setRotation(0);
                    pt.setBackgroundColor(OpenClose[1]);
                    // действия, когда невидим.
                }
                break;
            case R.id.sb:
                if (raspSB.getVisibility() == View.VISIBLE) {

                    raspSB.setVisibility(View.GONE);
                    imageRotSB.startAnimation(mines);
                    imageRotSB.setRotation(90);
                    sb.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotSB.startAnimation(mines);
                    raspSB.setVisibility(View.VISIBLE);
                    imageRotSB.setRotation(0);
                    sb.setBackgroundColor(OpenClose[1]);
                    // действия, когда невидим.
                }
                break;
            case R.id.vs:
                if (raspVS.getVisibility() == View.VISIBLE) {

                    raspVS.setVisibility(View.GONE);
                    imageRotVS.startAnimation(mines);
                    imageRotVS.setRotation(90);
                    vs.setBackgroundColor(OpenClose[0]);
                } else {
                    imageRotVS.startAnimation(mines);
                    raspVS.setVisibility(View.VISIBLE);
                    imageRotVS.setRotation(0);
                    vs.setBackgroundColor(OpenClose[1]);
                    // действия, когда невидим.
                }
                break;


        }
    }
}
