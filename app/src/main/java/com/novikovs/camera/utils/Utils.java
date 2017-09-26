package com.novikovs.camera.utils;

import android.os.Environment;

import com.novikovs.camera.models.Photo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by VladislavNovikov on 07.09.17.
 */

public class Utils {

    public static ArrayList<Photo> getAllPhoto() {
        ArrayList<Photo> photos = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory(), "CameraTest");
        for (final File file : folder.listFiles()) {
            photos.add(new Photo(file));
        }
        return photos;
    }

    public static File getLastPhoto() {
        ArrayList<Photo> photos = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory(), "CameraTest");
        File[] files = folder.listFiles();
        return files[files.length-1];
    }

    public static void deletePhotoInFile(ArrayList<Photo> photos) {
        File folder = new File(Environment.getExternalStorageDirectory(), "CameraTest");
        for (File file : folder.listFiles()) {
            if (!photos.contains(new Photo(file))) {
                file.delete();
            }
        }
    }
}
