package io.github.abhishek.happybeing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction()!= null && context!=null){
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                NotificationData notificationData=new NotificationData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class,
                        notificationData.get_notification_hour(),
                        notificationData.get_notification_min());
                return;
            }
        }

        NotificationScheduler.showNotification(context, AddEntryActivity.class,
                "Add a new log!", "Write about your day :)");
    }
}