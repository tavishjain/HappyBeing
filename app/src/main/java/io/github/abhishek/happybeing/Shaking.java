package io.github.abhishek.happybeing;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Shaking extends AppCompatActivity {
    TextView st = null;
    Button reset = null;
    Button returnscore = null;
    String[] depressingWordsList = {"bad", "depressing", "depress", "comfortless", "depressive", "godforsaken", "lonely","miserable", "solemn", "wretched"};
//    String entered_data;
    SensorManager sensorManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shaking);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        reset = (Button) findViewById(R.id.reset);
        st = (TextView) findViewById(R.id.shake_text);
        Toast.makeText(this, "onCreate " + getIntent().getStringExtra("user_entered_string"), Toast.LENGTH_SHORT).show();
        returnscore = (Button) findViewById(R.id.returntoentry);

//        if (getIntent() != null) {
//            entered_data = getIntent().getStringExtra("user_entered_string");
//        }


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onPause() {//when not on this activity, stop listening
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {//back on this activity, start listening
        super.onResume();
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void resetonclick(View v) {//click reset button, reset text view and hide the buttons
        st.setText("Shake again!");
        reset.setVisibility(View.INVISIBLE);
        returnscore.setVisibility(View.INVISIBLE);
//        highestscore = 0;
    }

    public void returnonclick(View v) {//click return button, return the score
        Intent intent = new Intent();
//        intent.putExtra("result", highestscore);
        setResult(RESULT_OK, intent);
        finish();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {//record the score if it's big enough

            int sensorType = event.sensor.getType();
            //values[0]:X，values[1]：Y，values[2]：Z
            float[] values = event.values;
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {

                if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
                        .abs(values[2]) > 14)) {
//                    int score=(int) Math.sqrt(Math.pow(values[0],2)+ Math.pow(values[1],2)+ Math.pow(values[2],2));
//                    if(score>highestscore){
//                        highestscore=score;
//                    }
                int score = 0;

                KMP_String_Matching kmp = new KMP_String_Matching();
                String data = getIntent().getStringExtra("user_entered_string");
//                Toast.makeText(Shaking.this, data, Toast.LENGTH_SHORT).show();
                for (String str : depressingWordsList) {
//                    Toast.makeText(Shaking.this, data, Toast.LENGTH_SHORT).show();
                    if (kmp.KMPSearch(str, data)) {
                        score += 5;
                        break;
                    }
                }
                score += (6 - getIntent().getIntExtra("score_value", -1));
                st.setText("Your score is " + score + " !");
//                    reset.setVisibility(View.VISIBLE);
//                    returnscore.setVisibility(View.VISIBLE);
                }

            }
        }
    };


}