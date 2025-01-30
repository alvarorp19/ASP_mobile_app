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


public class NotificationClass implements Runnable{


    private static final String NOTIFICATION_MSG = "NOTIFICATION_MSG";
    private Context contextoGlobal;

    // Constructor que recibe el contexto
    public NotificationClass(Context context) {
        this.contextoGlobal = context.getApplicationContext();
    }

    @Override
    public void run() {

        CreateNotificationChannel();

        while(true){

            try {
                showNotification("New notification");
                Log.d(NOTIFICATION_MSG,"notification background thread running now!");
                Thread.sleep(3000);

            } catch (InterruptedException e) {

                throw new RuntimeException(e);
            }

        }

    }

    // Método para crear el canal de notificación
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

    // Método para mostrar la notificación
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

    // Método para pedir permisos a la actividad (delegado desde la actividad)
    public void requestNotificationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
    }
}
