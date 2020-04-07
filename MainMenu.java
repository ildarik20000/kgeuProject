package com.example.kgeu_main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.URL;

import static com.example.kgeu_main.utils.UtilsNews.generateURL;
import static com.example.kgeu_main.utils.UtilsNews.getResponceFromUrl;

public class MainMenu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DBHelper dbHelper;

    final String Pusername= "username";
    final String Pgroup= "group";
    final String Pinstitut= "institut";
    final String Pcafedra= "cafedra";
    final String Pnapravlenie= "napravlenie";
    final String Pformao= "formao";
    final String Pavtor="avtor";
    final String Pimg= "image";
    private SharedPreferences sPref;
    Boolean checkInet=true;


    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_timetable, R.id.nav_brs, R.id.nav_teacher, R.id.nav_news)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//для проверки инета
        URL generatedURL = generateURL();
        new MainMenu.QueryTask().execute(generatedURL);


        //Анимация вытеснения
        final ConstraintLayout content = findViewById(R.id.content);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);

                // а также меняем размер
                content.setScaleX(1 - slideOffset);
                content.setScaleY(1 - slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        dbHelper = new DBHelper(this);


        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //SharedPreferences
        sPref = getSharedPreferences("info", Context.MODE_PRIVATE);

         username = sPref.getString(Pusername, "");
        String group = sPref.getString(Pgroup, "");
        String img = sPref.getString(Pimg, "");

        NavigationView nv=(NavigationView) findViewById(R.id.nav_view);
        View headerLayoutImg = navigationView.getHeaderView(0);
        //View headerLayout = nv.inflateHeaderView(R.layout.nav_header_main_menu);
        ImageView imageViewMyPhoto=headerLayoutImg.findViewById(R.id.imageViewMyPhoto);
        TextView TVusername=headerLayoutImg.findViewById(R.id.TVusername);
        TextView TVgroup=headerLayoutImg.findViewById(R.id.TVgroup);


        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(checkInet) {
            Picasso.with(this)
                    .load(img)
                    .placeholder(R.drawable.logo4)
                    .error(R.drawable.logo4)
                    .into(imageViewMyPhoto);
        }

        TVusername.setText(username);
        TVgroup.setText(group);
        Log.d("TAGHEADER", "setText " + username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

//Ваш код
    }

    //Делаем запрос для проверки интернета

    class QueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                checkInet=true;
                response = getResponceFromUrl(urls[0]);
            } catch (IOException e) {
                checkInet=false;
                e.printStackTrace();
            }
            return response;
        }


    }







    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    //обработчик нажатия на кнопку
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();


        if(id==R.id.action_out)
        {
            sPref = getSharedPreferences("info", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed=sPref.edit();
            String avtor = sPref.getString(Pavtor, "");

            ed.putString(Pavtor, "false");
            ed.apply();


            Log.d("TAGHEADER", "avtor " + avtor);

            Intent i = new Intent(MainMenu.this, MainActivity.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        if(id==R.id.action_refresh)
        {


            Intent i = new Intent(MainMenu.this, Loading.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    return super.onOptionsItemSelected(item);
    }




}
