package io.github.abhishek.happybeing;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE=100;


    //This method creates a repeating alarm
    public static void setReminder(Context context, Class<?> cls, int hour, int min)
    {
        Calendar calendar= Calendar.getInstance();

        Calendar setcalendar= Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        cancelReminder(context,cls);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        ComponentName receiver= new ComponentName(context, cls);
        PackageManager pm= context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1= new Intent(context, cls);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(
                context, DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am=(AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                setcalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    //This method cancels the alarm
    public static void cancelReminder(Context context, Class<?> cls){

        ComponentName receiver= new ComponentName(context, cls);
        PackageManager pm= context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent cancelIntent= new Intent(context, cls);
        PendingIntent pendingcancelIntent= PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE,
                cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am=(AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingcancelIntent);
        pendingcancelIntent.cancel();

    }

    //This is the method that builds the notification
    public static void showNotification(Context context, Class<?> cls, String title, String content){

        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent= new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder= TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent=stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);

        Notification notification= builder
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
    }



}