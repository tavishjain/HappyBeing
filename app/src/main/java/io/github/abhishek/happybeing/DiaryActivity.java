package io.github.abhishek.happybeing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    public ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
        this.getWindow().setStatusBarColor(Color.parseColor("#2979af"));

        dbHelper = new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);
        listView = (ListView) findViewById(R.id.list_view_id);
        final List<Entry> entrys = dbHelper.getAllReverseEntry();

        stringArrayList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                stringArrayList) {

            //turns the color of the text, in the ListView, into black
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLACK);

                return view;
            }
        };

        listView.getSolidColor();
        listView.setAdapter(stringArrayAdapter);

        //populates the ListView with all the entries from the
        //database and displays to the user the date and the time
        //of the entry
        final int size = entrys.size();
        for (int i = 0; i < size; i++) {
            stringArrayList.add(entrys.get(i).getDate_Time().toString());
        }

        stringArrayAdapter.notifyDataSetChanged();

        //when one item is clicked on the list, opens the DiaryEntryActivity
        //and sends the rows' id to DiaryEntryActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long Long) {
                Intent intent = new Intent(DiaryActivity.this, DiaryEntryActivity.class);
                intent.putExtra("id", entrys.get(position).getId());
                startActivity(intent);
            }
        });
    }
}
