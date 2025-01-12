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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class ThirdActivity extends AppCompatActivity {


    private static final String THIRD_ACTIVITY_TAG = "THIRD_ACTIVITY_TAG";

    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    private String userToken = "";

    private String deviceToken = "c80zn9tfv9oiluyp2tss";

    private String postTelemetryUrl = "";

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    private Retrofit retrofit;


    //telemetry parameters

    private String joystickAxisX = "333";
    private String joystickAxisY = "333";

    interface postJoystick{

        @POST("/api/v1/c80zn9tfv9oiluyp2tss/telemetry")
        Call<Void> PostJoystickTelemetry( @Body JoystickPostTelemetry joystickTelemetry);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Get the text to be shown from the calling intent and set it in the layout
        Intent inputIntent = getIntent();
        userToken = inputIntent.getStringExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN);

        postTelemetryUrl = "/api/v1/" + userToken + "/telemetry";

        Log.d(THIRD_ACTIVITY_TAG,"User token retreived -> " + userToken);
        Log.d(THIRD_ACTIVITY_TAG,"URL to post telemetry -> " + postTelemetryUrl);

    //RetroFit initialization

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userPostJoystickTelemetry();


        try{
            final JoystickJhr joystick = findViewById(R.id.joystick);
            joystick.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    joystick.move(motionEvent);

                    //debug prints

                    Log.d(THIRD_ACTIVITY_TAG,"AXIS X ->" + joystick.joyX());
                    Log.d(THIRD_ACTIVITY_TAG,"AXIS Y ->" + joystick.joyY());
                    Log.d(THIRD_ACTIVITY_TAG,"ANGLE ->" + joystick.angle());
                    Log.d(THIRD_ACTIVITY_TAG,"distancia ->" + joystick.distancia());

                    joystickAxisX = String.valueOf(joystick.joyX());
                    joystickAxisY = String.valueOf(joystick.joyY());

                    //ToDo: try to avoid to send repetitive data

                    userPostJoystickTelemetry();

                    return true;
                }
            });

        }catch (Exception e){
            Log.d(THIRD_ACTIVITY_TAG,"exception" + e);
        }
    }

    private void userPostJoystickTelemetry(){

        postJoystick postJoystickRequest = retrofit.create(postJoystick.class);
        postJoystickRequest.PostJoystickTelemetry( new JoystickPostTelemetry(joystickAxisX,joystickAxisY)).enqueue(new Callback<Void>() {
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
//
//                        launchSecondActivity();

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