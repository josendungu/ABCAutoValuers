package com.example.abcautovaluers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ValuationInstance {

    private SharedPreferences valSession;
    private SharedPreferences.Editor editor;

    public static final String KEY_VALUATION_PRESENT = "valuation_present";

    public static final String KEY_PLATE_NO = "plate_no";
    public static final String KEY_LOG_BOOK = "Log Book";
    public static final String KEY_KRA = "KRA";
    public static final String KEY_ID = "ID";
    public static final String KEY_INSTRUCTIONS = "Instructions";
    public static final String KEY_FRONT = "Front";
    public static final String KEY_FRONT_LEFT = "Front Left";
    public static final String KEY_FRONT_RIGHT = "Front Right";
    public static final String KEY_REAR = "Rear";
    public static final String KEY_REAR_RIGHT = "Rear Right";
    public static final String KEY_REAR_LEFT = "Rear Left";
    public static final String KEY_HEAD_LIGHT = "Head Light";
    public static final String KEY_MILLAGE = "Millage";
    public static final String KEY_DASHBOARD = "Dashboard";
    public static final String KEY_RADIO = "Radio";
    public static final String KEY_INSURANCE = "Insurance";
    public static final String KEY_CHASSIS = "Chassis";

    public ValuationInstance(Context context){

        valSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = valSession.edit();


    }

    public void createValuationInstance(String key, String filePath){

        if (!checkValuation()){

            editor.putBoolean(KEY_VALUATION_PRESENT, true);

        }

        editor.putString(key, filePath);
        editor.commit();

    }


    public boolean checkValuation(){

        return valSession.getBoolean(KEY_VALUATION_PRESENT, false);

    }

    public void clearInstance(){

        editor.clear().commit();

    }

    public HashMap<String, String> getValuationData(){

        HashMap<String, String> valuationData = new HashMap<>();

        valuationData.put(KEY_PLATE_NO, valSession.getString(KEY_PLATE_NO, null));
        valuationData.put(KEY_LOG_BOOK, valSession.getString(KEY_LOG_BOOK, null));
        valuationData.put(KEY_KRA, valSession.getString(KEY_KRA, null));
        valuationData.put(KEY_ID, valSession.getString(KEY_ID, null));
        valuationData.put(KEY_INSTRUCTIONS, valSession.getString(KEY_INSTRUCTIONS, null));
        valuationData.put(KEY_FRONT, valSession.getString(KEY_FRONT, null));
        valuationData.put(KEY_FRONT_RIGHT, valSession.getString(KEY_FRONT_RIGHT, null));
        valuationData.put(KEY_FRONT_LEFT, valSession.getString(KEY_FRONT_LEFT, null));
        valuationData.put(KEY_REAR, valSession.getString(KEY_REAR, null));
        valuationData.put(KEY_REAR_RIGHT, valSession.getString(KEY_REAR_RIGHT, null));
        valuationData.put(KEY_REAR_LEFT, valSession.getString(KEY_REAR_LEFT, null));
        valuationData.put(KEY_MILLAGE, valSession.getString(KEY_MILLAGE, null));
        valuationData.put(KEY_HEAD_LIGHT, valSession.getString(KEY_HEAD_LIGHT, null));
        valuationData.put(KEY_DASHBOARD, valSession.getString(KEY_DASHBOARD, null));
        valuationData.put(KEY_RADIO, valSession.getString(KEY_RADIO, null));
        valuationData.put(KEY_INSURANCE, valSession.getString(KEY_INSURANCE, null));
        valuationData.put(KEY_CHASSIS, valSession.getString(KEY_CHASSIS, null));

        return valuationData;

    }

}
