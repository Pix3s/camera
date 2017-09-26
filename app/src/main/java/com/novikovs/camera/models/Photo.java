package com.novikovs.camera.models;

import java.io.File;

/**
 * Created by VladislavNovikov on 06.09.17.
 */

public class Photo {

    private int id;
    private File file;

    public Photo(File file) {
        this.file = file;
    }

    public Photo(int id, File file) {
        this.id = id;
        this.file = file;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }
}
