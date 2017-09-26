package com.novikovs.camera.views;

import android.animation.IntArrayEvaluator;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.novikovs.camera.CameraPreview;
import com.novikovs.camera.R;
import com.novikovs.camera.SavePhoto;
import com.novikovs.camera.utils.Utils;
import com.squareup.picasso.Picasso;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener, Camera.AutoFocusCallback, Camera.PreviewCallback, Camera.PictureCallback, Animation.AnimationListener {

    private ImageView shutterView;
    private ImageView previewHistory;
    private ImageButton btnChangeCameraFunc;
    private Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        shutterView = (ImageView) findViewById(R.id.shutter_view);
        previewHistory = (ImageView) findViewById(R.id.preview_history);
        btnChangeCameraFunc = (ImageButton) findViewById(R.id.change_camera_func);

        Picasso.with(this).load(Utils.getLastPhoto()).resize(100,100).centerCrop().into(previewHistory);
        ImageButton button = (ImageButton) findViewById(R.id.btn_take_photo);

        button.setOnClickListener(this);
        btnChangeCameraFunc.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open(0);
        CameraPreview preview = new CameraPreview(this, camera);

        FrameLayout framePreview = (FrameLayout) findViewById(R.id.preview);
        framePreview.setOnClickListener(this);
        framePreview.addView(preview, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                startAnimationShutter();
                camera.takePicture(null, null, null, this);
                break;
            case R.id.preview:
                onBackPressed();
                break;
        }

    }

    private void startAnimationShutter() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shutter_camera);
        animation.setAnimationListener(this);
        shutterView.startAnimation(animation);
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {
        new SavePhoto(this, getResources().getConfiguration().orientation).execute(data);
        Picasso.with(this).load(Utils.getLastPhoto()).resize(100,100).centerCrop().into(previewHistory);
        camera.startPreview();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        shutterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        shutterView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        Toast.makeText(this, success ? "AutoFocus success" : "FUCK", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) stopCamera();
    }

    private void stopCamera() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}