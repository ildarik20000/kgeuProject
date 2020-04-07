package com.example.kgeu_main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button vhod;

    DBHelper dbHelper;

    EditText login, password;

    ImageView logoKGEU;

    SharedPreferences sPref;

    final String Plogin= "login";
    final String Ppassword= "password";
    final String Pusername= "username";
    final String Pgroup= "group";
    final String Pinstitut= "institut";
    final String Pcafedra= "cafedra";
    final String Pnapravlenie= "napravlenie";
    final String Pformao= "formao";
    final String Pimg= "image";
    final String Pavtor="avtor";

    Boolean flag1=true;//Чтоб на кнопку войти много не жмякали

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login=(EditText)findViewById(R.id.login);
        password=(EditText)findViewById(R.id.password);

        logoKGEU=(ImageView)findViewById(R.id.logoKGEU);
        logoKGEU.setOnClickListener(this);

        vhod=(Button) findViewById(R.id.button_vhod);
        vhod.setOnClickListener(this);


        sPref = getSharedPreferences("info", Context.MODE_PRIVATE);
        String avtor = sPref.getString(Pavtor, "");
        //Если авторизован
        Log.d("TAGPOST", "avtor avtorizovan "+avtor);
        if(avtor.equals("true"))
        {
            Intent intent= new Intent(MainActivity.this, MainMenu.class);
            startActivity(intent);
        }



    }

    //POST

    void writeToMySQL () {
        Thread threadwriteMySQL = new Thread(runwriteMySQL);
        threadwriteMySQL.start();
    }
    final Runnable runwriteMySQL = new Runnable() {
        @Override
        public void run() {
            try {
                // пишем
                URL url = new URL("https://eners.kgeu.ru/kkk/api1.php");
                final HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("POST");
                DataOutputStream os = new DataOutputStream(connect.getOutputStream());
                String str = "email="+login.getText()+"&password="+password.getText();
                os.write(str.getBytes("utf-8"));
                os.flush();
                os.close();


                //login
                sPref = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sPref.edit();
                ed.putString(Plogin, login.getText().toString());
                ed.apply();
                //password
                ed.putString(Ppassword, password.getText().toString());
                ed.apply();


                // читаем ответ
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                //Парсим JSON объект

                String username=null;
                String group=null;
                String institut=null;
                String cafedra=null;
                String napravlenie=null;
                String formao=null;
                String img=null;

                JSONObject jsonRespose = new JSONObject(String.valueOf(response));
                JSONArray jsonArray=jsonRespose.getJSONArray("login");
                JSONObject userInfo= jsonArray.getJSONObject(0);

                username=userInfo.getString("username");
                group=userInfo.getString("group");
                institut=userInfo.getString("institut");
                cafedra=userInfo.getString("cafedra");
                napravlenie=userInfo.getString("napravlenie");
                formao=userInfo.getString("formao");
                img=userInfo.getString("urlphoto");
                //Сохранение в shared Preferences

                //фото
                ed.putString(Pimg, img);
                ed.apply();
                //ФИО
                sPref = getPreferences( MODE_PRIVATE);

                ed.putString(Pusername, username);
                ed.apply();
                //группа
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pgroup, group);
                ed.apply();
                //институт
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pinstitut, institut);
                ed.apply();
                //кафедра
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pcafedra, cafedra);
                ed.apply();
                //направление
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pnapravlenie, napravlenie);
                ed.apply();
                //форма обучения
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pformao, formao);
                ed.apply();

                Log.d("TAGPOST", "responce" + jsonRespose + "=" + response.toString().length());

                Log.d("TAGPOST", "ФИО " + username);
                Log.d("TAGPOST", "Группа " + group);
                Log.d("TAGPOST", "Институт " + institut);
                Log.d("TAGPOST", "Кафедра " + cafedra);
                Log.d("TAGPOST", "Направление " + napravlenie);
                Log.d("TAGPOST", "ПФорма обучения " + formao);

                //String message = info.getString("message");


                Log.d("TAGPOST", "ПОТОК" + response.toString() + "=" + response.toString().length());
                //msg = myhandler.obtainMessage(2731,"Ответ сервера: "+response.toString()+"="+response.length());
                //myhandler.sendMessage(msg);


                br.close();
                //если авторизация прошла, запускаем загрузку

                //Если прошла авторизация чтоб не возвращаться
                sPref = getPreferences(MODE_PRIVATE);

                ed.putString(Pavtor, "true");
                ed.commit();
                //

                Intent intent= new Intent(MainActivity.this, Loading.class);

                startActivity(intent);
            } catch (IOException e) {

                e.getMessage();
            } catch (JSONException e) {
                flag1=true;
                toast("Неверный логин или пароль");
                Log.d("TAGPOST", "Неверный логин или пароль");
                e.printStackTrace();
            }

        }
    };
//Toast если неверный логин или пароль
    final Handler h = new Handler();
    private void toast(final String Text) {
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    int ficha=0;
    @Override
    public void onClick(View view) {

            switch(view.getId()) {
            case R.id.button_vhod:
                //Intent intent= new Intent(MainActivity.this, Loading.class);
                //startActivity(intent);
                if(flag1) {
                    flag1=false;
                    writeToMySQL();





                }

                break;

                case  R.id.logoKGEU:
                    ficha++;
                    if(ficha==5)
                    {
                        Toast.makeText(getApplicationContext(), "Создатели приложения: Мазитов Ильдар, Кольчурина Милена, Тяминов Даниил", Toast.LENGTH_LONG).show();

                    }
                    break;
        }
    }
}
