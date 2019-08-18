package com.camera.api;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";
    int noOfCameras;

    HashSet<String> FCR;
    HashSet<String> BCR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        BCR = new HashSet<>();
        FCR = new HashSet<>();
        noOfCameras = Camera.getNumberOfCameras();
        Log.d(TAG, "onCreate: noOfCameras : " +noOfCameras);


        getcameraresolution();


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



    public void Front(View view) {


        int temp =0;
        final String[] resolution = new String[FCR.size()];
        for(String s : FCR){
            resolution[temp] = s;
            temp = temp+1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);

        builder.setTitle("Set Resolutioon");

        builder.setItems(resolution, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(BasicActivity.this,MainActivity.class)
                        .putExtra("BackCamera",false)
                .putExtra("Resolution",resolution[i]));
                Toast.makeText(BasicActivity.this, resolution[i], Toast.LENGTH_SHORT).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);

        builder.setTitle("Set Resolutioon");

        builder.setItems(resolution, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(BasicActivity.this,MainActivity.class)
                        .putExtra("BackCamera",true)
                        .putExtra("Resolution",resolution[i]));
                Toast.makeText(BasicActivity.this, resolution[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();



    }
}


