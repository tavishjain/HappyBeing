
package io.github.abhishek.happybeing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        this.getWindow().setStatusBarColor(Color.BLACK);
    }

    /** opens get help activity via intent */
    public void openGetHelp(View view) {
        Intent intent = new Intent(this, GetHelpActivity.class);
        startActivity(intent);
    }

    /** opens add entry activity page via intent */
    public void AddEntry(View view) {
        Intent intent = new Intent(this, AddEntryActivity.class);
        startActivity(intent);
    }

    /** opens diary activity via intent */
    public void openDiary(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    /** opens settings activity via intent */
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openAnalytics (View view) {
        Intent intent = new Intent(this, AnalyticsActivity.class);
        startActivity(intent);
    }

}
