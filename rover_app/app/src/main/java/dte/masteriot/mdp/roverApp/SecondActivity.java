package dte.masteriot.mdp.roverApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dte.masteriot.mdp.roverApp.R;

public class SecondActivity extends AppCompatActivity{

    private static final String SECOND_ACTIVITY_TAG = "SECOND_ACTIVITY_TAG";

    private Button controlButton;
    private Button monitorButton;
    private Button cameraButton;

    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    private Thread notificationThread;

    private String userToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get the text to be shown from the calling intent and set it in the layout
        Intent inputIntent = getIntent();
        userToken = inputIntent.getStringExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN);

        Log.d(SECOND_ACTIVITY_TAG, "User token -> " + userToken);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
//            }
//        }


        //buttons initialization
        controlButton = findViewById(R.id.button1);
        monitorButton = findViewById(R.id.button2);
        cameraButton = findViewById(R.id.button3);

        //alarms
        //creating notification thread on background

        notificationThread = new Thread(new NotificationClass(this,userToken));
        notificationThread.start();

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SECOND_ACTIVITY_TAG, "control button pressed");

                launchControlActivity();


            }
        });

        monitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SECOND_ACTIVITY_TAG, "monitor button pressed");

                launchMonitorActivity();

            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SECOND_ACTIVITY_TAG, "camera button pressed");

                launchCamaraActivity();

            }
        });

    }

    private void launchControlActivity(){

        //launching control activity
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN,userToken);
        Log.d(SECOND_ACTIVITY_TAG,"Launching 3º activity");
        startActivity(intent);
    }


    private void launchMonitorActivity(){

        //launching control activity
        Intent intent = new Intent(this, FourthActivity.class);
        intent.putExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN,userToken);
        Log.d(SECOND_ACTIVITY_TAG,"Launching 4º activity");
        startActivity(intent);
    }


    private void launchCamaraActivity(){

        //launching control activity
        Intent intent = new Intent(this, FifthActivity.class);
        intent.putExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN,userToken);
        Log.d(SECOND_ACTIVITY_TAG,"Launching 5º activity");
        startActivity(intent);
    }

}