package com.example.jyl.mediaprojection;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScreenShotService extends Service {

    private final static String TAG = "ScreenShotService";
    public String fileName = "NA";
    File saveFile = getMainDirectoryName();


    private static Intent resultIntent;

    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    private ImageReader imageReader;
    private Handler handler;
    private Runnable runnable;

    public ScreenShotService() {
        Log.e(TAG, "ScreenShotService");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "OnCreate Start");

        initWindow();
        initHandler();
        createImageReader();
        initMediaProjection();
        startScreenShot();
        handler.postDelayed(runnable, 5000);


    }

    private WindowManager windowManager =null;
    private ImageView igv = null;
    private WindowManager.LayoutParams params;

    private int screenWidth,screenHeight,screenDensity;

    public void initWindow(){

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //取得螢幕的各項參數
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.densityDpi;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;


        Log.d(TAG,"density:"+screenDensity+", width:" + screenWidth + ", height:" + screenHeight);

        igv = new ImageView(this);
        igv.setImageResource(R.mipmap.ic_launcher);


//        int layout_parms;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//
//        {
//            layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//
//        }
//
//        else {
//
//            layout_parms = WindowManager.LayoutParams.TYPE_PHONE;
//
//        }

//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.x = 0;
//        params.y = screenHeight/4;


//        igv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startScreenShot();
//            }
//        });
//        windowManager.addView(igv, params);
    }

    public void initHandler(){
        handler = new Handler();
    }

    //建立imageReader
    public void createImageReader() {
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
    }

    public void initMediaProjection(){

        //透過MediaProjectionManager取得MediaProjection
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, resultIntent);

        //呼叫mediaProjection.createVirtualDisplay()
        virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror",
                screenWidth, screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, handler);

    }

    public static void setResultIntent(Intent it){
        resultIntent = it;
    }

    public void startScreenShot(){

        igv.setVisibility(View.GONE);
        Log.e(TAG, "startScreenShot called");
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                startCapture();
//            }
//        },5000);

          runnable = new Runnable() {
            @Override
            public void run() {
                startCapture();
                //延时1秒post
                handler.postDelayed(this, 5000);
            }
        };
    }


    private void startCapture() {

        //呼叫image.acquireLatestImage()，取得image
        Image image = imageReader.acquireLatestImage();
        new SaveTask().execute(image);

    }


    public class SaveTask extends AsyncTask<Image, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return false;
            }

            Boolean success = false;


            Image image = params[0];
            //處理影像並儲存到手機
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();

            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * screenWidth;
            Bitmap bitmap = Bitmap.createBitmap(screenWidth + rowPadding / pixelStride, screenHeight, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight);
            image.close();
            File fileImage;
            if (bitmap != null) {
                try {
                    fileImage = getScreenShotsFile();
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        success = true;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    bitmap.recycle();
                }
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);

            if (bool) {
                Toast.makeText(getApplicationContext(),"Got it",Toast.LENGTH_SHORT).show();
            }
            else{
                startCapture();
            }
            igv.setVisibility(View.VISIBLE);

        }
    }

    public static File getMainDirectoryName() {
        //Here we will use getExternalFilesDir and inside that we will make our Demo folder
        //benefit of getExternalFilesDir is that whenever the app uninstalls the images will get deleted automatically.
        File mainDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Demo");

        Log.e(TAG, "Demo File is presented at " + mainDir );
//        mainDir.mkdirs();
//        //If File is not present create directory
        if (!mainDir.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return mainDir;
    }

    public File getScreenShotsFile(){
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        String str = formatter.format(curDate);
        fileName = str.replaceAll("\\D+", "");
        File file = new File(saveFile.getAbsolutePath(), fileName+".jpg");
        return file;
//        String screenShot = FileUtils.getDiskCacheDir(getApplicationContext()) + File.separator + "ScreenShot";
//        if(!FileUtils.isFileExists(screenShot)){
//            FileUtils.creatSDDir(screenShot);
//        }
//        String tmp = screenShot + File.separator + System.currentTimeMillis() + ".jpg";
//
//        if(!FileUtils.isFileExists(tmp)){
//            FileUtils.createSDFile(tmp);
//        }
//        Log.i("path",tmp);
//
//        return new File(tmp);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (igv != null) windowManager.removeView(igv);
//
//        if (virtualDisplay != null) {
//            virtualDisplay.release();
//            virtualDisplay = null;
//        }
//
//        if (mediaProjection != null) {
//            mediaProjection.stop();
//            mediaProjection = null;
//        }

    }
}
