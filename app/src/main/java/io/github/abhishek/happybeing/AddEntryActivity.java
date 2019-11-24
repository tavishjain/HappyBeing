package io.github.abhishek.happybeing;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEntryActivity extends AppCompatActivity {
    ImageView emojiup;//record the chosed emoji view
    private static final int PHOTO_GRAPH = 1;// request code of taking picture
    private static final int PHOTO_ZOOM = 2; // request code of zooming phtoto
    private static final int PHOTO_RESOULT = 3;// request code of the result
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int SHAKE_SCORE = 4;//request code of getting shaking score


    private MyDatabaseHelper dbHelper;
    private int emoji_id=0;
    private ByteArrayOutputStream os=null;
    private int shakescore=0;


    private TextView postionView;
    private LocationManager locationManager;
    private EditText edittext;
    private String locationProvider;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        dbHelper = new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,1);
        //dbHelper.onUpgrade(dbHelper.getWritableDatabase(),2,3);

        ActionBar actionBar = getSupportActionBar();
        edittext = findViewById(R.id.new_entry_text);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9b59b6")));
        this.getWindow().setStatusBarColor(Color.parseColor("#7c4791"));


        //TextView for location
        postionView = (TextView) findViewById(R.id.textView6);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //if the phone has a GPS sensor
            locationProvider = LocationManager.GPS_PROVIDER;
        }else{
            Toast.makeText(this, "No GPS sensor", Toast.LENGTH_SHORT).show();
            return ;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //get Location
        location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            //not null, show location
            showLocation(location);
        }
        //listen to the change of location
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }


    /**
     * show latitude and longitude
     * @param location
     */



    private void showLocation(Location location){//show the loacation in the textview
        Geocoder gc = new Geocoder(this);
        List<Address> addresses = null;
        String msg = "";
        if (location != null) {
            try {
                addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    msg += addresses.get(0).getThoroughfare();
                    msg += "," + addresses.get(0).getLocality();
                    msg += "," + addresses.get(0).getAdminArea();
                    msg += "," + addresses.get(0).getCountryName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            postionView.setText("Location：\n");
            postionView.append(msg);
            postionView.append("\nLongitude:");
            postionView.append(String.valueOf(location.getLongitude()));
            postionView.append("\nLatitude:");
            postionView.append(String.valueOf(location.getLatitude()));

        } else {
            postionView.getEditableText().clear();
            postionView.setText("Can't locate. Please check the setting.");
        }
    }


    LocationListener locationListener =  new LocationListener() {//set location listener

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //location change, reshow.
            showLocation(location);

        }
    };


    /*public void update(View v){
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(),2,3);
    }*/

    //when user click on emoji
    public void EmojionClick(View v) {//change the clicked emoji to color graph, change former clicked one to BW; change the emoji code.
        if(v==emojiup)
            return;

        if(emojiup!=null)
        {
            emojiup.setVisibility(View.INVISIBLE);
            switch(emojiup.getId()){
                case R.id.emoji1_up:
                    findViewById(R.id.emoji1_down).setVisibility(View.VISIBLE);
                    break;
                case R.id.emoji2_up:
                    findViewById(R.id.emoji2_down).setVisibility(View.VISIBLE);
                    break;
                case R.id.emoji3_up:
                    findViewById(R.id.emoji3_down).setVisibility(View.VISIBLE);
                    break;
                case R.id.emoji4_up:
                    findViewById(R.id.emoji4_down).setVisibility(View.VISIBLE);
                    break;
                case R.id.emoji5_up:
                    findViewById(R.id.emoji5_down).setVisibility(View.VISIBLE);
                    break;
            }

        }
        v.setVisibility(View.INVISIBLE);
        switch(v.getId()){
            case R.id.emoji1_down:
                emojiup=(ImageView) findViewById(R.id.emoji1_up);
                findViewById(R.id.emoji1_up).setVisibility(View.VISIBLE);
                emoji_id=1;
                break;
            case R.id.emoji2_down:
                emojiup=(ImageView) findViewById(R.id.emoji2_up);
                findViewById(R.id.emoji2_up).setVisibility(View.VISIBLE);
                emoji_id=2;
                break;
            case R.id.emoji3_down:
                emojiup=(ImageView) findViewById(R.id.emoji3_up);
                findViewById(R.id.emoji3_up).setVisibility(View.VISIBLE);
                emoji_id=3;
                break;
            case R.id.emoji4_down:
                emojiup=(ImageView) findViewById(R.id.emoji4_up);
                findViewById(R.id.emoji4_up).setVisibility(View.VISIBLE);
                emoji_id=4;
                break;
            case R.id.emoji5_down:
                emojiup=(ImageView) findViewById(R.id.emoji5_up);
                findViewById(R.id.emoji5_up).setVisibility(View.VISIBLE);
                emoji_id=5;
                break;
        }
    }

    public void shakingonclick(View v){//call shaking activity
        Intent intent = new Intent(this, Shaking.class);
//        Toast.makeText(this, edittext.getText(), Toast.LENGTH_SHORT).show();
        intent.putExtra("user_entered_string", edittext.getText().toString());
        intent.putExtra("score_value", emoji_id);
        startActivity(intent);
    }
    //when user click choose from album
    public void choosefromalbum(View vsd)//call intent to gallery
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_ZOOM);
    }
    //when user click take a photo
    public void takeaphoto(View v)//call camera
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                openCamera();
            }
        } else {
            openCamera();
        }

    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// call camera
        startActivityForResult(intent, PHOTO_GRAPH);
    }


    @Override

    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//check the permission of camera
        switch (requestCode) {
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Sorry, you disabled the camera.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){//override onActivityResult
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK||data==null) {
            Toast.makeText(getApplicationContext(),"No data back", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode){
            case PHOTO_GRAPH:
                Bundle bundle = data.getExtras();
                Bitmap photo_cam = bundle.getParcelable("data");
                ByteArrayOutputStream stream_cam = new ByteArrayOutputStream();
                photo_cam.compress(Bitmap.CompressFormat.JPEG, 100, stream_cam);
                os=stream_cam;
                ((ImageView)findViewById(R.id.new_entry_photo)).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.new_entry_photo)).setImageBitmap(photo_cam); //show the picture

                break;
            case PHOTO_ZOOM:
                startPhotoZoom(data.getData());
                break;
            case PHOTO_RESOULT:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo_al = extras.getParcelable("data");
                    ByteArrayOutputStream stream_al = new ByteArrayOutputStream();
                    photo_al.compress(Bitmap.CompressFormat.JPEG, 100, stream_al);
                    os=stream_al;
                    ((ImageView)findViewById(R.id.new_entry_photo)).setVisibility(View.VISIBLE);
                    ((ImageView)findViewById(R.id.new_entry_photo)).setImageBitmap(photo_al); //show the picture
                }
                break;
            case SHAKE_SCORE:
                Bundle shake_extras=data.getExtras();
                if(shake_extras !=null)
                {
                    int shake_score = shake_extras.getInt("score_value");
                    shakescore=shake_score;
                }
                break;


        }
    }
    public void startPhotoZoom(Uri uri) {//zoom the picture from gallery
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }


    public void savetodatabase(View v){//save to entry
        if(((EditText)findViewById(R.id.new_entry_text)).getText().toString()==null||emoji_id==0)
        {
            Toast.makeText(this, "Please say something and choose a emoji.", Toast.LENGTH_SHORT).show();
            return;
        }
        Entry entry=new Entry();
        entry.setText(((EditText)findViewById(R.id.new_entry_text)).getText().toString());
        entry.setFealing(emoji_id);
        SimpleDateFormat format = new SimpleDateFormat("EEE - dd MMM yyyy, hh:mm");
        Date date = new Date(System.currentTimeMillis());
        String time_tag = format.format(date);
        Log.e("time: ",""+time_tag);
        entry.setDate_Time(time_tag);
        if(os!=null)entry.setPhoto(os.toByteArray());
        if(shakescore!=0)entry.setShakescore(shakescore);

        if(location!=null)
        {
            entry.setLocation("geo:"+ String.valueOf(location.getLatitude())+","+ String.valueOf(location.getLongitude()));
        }
        dbHelper.addEntry(entry);

        Toast.makeText(this, "save successfully", Toast.LENGTH_SHORT).show();
        finish();


    }



}
