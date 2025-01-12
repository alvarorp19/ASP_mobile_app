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




public class ThirdActivity extends AppCompatActivity {


    private static final String THIRD_ACTIVITY_TAG = "THIRD_ACTIVITY_TAG";
    

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

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

                    return true;
                }
            });

        }catch (Exception e){
            Log.d(THIRD_ACTIVITY_TAG,"exception" + e);
        }
    }

}