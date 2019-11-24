package io.github.abhishek.happybeing;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e67e22")));
        this.getWindow().setStatusBarColor(Color.parseColor("#b8641b"));
        dbHelper =new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);




        //dbHelper =new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);
        List<Entry> dataObjects = dbHelper.getAllEntry();
        PieChart chart = (PieChart) findViewById(R.id.pie_chart);

        int smile = 0, angry = 0, cry = 0, laugh = 0, sad = 0;

        List<com.github.mikephil.charting.data.PieEntry> entries = new ArrayList<>();

        //Counts every entry for each feeling nad stores it in the appropriate variable
        for (int i = 0; i < dataObjects.size(); i++) {
            if (dataObjects.get(i).getFealing() == 1) laugh++;
            if (dataObjects.get(i).getFealing() == 2) smile++;
            if (dataObjects.get(i).getFealing() == 3) sad++;
            if (dataObjects.get(i).getFealing() == 4) cry++;
            if (dataObjects.get(i).getFealing() == 5) angry++;
        }

        if (laugh > 0) {
            entries.add(new com.github.mikephil.charting.data.PieEntry(laugh, "Laughing"));
        }
        if (smile > 0) {
            entries.add(new com.github.mikephil.charting.data.PieEntry(smile, "Smiling"));
        }
        if (sad > 0) {
            entries.add(new com.github.mikephil.charting.data.PieEntry(sad, "Frowning"));
        }
        if (cry > 0) {
            entries.add(new com.github.mikephil.charting.data.PieEntry(cry, "Crying"));
        }
        if (angry > 0) {
            entries.add(new com.github.mikephil.charting.data.PieEntry(angry, "Angered"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "    Cumulative Feelings");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.invalidate();
    }
}
