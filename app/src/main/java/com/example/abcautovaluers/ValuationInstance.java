package com.example.abcautovaluers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ValuationInstance {

    private final SharedPreferences valSession;
    private final SharedPreferences.Editor editor;

    private Context mContext;

    public static final String KEY_VALUATION_PRESENT = "valuation_present";

    public static final String KEY_PLATE_NO = "plate_no";
    public static final String KEY_LOG_BOOK = "Log Book";
    public static final String KEY_KRA = "KRA Pin";
    public static final String KEY_ID = "ID";
    public static final String KEY_INSTRUCTIONS = "Instructions";
    public static final String KEY_FRONT = "*Front";
    public static final String KEY_FRONT_LEFT = "*Front Left";
    public static final String KEY_FRONT_RIGHT = "*Front Right";
    public static final String KEY_REAR = "*Rear";
    public static final String KEY_REAR_RIGHT = "*Rear Right";
    public static final String KEY_REAR_LEFT = "*Rear Left";
    public static final String KEY_HEAD_LIGHT = "*Head Light";
    public static final String KEY_ENGINE = "*Engine";
    public static final String KEY_MILLAGE = "*Millage";
    public static final String KEY_DASHBOARD = "*Dashboard";
    public static final String KEY_RADIO = "*Radio";
    public static final String KEY_INSURANCE = "*Insurance";
    public static final String KEY_CHASSIS = "*Chassis";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_ASSIGNED_TO = "assigned_to";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_COUNTY = "county";
    public static final String KEY_TOWN = "town";
    public static final String KEY_DAY = "day";
    public static final String KEY_TIME = "time";
    public static final String KEY_SCHEDULE_ID = "schedule_id";


    public ValuationInstance(Context context) {

        valSession = context.getSharedPreferences("valuationInstanceSession", Context.MODE_PRIVATE);
        editor = valSession.edit();

        mContext = context;


    }

    public void addValuationItem(String key, String filePath) {

        if (!checkValuation()) {

            editor.putBoolean(KEY_VALUATION_PRESENT, true);

        }

        editor.putString(key, filePath);
        editor.commit();

    }

    public File getValuationItem(String key) {

        return new File(Objects.requireNonNull(valSession.getString(key, null)));

    }

    public void addScheduleDetails(ScheduleDetails scheduleDetails) {
        addValuationItem(KEY_SURNAME, scheduleDetails.getSurname());
        addValuationItem(KEY_FIRST_NAME, scheduleDetails.getFirstName());
        addValuationItem(KEY_LAST_NAME, scheduleDetails.getLastName());
        addValuationItem(KEY_PLATE_NO, scheduleDetails.getPlateNumber());
        addValuationItem(KEY_ASSIGNED_TO, scheduleDetails.getAssignedTo());
        addValuationItem(KEY_EMAIL, scheduleDetails.getEmail());
        addValuationItem(KEY_PHONE_NUMBER, scheduleDetails.getPhoneNumber());
        addValuationItem(KEY_CLIENT_ID, scheduleDetails.getId());
        addValuationItem(KEY_SCHEDULE_ID, scheduleDetails.getScheduleId());
    }

    public ScheduleDetails getScheduleDetails() {
        String surname = valSession.getString(KEY_SURNAME, null);
        String first_name = valSession.getString(KEY_FIRST_NAME, null);
        String last_name = valSession.getString(KEY_LAST_NAME, null);
        String assigned_to = valSession.getString(KEY_ASSIGNED_TO, null);
        String email = valSession.getString(KEY_EMAIL, null);
        String phone_number = valSession.getString(KEY_PHONE_NUMBER, null);
        String client_id = valSession.getString(KEY_CLIENT_ID, null);
        String schedule_id = valSession.getString(KEY_SCHEDULE_ID, null);
        String plate_no = valSession.getString(KEY_PLATE_NO, null);
        String county = valSession.getString(KEY_COUNTY, null);
        String town = valSession.getString(KEY_TOWN, null);
        String day = valSession.getString(KEY_DAY, null);
        String time = valSession.getString(KEY_TIME, null);

        return new ScheduleDetails(surname, first_name, last_name, phone_number, email, client_id, schedule_id, plate_no, county, town, null, day, time, assigned_to, false);
    }

    public boolean checkValuation() {

        return valSession.getBoolean(KEY_VALUATION_PRESENT, false);

    }

    public void clearInstance() {

        deleteFiles();
        editor.clear().commit();

    }

    public String getPlateNumber() {

        return valSession.getString(KEY_PLATE_NO, null);

    }

    public HashMap<String, String> getValuationPresentState() {

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
        valuationData.put(KEY_ENGINE, valSession.getString(KEY_ENGINE, null));

        return valuationData;

    }

    private Boolean checkIfNull(String key) {

        String path = valSession.getString(key, null);
        return path == null;

    }

    public HashMap<String, File> getValuationData() {

        HashMap<String, File> valuationData = new HashMap<>();

        if (checkIfNull(KEY_LOG_BOOK)) {

            valuationData.put(KEY_LOG_BOOK, null);

        } else {

            valuationData.put(KEY_LOG_BOOK, new File(Objects.requireNonNull(valSession.getString(KEY_LOG_BOOK, null))));

        }

        if (checkIfNull(KEY_KRA)) {

            valuationData.put(KEY_KRA, null);

        } else {

            valuationData.put(KEY_KRA, new File(Objects.requireNonNull(valSession.getString(KEY_KRA, null))));

        }

        if (checkIfNull(KEY_INSTRUCTIONS)) {

            valuationData.put(KEY_INSTRUCTIONS, null);

        } else {

            valuationData.put(KEY_INSTRUCTIONS, new File(Objects.requireNonNull(valSession.getString(KEY_INSTRUCTIONS, null))));


        }

        if (checkIfNull(KEY_ID)) {

            valuationData.put(KEY_ID, null);

        } else {

            valuationData.put(KEY_ID, new File(Objects.requireNonNull(valSession.getString(KEY_ID, null))));

        }

        if (!checkIfNull(KEY_FRONT)){

            valuationData.put(KEY_FRONT, new File(Objects.requireNonNull(valSession.getString(KEY_FRONT, null))));

        } else {
            valuationData.put(KEY_FRONT, null);

        }

        if (!checkIfNull(KEY_HEAD_LIGHT)){

            valuationData.put(KEY_HEAD_LIGHT, new File(Objects.requireNonNull(valSession.getString(KEY_HEAD_LIGHT, null))));

        } else {

            valuationData.put(KEY_HEAD_LIGHT, null);

        }

        if (!checkIfNull(KEY_MILLAGE)){

            valuationData.put(KEY_MILLAGE, new File(Objects.requireNonNull(valSession.getString(KEY_MILLAGE, null))));

        } else {
            valuationData.put(KEY_MILLAGE, null);

        }

        if (!checkIfNull(KEY_ENGINE)){

            valuationData.put(KEY_ENGINE, new File(Objects.requireNonNull(valSession.getString(KEY_ENGINE, null))));

        } else {
            valuationData.put(KEY_ENGINE, null);

        }


        if (!checkIfNull(KEY_REAR_LEFT)){

            valuationData.put(KEY_REAR_LEFT, new File(Objects.requireNonNull(valSession.getString(KEY_REAR_LEFT, null))));

        } else {

            valuationData.put(KEY_REAR_LEFT, null);

        }

        if (!checkIfNull(KEY_REAR_RIGHT)){

            valuationData.put(KEY_REAR_RIGHT, new File(Objects.requireNonNull(valSession.getString(KEY_REAR_RIGHT, null))));

        } else {

            valuationData.put(KEY_REAR_RIGHT, null);

        }

        if (!checkIfNull(KEY_REAR)){

            valuationData.put(KEY_REAR, new File(Objects.requireNonNull(valSession.getString(KEY_REAR, null))));

        } else {

            valuationData.put(KEY_REAR, null);

        }

        if (!checkIfNull(KEY_FRONT_LEFT)){

            valuationData.put(KEY_FRONT_LEFT, new File(Objects.requireNonNull(valSession.getString(KEY_FRONT_LEFT, null))));

        } else {

            valuationData.put(KEY_FRONT_LEFT, null);

        }

        if (!checkIfNull(KEY_FRONT_RIGHT)){

            valuationData.put(KEY_FRONT_RIGHT, new File(Objects.requireNonNull(valSession.getString(KEY_FRONT_RIGHT, null))));

        } else {
            valuationData.put(KEY_FRONT_RIGHT, null);

        }

        if (checkIfNull(KEY_DASHBOARD)) {

            valuationData.put(KEY_DASHBOARD, null);

        } else {

            valuationData.put(KEY_DASHBOARD, new File(Objects.requireNonNull(valSession.getString(KEY_DASHBOARD, null))));

        }

        if (checkIfNull(KEY_RADIO)) {

            valuationData.put(KEY_RADIO, null);

        } else {

            valuationData.put(KEY_RADIO, new File(Objects.requireNonNull(valSession.getString(KEY_RADIO, null))));

        }

        if (checkIfNull(KEY_INSURANCE)) {

            valuationData.put(KEY_INSURANCE, null);

        } else {

            valuationData.put(KEY_INSURANCE, new File(Objects.requireNonNull(valSession.getString(KEY_INSURANCE, null))));

        }

        if (checkIfNull(KEY_CHASSIS)) {

            valuationData.put(KEY_CHASSIS, null);

        } else {

            valuationData.put(KEY_CHASSIS, new File(Objects.requireNonNull(valSession.getString(KEY_CHASSIS, null))));

        }



        return valuationData;

    }

    @NotNull
    public static ArrayList<String> getItemList() {

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(KEY_LOG_BOOK);
        arrayList.add(KEY_KRA);
        arrayList.add(KEY_ID);
        arrayList.add(KEY_INSTRUCTIONS);
        arrayList.add(KEY_FRONT);
        arrayList.add(KEY_FRONT_RIGHT);
        arrayList.add(KEY_FRONT_LEFT);
        arrayList.add(KEY_REAR);
        arrayList.add(KEY_REAR_RIGHT);
        arrayList.add(KEY_REAR_LEFT);
        arrayList.add(KEY_ENGINE);
        arrayList.add(KEY_MILLAGE);
        arrayList.add(KEY_HEAD_LIGHT);
        arrayList.add(KEY_DASHBOARD);
        arrayList.add(KEY_RADIO);
        arrayList.add(KEY_INSURANCE);
        arrayList.add(KEY_CHASSIS);

        return arrayList;

    }

    private void deleteFiles() {

        HashMap<String, File> valuationData = getValuationData();

        for (java.util.Map.Entry<String, File> stringFileEntry : valuationData.entrySet()) {

            if (stringFileEntry != null) {

                File file = stringFileEntry.getValue();
                if (file != null) {
                    if (file.exists()) {
                        boolean deleted = file.delete();

                        if (!deleted) {

                            String text = "File " + file.getAbsolutePath() + " was not deleted. Please delete it manually";
                            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();

                        }
                    }
                } else {


                    Log.d("Delete", "File " + stringFileEntry + " was not deleted. Please delete it manually");

                }


            }

        }


    }


}
