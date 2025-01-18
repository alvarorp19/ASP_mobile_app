package dte.masteriot.mdp.roverApp;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

public class FourthActivity extends AppCompatActivity{


    private static final String FOURTH_ACTIVITY_TAG = "FOURTH_ACTIVITY_TAG";
    public static final String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";



    //elements from the layout

    private TextView textRain;
    private TextView textUV;

    //ThingsBoard information

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    private Retrofit retrofit;

    private String userToken = "";
    //interfaces
    interface ThingsBoardHTTPInterface{

        @POST("/api/auth/login")
        Call<UserRequest> PostUser(@Body RequestPost RequestPost);

        @Headers({"Content-Type: application/json"})
        @GET("/api/plugins/telemetry/DEVICE/e5a176f0-cd1b-11ef-bab7-d10af52420c3/values/timeseries?keys=rain,UV")
        Call<RequestSimulatedData> GetSimulatedTelemetry(@Header("X-Authorization") String token);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        //get element from layout
        textRain = findViewById(R.id.rainData);
        textUV = findViewById(R.id.UVData);


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
    }

    private void userGetSimulatedTelemetry() {

        FourthActivity.ThingsBoardHTTPInterface getTelemetryFromSimulatedData = retrofit.create(FourthActivity.ThingsBoardHTTPInterface.class);
        getTelemetryFromSimulatedData.GetSimulatedTelemetry("Bearer "+ userToken).enqueue(new Callback<RequestSimulatedData>() {
            @Override
            public void onResponse(Call<RequestSimulatedData> call, Response<RequestSimulatedData> response) {

                try {

                    if (response.code() == 200) { //OK

                        RequestSimulatedData data = response.body();

                        //Getting rain parameter

                        for (TelemetryEntry entry : data.getRain()) {
                            Log.d(FOURTH_ACTIVITY_TAG,"  Timestamp: " + entry.getTs());
                            Log.d(FOURTH_ACTIVITY_TAG,"  Rain Value: " + entry.getValue());
                            textRain.setText(entry.getValue() + " mm/h");
                        }

                        //Getting UV parameter

                        for (TelemetryEntry entry : data.getUV()) {
                            Log.d(FOURTH_ACTIVITY_TAG,"  Timestamp: " + entry.getTs());
                            Log.d(FOURTH_ACTIVITY_TAG,"  UV Value: " + entry.getValue());
                            textUV.setText(entry.getValue() + " W/m^2");
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

}
