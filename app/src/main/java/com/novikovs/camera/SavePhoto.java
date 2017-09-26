package com.novikovs.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

public class SavePhoto extends AsyncTask<byte[], Void, Boolean> {

    private Context context;
    private int orientation;

    public SavePhoto(Context context, int orientation) {
        this.context = context;
        this.orientation = orientation;
    }



    @Override
    protected Boolean doInBackground(byte[]... params) {
        Uri pictureFile = generateFile();
        try {
            savePhotoInFile(params[0], pictureFile);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    private Uri generateFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File(Environment.getExternalStorageDirectory(), "CameraTest");
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return Uri.fromFile(newFile);
    }

    private void savePhotoInFile(byte[] data, Uri pictureFile) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        OutputStream os = context.getContentResolver().openOutputStream(pictureFile);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Matrix matrix = new Matrix();

            matrix.postRotate(180);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        }
        os.close();
    }


    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);

    }
}
