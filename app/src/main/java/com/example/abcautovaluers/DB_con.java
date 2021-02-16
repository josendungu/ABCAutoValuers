package com.example.abcautovaluers;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DB_con {

    private String data;
    private String result = "";

    private String tag = "DB_con";

    public DB_con(String data) {
        this.data = data;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getConnection(){
        try {

            String url1 = "http://192.168.0.107/abcautovaluers/login.php";
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                InputStream ips = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line = "";
                while ((line = reader.readLine()) != null){

                    result += line;
                    Log.d(tag, "dbResult:" + result);

                }
                reader.close();
                ips.close();
                con.disconnect();
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(tag, e.toString());
        }
        return result;
    }



}
