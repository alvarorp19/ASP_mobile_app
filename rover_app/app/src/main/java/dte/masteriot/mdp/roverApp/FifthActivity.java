package dte.masteriot.mdp.roverApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class FifthActivity extends AppCompatActivity {

    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    public static final String FIFTH_ACTIVITY_TAG = "FIFTH_ACTIVITY_TAG";

    public final static String APIURL = "https://srv-iot.diatel.upm.es";

    private String userToken = "";

    private Retrofit retrofit;

    private ImageView imageView;
    private TextView bannerText;

    //handler for repetitive task

    Handler handler = new Handler();
    Runnable runnable;

    //interfaces
    interface ThingsBoardHTTPInterface{

        @POST("/api/auth/login")
        Call<UserRequest> PostUser(@Body RequestPost RequestPost);

        @Headers({"Content-Type: application/json"})
        @GET("/api/plugins/telemetry/DEVICE/701398f0-d441-11ef-bab7-d10af52420c3/values/timeseries?keys=image")
        Call<RequestCameraData> GetCameraTelemetry(@Header("X-Authorization") String token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        // Get the text to be shown from the calling intent and set it in the layout
        Intent inputIntent = getIntent();
        userToken = inputIntent.getStringExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN);

        //init camera UI element
        imageView = findViewById(R.id.imageCamera);
        bannerText = findViewById(R.id.textViewCamera);

        //RetroFit initialization

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        runnable = new Runnable() {
            @Override
            public void run() {

                //Get image from rover each 1 second
                userGetCameraTelemetry();

                Log.d(FIFTH_ACTIVITY_TAG,"Image from the rover's camera retrieved!");

                handler.postDelayed(this, 1000);
            }
        };

        //Starting periodic routine
        handler.post(runnable);

    }



    @Override
    protected void onStop() {
        super.onStop();

        Log.d(FIFTH_ACTIVITY_TAG,"5º activity stopped");

        handler.removeCallbacks(runnable);
    }



    private void userGetCameraTelemetry() {

        FifthActivity.ThingsBoardHTTPInterface getTelemetryFromSimulatedData = retrofit.create(FifthActivity.ThingsBoardHTTPInterface.class);
        getTelemetryFromSimulatedData.GetCameraTelemetry("Bearer "+ userToken).enqueue(new Callback<RequestCameraData>() {
            @Override
            public void onResponse(Call<RequestCameraData> call, Response<RequestCameraData> response) {

                long timestamp = 0;

                try {

                    if (response.code() == 200) { //OK

                        RequestCameraData data = response.body();

                        Log.d(FIFTH_ACTIVITY_TAG, "camera data OK ");

                        //Getting camera payload

                        for (TelemetryEntry entry : data.getImage()) {

                            Log.d(FIFTH_ACTIVITY_TAG,entry.getValue());
                            decodeCameraImage(entry.getValue());

                            timestamp = entry.getTs();
                        }

                        //update banner message

                        bannerText.setText("AT " + TelemetryEntry.unixTimeToDateTime(timestamp));


                    } else if (response.code() == 401){//Unauthorized

                        Log.d(FIFTH_ACTIVITY_TAG, "Unauthorized ");

                    }

                } catch (Exception e) {
                    Log.d(FIFTH_ACTIVITY_TAG, "Response excepcion : " + e);
                }
            }

            @Override
            public void onFailure(Call<RequestCameraData> call, Throwable throwable) {
                Log.d(FIFTH_ACTIVITY_TAG, "Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }

    private void decodeCameraImage(String codedImageB64){

        Bitmap bitmap = decodeBase64ToBitmap(codedImageB64);
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
