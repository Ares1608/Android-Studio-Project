package co.edu.taskmate;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("TASK_TITLE");
        String description = intent.getStringExtra("TASK_DESCRIPTION");


        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendNotification(title, description);
    }
}