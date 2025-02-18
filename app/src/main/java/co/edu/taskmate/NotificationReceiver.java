package co.edu.taskmate;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener el título y la descripción de la tarea
        String title = intent.getStringExtra("TASK_TITLE");
        String description = intent.getStringExtra("TASK_DESCRIPTION");

        // Mostrar la notificación
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendNotification(title, description);
    }
}