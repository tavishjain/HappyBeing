package io.github.abhishek.happybeing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DiaryEntryActivity extends AppCompatActivity {
    private ImageView emjoy;
    private ImageView photo_orizontal;
    private ImageView photo_vertical;
    private ImageView score_bar;
    private ImageView shaking_score_text;
    private TextView diaryText;
    public TextView display_date;
    public TextView shake_score;
    private Button showinmaps;
    private TextView locText;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        dbHelper = new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
        this.getWindow().setStatusBarColor(Color.parseColor("#2979af"));

        int id = getIntent().getIntExtra("id",0);
        final Entry e = dbHelper.getEntry(id);

        //connection java obj to the xml obj
        diaryText = (TextView) findViewById(R.id.textView);
        display_date = (TextView) findViewById(R.id.date);
        showinmaps = (Button) findViewById(R.id.showLocation);
        emjoy=(ImageView)findViewById(R.id.faccina);
        photo_orizontal=(ImageView)findViewById(R.id.picoftheday_orizontal);
        photo_vertical=(ImageView)findViewById(R.id.picoftheday_vertical);
        shake_score= (TextView)findViewById(R.id.score);
        score_bar= (ImageView) findViewById(R.id.bar);

        //botton that send an intent to google maps to show the location
        //stored in the entry
        showinmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getLocation()!=null) {
                    Uri gmmIntentUri = Uri.parse(e.getLocation().toString());
                    Intent ShowInMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    ShowInMapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(ShowInMapIntent);
                }
                else Toast.makeText(DiaryEntryActivity.this, "No location in this entry",
                        Toast.LENGTH_LONG).show();
            }
        });
        //if there is a photo in this entry show it
        if(e.getPhoto()!=null) {
            Bitmap photo = null ;
            photo= BitmapFactory.decodeByteArray(e.getPhoto(), 0, e.getPhoto().length);
            if(photo.getHeight()>=photo.getWidth()){
                photo_vertical.setImageBitmap(photo);
                photo_orizontal.setVisibility(View.INVISIBLE);
            }
            else{
                photo_orizontal.setImageBitmap(photo);
                photo_vertical.setVisibility(View.INVISIBLE);
            }

        }
        //if there is no photo the ImageView is not visibile
        else {
            photo_vertical.setVisibility(View.INVISIBLE);
            photo_orizontal.setVisibility(View.INVISIBLE);
        }

        //show the emoji of this entry
        if (e.getFealing() == 1)
            emjoy.setImageResource(R.drawable.laugh_color);
        else if (e.getFealing() == 2)
            emjoy.setImageResource(R.drawable.smile_color);
        else if(e.getFealing()==3)
            emjoy.setImageResource(R.drawable.sad_color);
        else if(e.getFealing()==4)
            emjoy.setImageResource(R.drawable.cry_color);
        else if(e.getFealing()==5)
            emjoy.setImageResource(R.drawable.angry_color);
        //show the date and the time
        display_date.setText(e.getDate_Time());
        //show the text
        diaryText.setText(e.getText());
        //if there is a shakescore show the score
        if(e.getShakescore()<14){
            shake_score.setText("No Shaking Score");
            score_bar.setVisibility(View.INVISIBLE);
        }
        //evry score range had a different image
        else {
            shake_score.setText("" + e.getShakescore());
            if (e.getShakescore() >= 80) score_bar.setImageResource(R.drawable.score_80);
            else if (e.getShakescore() >= 70) score_bar.setImageResource(R.drawable.score_70);
            else if (e.getShakescore() >= 60) score_bar.setImageResource(R.drawable.score_60);
            else if (e.getShakescore() >= 50) score_bar.setImageResource(R.drawable.score_50);
            else if (e.getShakescore() >= 40) score_bar.setImageResource(R.drawable.score_40);
            else if (e.getShakescore() >= 14) score_bar.setImageResource(R.drawable.score_14);
        }
    }
}
