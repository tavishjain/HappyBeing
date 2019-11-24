package io.github.abhishek.happybeing;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationData {

    private static final String APP_SHARED_PREFS="NotificationPreference";

    private SharedPreferences notificationPreferences;
    private SharedPreferences.Editor myEditor;

    private  static final String notificationStatus="notificationStatus";
    private static final String hour="hour";
    private static final String min="min";


    public NotificationData(Context context){
        this.notificationPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.myEditor= notificationPreferences.edit();
    }

    //Called to set whether or not the notification is active, effectively storing the status of the Swtich
    public void setNotificationStatus(boolean status){
        myEditor.putBoolean(notificationStatus, status);
        myEditor.commit();
    }

    //Called to set the notification target hour
    public void set_notification_hour(int h){
        myEditor.putInt(hour, h);
        myEditor.commit();
    }

    //Called to set the notification target minute
    public void set_notification_min(int m){
        myEditor.putInt(min, m);
        myEditor.commit();
    }

    //Returns the status of the Switch
    public boolean getNotificationStatus(){
        return notificationPreferences.getBoolean(notificationStatus, false);
    }

    //Returns the notification target hour
    public int get_notification_hour(){
        return notificationPreferences.getInt(hour, 20);
    }

    //Returns the notificatioon target minute
    public int get_notification_min(){
        return notificationPreferences.getInt(min, 0);
    }

}