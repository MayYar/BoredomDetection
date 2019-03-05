package com.example.jyl.mediaprojection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheck = new ArrayList<>();

    public static ImageView imageShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageShow = (ImageView)findViewById(R.id.image_show);

        requestCapturePermission();
        isStoragePermissionGranted();
        getImages();
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        File imgFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Demo");
        if(imgFile.exists()){
//            Toast.makeText(this, imgFile + "exists", Toast.LENGTH_LONG).show();
            Log.d(TAG,  "Path exists: " + imgFile);

            File[] files = imgFile.listFiles();
            Log.d(TAG, "Files Size: "+ files.length);
            mImages.clear();
            mNames.clear();
            mCheck.clear();
            for (int i = 0; i < files.length; i++)
            {
                Log.d(TAG, "FileName:" + files[i].getName());
                mImages.add(imgFile.toString() + "/" + files[i].getName());
                mNames.add(files[i].getName().substring(0, 8) + " " + files[i].getName().substring(8, 10) + ":" +files[i].getName().substring(10, 12) + ":" + files[i].getName().substring(12, 14));
                mCheck.add(false);
            }

            Glide.with(this)
                    .asBitmap()
                    .load(mImages.get(0))
                    .into(imageShow);
        }else{
//            Toast.makeText(this, imgFile + " not exists", Toast.LENGTH_LONG).show();
            Log.d(TAG,  "Path NOT exists: " + imgFile);

        }


        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImages, mCheck);
        recyclerView.setAdapter(adapter);
    }

//    public void checkCanDrawOverlays(){
//        if (Build.VERSION.SDK_INT >= 23) {
//            if(!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                startActivityForResult(intent,0);
//            }
//            else{
//                requestCapturePermission();
//            }
//        }
//    }

//確認是否同意擷取螢幕內容
    public void requestCapturePermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
//android 5.0 up
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent it = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);

        switch (requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    requestCapturePermission();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && it != null) {
                    ScreenShotService.setResultIntent(it);
                    startService(new Intent(getApplicationContext(), ScreenShotService.class));
                    Log.e(TAG, "Start ScreenShotService");
                }
                break;

        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Renew");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getGroupId() == Menu.NONE){
            getImages();
        }
        return super.onOptionsItemSelected(item);
    }
}
