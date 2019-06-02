package com.example.jyl.mediaprojection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.jyl.mediaprojection.RecyclerViewAdapter.selectedPosition1;
import static com.example.jyl.mediaprojection.RecyclerViewAdapter.selectedPosition2;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private ArrayList<String> mImages = new ArrayList<>();
//    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheck = new ArrayList<>();
    private ArrayList<String> mStart = new ArrayList<>();
    private ArrayList<String> mEnd = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private ArrayList<String> mIndex = new ArrayList<>();


    private SharedPreferences sharedPreferences;

    int totalsize = 0;
    public static int controlIndex = 0;

    public static ImageView imageShow;
    Button bored;
    Button not_bored;
    private ArrayList<String> labelList;
    private ArrayList<String> startList;
    private ArrayList<String> endList;
    private ArrayList<String> indexList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.example.jyl.mediaprojection", MODE_PRIVATE);

        imageShow = (ImageView)findViewById(R.id.image_show);
        bored = (Button)findViewById(R.id.btn_bored);
        not_bored = (Button)findViewById(R.id.btn_notbored);

        bored.setOnClickListener(doBoredClick);
        not_bored.setOnClickListener(doNotBoredClick);


        requestCapturePermission();
        isStoragePermissionGranted();
        getImages();
    }

    Button.OnClickListener doBoredClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

        Toast.makeText(MainActivity.this, "Bored Click", Toast.LENGTH_SHORT).show();
//                    mLabel.set(selectedPosition1, "bored");
//                    mLabel.set(selectedPosition2, "bored");
            String serializedObject = sharedPreferences.getString("Label", null);
            ArrayList<String> temp = new ArrayList<>();
            if (serializedObject != null){
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                temp = gson1.fromJson(serializedObject, type);
                Log.d(TAG, "SerializeObject: " + temp);

                if(selectedPosition1 < selectedPosition2){
                    for(int i = selectedPosition1; i<=selectedPosition2; i++)
                        temp.set(i, "bored");
                }else{
                    for(int i = selectedPosition2; i<=selectedPosition1; i++)
                        temp.set(i, "bored");
                }
                Log.d(TAG, "Label SerializeObject Result: " + temp);

                Gson gson = new Gson();
                String json = gson.toJson(temp);
                sharedPreferences.edit().putString("Label", json).apply();

            }

            String serializedObject1 = sharedPreferences.getString("Start", null);
            ArrayList<String> temp1 = new ArrayList<>();
            if (serializedObject1 != null) {
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                temp1 = gson1.fromJson(serializedObject1, type);
                if(selectedPosition1 < selectedPosition2){
                    temp1.set(selectedPosition1,"1");
                }else
                    temp1.set(selectedPosition2,"1");

                Gson gson = new Gson();
                String json = gson.toJson(temp1);
                sharedPreferences.edit().putString("Start", json).apply();

                Log.d(TAG, "Start SerializeObject Result: " + temp1);

            }

            String serializedObject2 = sharedPreferences.getString("End", null);
            ArrayList<String> temp2 = new ArrayList<>();
            if (serializedObject2 != null) {
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                temp2 = gson1.fromJson(serializedObject2, type);
                if(selectedPosition1 < selectedPosition2){
                    temp2.set(selectedPosition2,"1");
                }else
                    temp2.set(selectedPosition1,"1");

                Gson gson = new Gson();
                String json = gson.toJson(temp2);
                sharedPreferences.edit().putString("End", json).apply();

                Log.d(TAG, "End SerializeObject Result: " + temp2);

            }

            String serializedObject3 = sharedPreferences.getString("Index", null);
            ArrayList<String> temp3 = new ArrayList<>();
            if (serializedObject3 != null) {
                Gson gson3 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                temp3 = gson3.fromJson(serializedObject3, type);
                controlIndex = controlIndex + 1;

                if(selectedPosition1 < selectedPosition2){
                    for(int i = selectedPosition1; i<=selectedPosition2; i++)
                        temp3.set(i, String.valueOf(controlIndex));
                }else{
                    for(int i = selectedPosition2; i<=selectedPosition1; i++)
                        temp3.set(i, String.valueOf(controlIndex));
                }

                Gson gson = new Gson();
                String json = gson.toJson(temp3);
                sharedPreferences.edit().putString("Index", json).apply();

                Log.d(TAG, "Index SerializeObject Result: " + temp3);

            }
//            if(selectedPosition1 < selectedPosition2){
//                        mStart.set(selectedPosition1, 1);
//                        mEnd.set(selectedPosition2, 1);
//                    }else{
//                        mStart.set(selectedPosition2, 1);
//                        mEnd.set(selectedPosition1, 1);
//                    }

            selectedPosition1 = -1;
            selectedPosition2 = -1;
        }
    };

    Button.OnClickListener doNotBoredClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(MainActivity.this, "Not Bored Click", Toast.LENGTH_SHORT).show();
//                    mLabel.set(selectedPosition1, "bored");
//                    mLabel.set(selectedPosition2, "bored");
            String serializedObject = sharedPreferences.getString("Label", null);
            ArrayList<String> temp = new ArrayList<>();
            if (serializedObject != null){
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                temp = gson1.fromJson(serializedObject, type);
                Log.d(TAG, "SerializeObject: " + temp);

                if(selectedPosition1 < selectedPosition2){
                    for(int i = selectedPosition1; i<=selectedPosition2; i++)
                        temp.set(i, "not_bored");
                }else{
                    for(int i = selectedPosition2; i<=selectedPosition1; i++)
                        temp.set(i, "not_bored");
                }
                Log.d(TAG, "Label SerializeObject Result: " + temp);

                Gson gson = new Gson();
                String json = gson.toJson(temp);
                sharedPreferences.edit().putString("Label", json).apply();

            }

            String serializedObject1 = sharedPreferences.getString("Start", null);
            ArrayList<String> temp1 = new ArrayList<>();
            if (serializedObject1 != null) {
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                temp1 = gson1.fromJson(serializedObject1, type);
                if(selectedPosition1 < selectedPosition2){
                    temp1.set(selectedPosition1,"1");
                }else
                    temp1.set(selectedPosition2,"1");

                Gson gson = new Gson();
                String json = gson.toJson(temp1);
                sharedPreferences.edit().putString("Start", json).apply();

                Log.d(TAG, "Start SerializeObject Result: " + temp1);

            }

            String serializedObject2 = sharedPreferences.getString("End", null);
            ArrayList<String> temp2 = new ArrayList<>();
            if (serializedObject2 != null) {
                Gson gson1 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                temp2 = gson1.fromJson(serializedObject2, type);
                if(selectedPosition1 < selectedPosition2){
                    temp2.set(selectedPosition2,"1");
                }else
                    temp2.set(selectedPosition1,"1");

                Gson gson = new Gson();
                String json = gson.toJson(temp2);
                sharedPreferences.edit().putString("End", json).apply();

                Log.d(TAG, "End SerializeObject Result: " + temp2);

            }
            selectedPosition1 = -1;
            selectedPosition2 = -1;
        }
    };

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        File imgFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Demo");
        if(imgFile.exists()){
//            Toast.makeText(this, imgFile + "exists", Toast.LENGTH_LONG).show();
            Log.d(TAG,  "Path exists: " + imgFile);

            File[] files = imgFile.listFiles();
//            Log.d(TAG, "Files Size: "+ files.length);
            mImages.clear();
//            mNames.clear();
            mCheck.clear();
            mLabel.clear();
            mStart.clear();
            mEnd.clear();
            mIndex.clear();
            if(imgFile.listFiles()!=null){
                for (int i = 0; i < files.length; i++)
                {
                    Log.d(TAG, "FileName:" + files[i].getName());
                    mImages.add(imgFile.toString() + "/" + files[i].getName());
//                mNames.add(files[i].getName().substring(0, 8) + " " + files[i].getName().substring(8, 10) + ":" +files[i].getName().substring(10, 12) + ":" + files[i].getName().substring(12, 14));
                    mCheck.add(false);


                    //Get data and turn String to ArrayList
                    if(i<totalsize){
                        String serializedObject = sharedPreferences.getString("Label", null);
                        Log.d(TAG, "Get SerializeObject: " + serializedObject);
                        labelList = new ArrayList<>();
                        if (serializedObject != null){
                            Gson gson1 = new Gson();
                            Type type = new TypeToken<ArrayList<String>>(){}.getType();
                            labelList = gson1.fromJson(serializedObject, type);
                            Log.d(TAG, "SerializeObject: " + labelList);
                            mLabel.add(labelList.get(i));
                        }
                    }
                    else
                        mLabel.add("NA");

                    if(i<totalsize){
                        //Get data and turn String to ArrayList
                        String serializedObject1 = sharedPreferences.getString("Start", null);
                        Log.d(TAG, "Get SerializeObject: " + serializedObject1);
                        startList = new ArrayList<>();
                        if (serializedObject1 != null){
                            Gson gson1 = new Gson();
                            Type type = new TypeToken<ArrayList<String>>(){}.getType();
                            startList = gson1.fromJson(serializedObject1, type);
                            Log.d(TAG, "SerializeObject: " + startList);
                            mStart.add(startList.get(i));
                        }
                    }
                    else
                        mStart.add("0");


                        //Get data and turn String to ArrayList
                    if(i<totalsize){
                        String serializedObject2 = sharedPreferences.getString("End", null);
                        Log.d(TAG, "Get SerializeObject: " + serializedObject2);
                        endList = new ArrayList<>();
                        if (serializedObject2 != null){
                            Gson gson1 = new Gson();
                            Type type = new TypeToken<ArrayList<String>>(){}.getType();
                            endList = gson1.fromJson(serializedObject2, type);
                            Log.d(TAG, "SerializeObject: " + endList);
                            mEnd.add(endList.get(i));
                        }
                    }
                    else
                        mEnd.add("0");

                    if(i<totalsize){
                        String serializedObject2 = sharedPreferences.getString("Index", null);
                        Log.d(TAG, "Get SerializeObject: " + serializedObject2);
                        indexList = new ArrayList<>();
                        if (serializedObject2 != null){
                            Gson gson1 = new Gson();
                            Type type = new TypeToken<ArrayList<String>>(){}.getType();
                            indexList = gson1.fromJson(serializedObject2, type);
                            Log.d(TAG, "SerializeObject: " + indexList);
                            mIndex.add(indexList.get(i));
                        }
                    }
                    else
                        mIndex.add("0");
                }

                Glide.with(this)
                        .asBitmap()
                        .load(mImages.get(0))
                        .into(imageShow);
            }else{
//            Toast.makeText(this, imgFile + " not exists", Toast.LENGTH_LONG).show();
                Log.d(TAG,  "Path NOT exists: " + imgFile);

            }
        }


        //Save data to preference
        Gson gson = new Gson();
        String json = gson.toJson(mLabel);
        sharedPreferences.edit().putString("Label", json).apply();
        totalsize = mLabel.size();
//
        //Save data to preference
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(mStart);
        sharedPreferences.edit().putString("Start", json1).apply();
//
        //Save data to preference
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(mEnd);
        sharedPreferences.edit(). putString("End", json2).apply();

        //Save data to preference
        Gson gson3 = new Gson();
        String json3 = gson3.toJson(mIndex);
        sharedPreferences.edit(). putString("Index", json3).apply();



        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mImages, mCheck, mLabel, mStart, mEnd, mIndex);
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
