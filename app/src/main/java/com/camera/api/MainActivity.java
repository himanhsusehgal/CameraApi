package com.camera.api;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";
    int noOfCameras;

    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    LinkedHashSet<String> FCR;
    LinkedHashSet<String> BCR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        BCR = new LinkedHashSet<>();
        FCR = new LinkedHashSet<>();
        noOfCameras = Camera.getNumberOfCameras();
        Log.d(TAG, "onCreate: noOfCameras : " +noOfCameras);
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            getcameraresolution();
        } else {
            requestCameraPermission();
        }




    }
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };


    }
    public void getcameraresolution(){

        for(int i = 0 ; i < noOfCameras ; i++){

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            // For BACK camera

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Log.d(TAG, "getcameraresolution: Front Camera Details");

                Camera camera = Camera.open(i);
                Camera.Parameters cameraParams = camera.getParameters();

                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {


                    BCR.add(String.valueOf(cameraParams.getSupportedPictureSizes().get(j).width)+"   X   "+String.valueOf(cameraParams.getSupportedPictureSizes().get(j).height));

                    Log.d(TAG, "getcameraresolution: ---------------------------------------------------------");
                }
                camera.release();

            }
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                Log.d(TAG, "getcameraresolution: Front Camera Details");

                Camera camera = Camera.open(i);
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {

                    FCR.add(String.valueOf(cameraParams.getSupportedPictureSizes().get(j).width)+"   X   "+String.valueOf(cameraParams.getSupportedPictureSizes().get(j).height));

                    Log.d(TAG, "getcameraresolution: ---------------------------------------------------------");
                }
                camera.release();

            }



        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            getcameraresolution();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage("No Permissiom")
                .setPositiveButton("ok", listener)
                .show();
    }


    public void Front(View view) {


        int temp =0;
        final String[] resolution = new String[FCR.size()];
        for(String s : FCR){
            resolution[temp] = s;
            temp = temp+1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Set Resolutioon");

        builder.setItems(resolution, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class)
                        .putExtra("BackCamera",false)
                .putExtra("Resolution",resolution[i]));
                Toast.makeText(MainActivity.this, resolution[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();



    }

    public void Back(View view) {


        int temp =0;
        final String[] resolution = new String[FCR.size()];
        for(String s : FCR){
            resolution[temp] = s;
            temp = temp+1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Set Resolutioon");

        builder.setItems(resolution, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class)
                        .putExtra("BackCamera",true)
                        .putExtra("Resolution",resolution[i]));
                Toast.makeText(MainActivity.this, resolution[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();



    }
}


