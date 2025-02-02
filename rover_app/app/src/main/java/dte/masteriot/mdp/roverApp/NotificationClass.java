package dte.masteriot.mdp.roverApp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.GET;


public class NotificationClass implements Runnable{

    interface ThingsBoardAlarms{

        @GET("/api/plugins/telemetry/DEVICE/e5a176f0-cd1b-11ef-bab7-d10af52420c3/values/attributes/SHARED_SCOPE?keys=alarm_hot,alarm_rain,alarm_cold,alarm_pollution,alarm_fire")
        Call<List<alarmDataEntry>> GetAlarms(@Header("X-Authorization") String authorization);
    }


    private static final String NOTIFICATION_MSG = "NOTIFICATION_MSG";
    private Context contextoGlobal;

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    private Retrofit retrofit;

    private String userToken = "";

    private Map<String, Boolean> mapa = new HashMap<>();


    public NotificationClass(Context context,String userToken) {
        this.userToken = userToken;
        this.contextoGlobal = context.getApplicationContext();

        Log.d(NOTIFICATION_MSG,"userToken: " + userToken);
    }

    @Override
    public void run() {

        CreateNotificationChannel();

        //retrofit obj

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        while(true){

            try {

                Log.d(NOTIFICATION_MSG,"notification background thread running now!");
                userGetAlarmsData();
                Thread.sleep(3000);

            } catch (InterruptedException e) {

                throw new RuntimeException(e);
            }

        }

    }

    public void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String idCanal = "1234";
            CharSequence nombre = "AlarmChannel";
            String descripcion = "Channel for alarms notification";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel canal = new NotificationChannel(idCanal, nombre, importancia);
            canal.setDescription(descripcion);

            NotificationManager manager = (NotificationManager) contextoGlobal.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(canal);
                Log.d(NOTIFICATION_MSG, "Notification channel created!");
            }
        } else {
            Log.d(NOTIFICATION_MSG, "Unable to create the notification channel!");
        }
    }


    public void showNotification(String notificationContent) {
        String idCanal = "1234";
        int idNotificacion = 2;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contextoGlobal, idCanal)
                .setSmallIcon(R.drawable.rover_icon)
                .setContentTitle("¡New Alarm!")
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(contextoGlobal);

        // Verificar permisos de notificación
        if (ContextCompat.checkSelfPermission(contextoGlobal, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Aquí es donde debes delegar la solicitud de permisos a la actividad
            // Como la clase no puede solicitar permisos, puedes llamar un método de la actividad para hacerlo
            Log.d(NOTIFICATION_MSG, "Permission not granted for POST_NOTIFICATIONS.");
            return;
        }

        Log.d(NOTIFICATION_MSG, "Showing notification!");
        manager.notify(idNotificacion, builder.build());
    }


    public void requestNotificationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
    }


    private void userGetAlarmsData() {

        ThingsBoardAlarms alarmsInterface = retrofit.create(ThingsBoardAlarms.class);
        alarmsInterface.GetAlarms("Bearer "+ userToken).enqueue(new Callback<List<alarmDataEntry>>() {
            @Override
            public void onResponse(Call<List<alarmDataEntry>> call, Response<List<alarmDataEntry>> response) {

                try {

                    if (response.code() == 200) { //OK

                        Log.d(NOTIFICATION_MSG, response.body().toString());


                        //JSONArray data = new JSONArray(str);

                        Log.d(NOTIFICATION_MSG, "Alarms data OK ");

                        List<alarmDataEntry> data = response.body();

                        //checking alarms

                        for (alarmDataEntry entry : data) {
                            Log.d(NOTIFICATION_MSG, "Alarm name -> " + entry.getKey());
                            Log.d(NOTIFICATION_MSG,"Hot alarm status -> " + entry.isValue());

                            mapa.put(entry.getKey(),entry.isValue());

                            //going through all the map
                            for (Map.Entry<String, Boolean> entr : mapa.entrySet()) {
                                //Log.d(NOTIFICATION_MSG,"Clave: " + entr.getKey() + ", Valor: " + entr.getValue());
                                if (entr.getValue()) {

                                    //New alarm has arisen
                                    showNotification("Alarm " + entr.getKey() + " has arisen");


                                }
                            }
                        }


                    } else if (response.code() == 401){//Unauthorired

                        //ToDo: request user token because has expired

                        Log.d(NOTIFICATION_MSG,"ERROR -> " + response);

                    }

                } catch (Exception e) {
                    Log.d(NOTIFICATION_MSG, "Response excepcion : " + e);
                }
            }

            @Override
            public void onFailure(Call<List<alarmDataEntry>> call, Throwable throwable) {
                Log.d(NOTIFICATION_MSG, "Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }

}
