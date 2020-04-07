package com.example.kgeu_main.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class UtilsNews {

    private static final String API="https://eners.kgeu.ru/";
    private static final String Timetable="kkk/News/api.php";
    private static final String key1="country";
    private static final String key2="apiKey";
    public static URL generateURL(){
        Uri builtUri=Uri.parse(API+Timetable)
                .buildUpon()
                .appendQueryParameter(key1,"ru")
                .appendQueryParameter(key2,"good")
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
