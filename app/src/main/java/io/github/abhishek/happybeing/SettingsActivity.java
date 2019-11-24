package io.github.abhishek.happybeing;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    NotificationData notificationData;

    SwitchCompat notificationSwitch;
    TextView time;

    LinearLayout set_time;
    int hour, min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7f8c8d")));
        this.getWindow().setStatusBarColor(Color.parseColor("#657070"));

        dbHelper = new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);
        notificationData = new NotificationData(getApplicationContext());


        set_time = (LinearLayout) findViewById(R.id.set_time);
        time = (TextView) findViewById(R.id.reminder_time_desc);

        notificationSwitch = (SwitchCompat) findViewById(R.id.timerSwitch);

        hour = notificationData.get_notification_hour();
        min = notificationData.get_notification_min();


        time.setText(getFormatedTime(hour, min));
        notificationSwitch.setChecked(notificationData.getNotificationStatus());

        if (!notificationData.getNotificationStatus())
            set_time.setAlpha(0.4f);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationData.setNotificationStatus(isChecked);
                if (isChecked) {
                    NotificationScheduler.setReminder(SettingsActivity.this,
                            AlarmReceiver.class, notificationData.get_notification_hour(),
                            notificationData.get_notification_min());
                    set_time.setAlpha(1f);
                } else {
                    NotificationScheduler.cancelReminder(SettingsActivity.this, AlarmReceiver.class);
                    set_time.setAlpha(0.4f);
                }
            }
        });

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationData.getNotificationStatus())
                    showTimePickerDialog(notificationData.get_notification_hour(), notificationData.get_notification_min());
            }
        });
    }

    //deletes all entries from the database
    public void update(View v) {
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);
    }


    //This method shows the analog clock with which the user sets the reminder time.
    private void showTimePickerDialog(int h, int m) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        notificationData.set_notification_hour(hour);
                        notificationData.set_notification_min(min);
                        time.setText(getFormatedTime(hour, min));
                        NotificationScheduler.setReminder(SettingsActivity.this,
                                AlarmReceiver.class, notificationData.get_notification_hour(), notificationData.get_notification_min());
                    }
                }, h, m, false);
        builder.setCustomTitle(view);
        builder.show();
    }

    //the following two methods are directly sourced from
    // https://github.com/jaisonfdo/RemindMe/blob/master/app/src/main/java/com/droidmentor/remindme/MainActivity.java
    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date date = simpleDateFormat.parse(oldDateString);
            simpleDateFormat.applyPattern(NEW_FORMAT);
            newDateString = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            return getResources().getConfiguration().locale;
        }
    }
}