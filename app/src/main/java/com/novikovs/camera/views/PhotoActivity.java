package com.novikovs.camera.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.novikovs.camera.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Жди");
        progressDialog.show();

        String pathPhotoFile = getIntent().getExtras().getString("path");
        ImageView imgFullPhoto = (ImageView) findViewById(R.id.img_full_photo);

        if (pathPhotoFile != null)
            Picasso.with(PhotoActivity.this)
                    .load(new File(pathPhotoFile))
                    .resize(1000,1000)
                    .into(imgFullPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressDialog.hide();
                        }

                        @Override
                        public void onError() {

                        }
                    });
    }
}



