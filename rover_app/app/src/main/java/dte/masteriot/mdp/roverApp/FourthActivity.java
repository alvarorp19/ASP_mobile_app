package dte.masteriot.mdp.roverApp;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Handler;

public class FourthActivity extends AppCompatActivity{


    private static final String FOURTH_ACTIVITY_TAG = "FOURTH_ACTIVITY_TAG";
    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    //handler for repetitive task

    Handler handler = new Handler();
    Runnable runnable;

    //elements from the layout

    private TextView textRain;
    private TextView textUV;
    private TextView textBattery;
    private TextView textPressure;
    private TextView textHumidity;
    private TextView textTemperature;
    private TextView textPM10;
    private TextView textPM2_5;

    //ThingsBoard information

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    private Retrofit retrofit;

    private String userToken = "";
    //interfaces
    interface ThingsBoardHTTPInterface{

        @POST("/api/auth/login")
        Call<UserRequest> PostUser(@Body RequestPost RequestPost);

        @Headers({"Content-Type: application/json"})
        @GET("/api/plugins/telemetry/DEVICE/e8e40740-d76d-11ef-bab7-d10af52420c3/values/timeseries?keys=rainRate,solarRadiation")
        Call<RequestSimulatedData> GetSimulatedTelemetry(@Header("X-Authorization") String token);

        @Headers({"Content-Type: application/json"})
        @GET("/api/plugins/telemetry/DEVICE/e5a176f0-cd1b-11ef-bab7-d10af52420c3/values/timeseries?keys=battery,pressure,humidity,temperature,PM10")
        Call<RequestRealData> GetRealTelemetry(@Header("X-Authorization") String token);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        //get elements from layout
        textRain = findViewById(R.id.rainData);
        textUV = findViewById(R.id.UVData);
        textBattery = findViewById(R.id.batteryData);
        textPressure = findViewById(R.id.PressureData);
        textHumidity = findViewById(R.id.humidityData);
        textTemperature = findViewById(R.id.TemperatureData);
        textPM2_5 = findViewById(R.id.PM2_5Data);
        textPM10 = findViewById(R.id.PM10Data);


        // Get the text to be shown from the calling intent and set it in the layout
        Intent inputIntent = getIntent();
        userToken = inputIntent.getStringExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN);

        Log.d(FOURTH_ACTIVITY_TAG,userToken);


        //RetroFit initialization

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userGetSimulatedTelemetry();
        userGetRealTelemetry();

        runnable = new Runnable() {
            @Override
            public void run() {

                //Get measure each 10 seconds
                userGetSimulatedTelemetry();
                userGetRealTelemetry();

                Log.d(FOURTH_ACTIVITY_TAG,"Telemetry obtained from ThingsBoard");

                handler.postDelayed(this, 10000);
            }
        };

        //Starting periodic routine
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(FOURTH_ACTIVITY_TAG,"4º activity stopped");

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //handler.post(runnable);
    }

    private void userGetSimulatedTelemetry() {

        FourthActivity.ThingsBoardHTTPInterface getTelemetryFromSimulatedData = retrofit.create(FourthActivity.ThingsBoardHTTPInterface.class);
        getTelemetryFromSimulatedData.GetSimulatedTelemetry("Bearer "+ userToken).enqueue(new Callback<RequestSimulatedData>() {
            @Override
            public void onResponse(Call<RequestSimulatedData> call, Response<RequestSimulatedData> response) {

                try {

                    if (response.code() == 200) { //OK

                        RequestSimulatedData data = response.body();

                        Log.d(FOURTH_ACTIVITY_TAG, "simulated data OK ");

                        //Getting rain parameter

                        for (TelemetryEntry entry : data.getrainRate()) {
                            textRain.setText(entry.getValue() + " mm/h | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting UV parameter

                        for (TelemetryEntry entry : data.getsolarRadiation()) {
                            textUV.setText(entry.getValue() + " W/m^2 | " + unixTimeToDateTime(entry.getTs()));
                        }



                    } else if (response.code() == 401){//Unauthorired

                        //ToDo: request user token because has expired

                    }

                } catch (Exception e) {
                    Log.d(FOURTH_ACTIVITY_TAG, "Response excepcion : " + e);
                }
            }

            @Override
            public void onFailure(Call<RequestSimulatedData> call, Throwable throwable) {
                Log.d(FOURTH_ACTIVITY_TAG, "Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }

    private void userGetRealTelemetry() {

        FourthActivity.ThingsBoardHTTPInterface getTelemetryFromRealData = retrofit.create(FourthActivity.ThingsBoardHTTPInterface.class);
        getTelemetryFromRealData.GetRealTelemetry("Bearer "+ userToken).enqueue(new Callback<RequestRealData>() {
            @Override
            public void onResponse(Call<RequestRealData> call, Response<RequestRealData> response) {

                try {

                    if (response.code() == 200) { //OK

                        Log.d(FOURTH_ACTIVITY_TAG, "CORRECTO OK ");

                        RequestRealData data = response.body();

                        //Getting battery parameter

                        for (TelemetryEntry entry : data.getBattery()) {
                            textBattery.setText(entry.getValue() + " V | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting pressure parameter

                        for (TelemetryEntry entry : data.getPressure()) {
                            textPressure.setText(entry.getValue() + " hPa | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting humidity parameter

                        for (TelemetryEntry entry : data.getHumidity()) {
                            textHumidity.setText(entry.getValue() + " %RH | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting temperature parameter

                        for (TelemetryEntry entry : data.getTemperature()) {
                            textTemperature.setText(entry.getValue() + " ºC | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting PM2.5 parameter
                        for (TelemetryEntry entry : data.getPM2_5()) {
                            textBattery.setText(entry.getValue() + " Ug/m^3 | " + unixTimeToDateTime(entry.getTs()));
                        }

                        //Getting PM10 parameter

                        for (TelemetryEntry entry : data.getPM10()) {
                            textPM10.setText(entry.getValue() + " Ug/m^3 | " + unixTimeToDateTime(entry.getTs()));
                        }





                    } else if (response.code() == 401){//Unauthorired

                        //ToDo: request user token because has expired

                        Log.d(FOURTH_ACTIVITY_TAG, "ERROR 401: ");

                    }

                } catch (Exception e) {
                    Log.d(FOURTH_ACTIVITY_TAG, "Response excepcion : " + e);
                }
            }

            @Override
            public void onFailure(Call<RequestRealData> call, Throwable throwable) {
                Log.d(FOURTH_ACTIVITY_TAG, "Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }

    public void gettoken(){
        FourthActivity.ThingsBoardHTTPInterface getTelemetryFromSimulatedData = retrofit.create(FourthActivity.ThingsBoardHTTPInterface.class);

        getTelemetryFromSimulatedData.PostUser(new RequestPost("alvaro.rodriguez.pineiro@alumnos.upm.es","19072001")).enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {

                try{

                    if (response.code() == 200){ //OK

                        userToken = response.body().getToken();


                        Log.d(FOURTH_ACTIVITY_TAG,"Response from API (token) -> " + userToken);


                    }else{//ERROR
                        Log.d(FOURTH_ACTIVITY_TAG,"Codigo de error token:" + response.code());

                    }

                }catch (Exception e){
                    Log.d(FOURTH_ACTIVITY_TAG,"excepcion en la respuesta : " + e);
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable throwable) {

            }
        });
    }


    public String unixTimeToDateTime(long unixTimestamp){

        Date date = new Date(unixTimestamp);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(date);

        return formattedDate;
    }


}
