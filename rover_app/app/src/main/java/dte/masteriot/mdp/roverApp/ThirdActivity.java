package dte.masteriot.mdp.roverApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.joystickjhr.JoystickJhr;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class ThirdActivity extends AppCompatActivity {


    private static final String THIRD_ACTIVITY_TAG = "THIRD_ACTIVITY_TAG";

    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    private static final int MAX_JOYSTICK_VALUE = 100;
    private static final int MIN_JOYSTICK_VALUE = -100;

    private String userToken = "";

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    private Retrofit retrofit;


    Runnable runnable;
    Handler handler = new Handler();

    //telemetry parameters

    private String joystickAxisX = "";
    private String joystickAxisY = "";
    private String joystickAngle = "";
    private String joystickDistance = "";

    //joystick parameters
    private int currentDirection;
    private int lastDirection = 0;

    //timer parameter

    private boolean flagJoystick = false;

    //interface

    interface postJoystick{

        @POST("/api/plugins/telemetry/DEVICE/e5a176f0-cd1b-11ef-bab7-d10af52420c3/SHARED_SCOPE")
        Call<Void> PostJoystickTelemetry(@Header("X-Authorization") String authorization, @Body JoystickPostTelemetry joystickTelemetry);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Get the text to be shown from the calling intent and set it in the layout
        Intent inputIntent = getIntent();
        userToken = inputIntent.getStringExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN);

        Log.d(THIRD_ACTIVITY_TAG,"User token retreived -> " + userToken);

    //RetroFit initialization

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        try{
            final JoystickJhr joystick = findViewById(R.id.joystick);
            joystick.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    joystick.move(motionEvent);

                    //debug prints

//                    Log.d(THIRD_ACTIVITY_TAG,"AXIS X ->" + joystick.joyX());
//                    Log.d(THIRD_ACTIVITY_TAG,"AXIS Y ->" + joystick.joyY());
//                    Log.d(THIRD_ACTIVITY_TAG,"ANGLE ->" + joystick.angle());
//                    Log.d(THIRD_ACTIVITY_TAG,"distancia ->" + joystick.distancia());

                    //casting joystick axis values

                    int xAxis = (int) joystick.joyX();
                    int yAxis = (int) joystick.joyY();

                    //Checking max values

                    if(xAxis > MAX_JOYSTICK_VALUE){

                        xAxis = 100;

                    } else if (xAxis < MIN_JOYSTICK_VALUE) {

                        xAxis = -100;

                    }

                    //Checking min values

                    if(yAxis > MAX_JOYSTICK_VALUE){

                        yAxis = 100;

                    } else if (yAxis < MAX_JOYSTICK_VALUE) {

                        yAxis = -100;
                    }

                    joystickAxisX = String.valueOf(xAxis);
                    joystickAxisY = String.valueOf(yAxis);
                    joystickAngle = String.valueOf(joystick.angle());
                    joystickDistance = String.valueOf(joystick.distancia());

                    currentDirection = joystick.getDireccion();

                    //Log.d(THIRD_ACTIVITY_TAG,"direction ->" + currentDirection);

                    if(currentDirection != lastDirection){
                        lastDirection = currentDirection;

                        if(flagJoystick){//this flag is set be the timer

                            flagJoystick = false;

                            Log.d(THIRD_ACTIVITY_TAG,"Sending joystick telemetry to thingsboard (dir " + currentDirection + ")");
                            Log.d(THIRD_ACTIVITY_TAG,"data to be sent: X ->" + joystickAxisX + " Y -> " + joystickAxisY);

                            userPostJoystickTelemetry();
                        }
                    }

                    return true;
                }
            });

        }catch (Exception e){
            Log.d(THIRD_ACTIVITY_TAG,"exception" + e);
        }

        runnable = new Runnable() {
            @Override
            public void run() {


                if (!flagJoystick){

                    flagJoystick = true;
                }

                Log.d(THIRD_ACTIVITY_TAG,"timer 1 second activated | TRUE");

                handler.postDelayed(this, 1000);
            }
        };

        //Starting periodic routine
        handler.post(runnable);
    }

    private void userPostJoystickTelemetry(){

        postJoystick postJoystickRequest = retrofit.create(postJoystick.class);
        postJoystickRequest.PostJoystickTelemetry( "Bearer " + userToken,new JoystickPostTelemetry(joystickAxisX,joystickAxisY)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                try{

                    if (response.code() == 200){ //OK

//                        userToken = response.body().getToken();
//                        userRefreshToken = response.body().getRefreshToken();
//
                        Log.d(THIRD_ACTIVITY_TAG,"respuesta OK");
//                        Log.d(MAINACTIVITYTAG,"Response from API (token) -> " + userToken);
//                        Log.d(MAINACTIVITYTAG,"Response from API (Refresh token) -> " + userRefreshToken);

                    }else{//ERROR
                        Log.d(THIRD_ACTIVITY_TAG,"Codigo de error:" + response.code());
                    }

                }catch (Exception e){
                    Log.d(THIRD_ACTIVITY_TAG,"excepcion en la respuesta : " + e);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(THIRD_ACTIVITY_TAG,"Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }

}