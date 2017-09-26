package com.novikovs.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;

/**
 * Created by VladislavNovikov on 06.09.17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private Context context;

    public CameraPreview(Context context, Camera camera) { //1
        super(context);
        this.context = context;
        this.camera = camera;
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this); //3

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //4
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { //5
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters(); //6
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            Camera.Size size = sizes.get(0);
            for(int i=0;i<sizes.size();i++) {
                if(sizes.get(i).width > size.width)
                    size = sizes.get(i);
            }
            parameters.setPictureSize(size.width, size.height);

            camera.setParameters(parameters);
            camera.startPreview();
        }
        catch (IOException e) {
            Toast.makeText(context, "Camera preview failed", Toast.LENGTH_LONG).show();
        }
    }

    // 6
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null)
            return;

        camera.stopPreview();

        setCameraDisplayOrientation();

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e) {
            Toast.makeText(context, "Camera preview failed", Toast.LENGTH_LONG).show();
        }
    }

    public int setCameraDisplayOrientation() {
        if (camera == null)
            return 0;

        Camera.CameraInfo info = new Camera.CameraInfo(); // 1
        Camera.getCameraInfo(0, info);

        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = winManager.getDefaultDisplay().getRotation(); //2

        int degrees = 0;

        switch (rotation) { //3
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result; //4
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }
        else {
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result); //5

        Camera.Parameters parameters = camera.getParameters(); //6
        int rotate = (degrees + 270) % 360;
        parameters.setRotation(rotate);

        camera.setParameters(parameters);
        return result;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

