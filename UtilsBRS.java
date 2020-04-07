package com.example.kgeu_main.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class UtilsBRS {

    private static final String API="https://eners.kgeu.ru/";
    private static final String Timetable="kkk/Brs/api.php";
    private static final String key="key";
    private static final String email="email";
    private static final String pass="pass";
    public static URL generateURL(String login, String password){
        Uri builtUri=Uri.parse(API+Timetable)
                .buildUpon()
                .appendQueryParameter(key, "")
                .appendQueryParameter(email,login)
                .appendQueryParameter(pass,password)
                .build();

        URL url = null;
        try {
            url=new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponceFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();

        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput=scanner.hasNext();
        try
        {
            if(hasInput)
            {
                return scanner.next();
            }else{
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }



    }

}
