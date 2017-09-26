package com.novikovs.camera.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.novikovs.camera.R;
import com.novikovs.camera.adapters.PhotoAdapter;
import com.novikovs.camera.interfaces.OnItemClickListener;
import com.novikovs.camera.models.Photo;
import com.novikovs.camera.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.novikovs.camera.utils.Constants.DELETE_PHOTO;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private static final int LAYOUT = R.layout.activity_main;
    private static final int MODE_SIMPLE = 0;
    private static final int MODE_HIGHLIGHT = 1;

    private int MODE = MODE_SIMPLE;

    private ArrayList<Photo> photos;
    private PhotoAdapter photoAdapter;

    private ArrayList<Integer> delPhotos;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photos = new ArrayList<>(Utils.getAllPhoto());
        delPhotos = new ArrayList<>();

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.add(0, DELETE_PHOTO, 0, "Удалить")
                .setIcon(R.drawable.ic_delete)
                .setVisible(false)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        photoAdapter = new PhotoAdapter(this, photos, delPhotos, this);

        RecyclerView recyclerPhoto = (RecyclerView) findViewById(R.id.recycler_photo);
        recyclerPhoto.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerPhoto.setAdapter(photoAdapter);
    }

    private Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_PHOTO:
                Collections.sort(delPhotos, comparator);
                for (Integer integer : delPhotos) {
                    photos.remove(integer);
                }
                Utils.deletePhotoInFile(photos);
                delPhotos.clear();
                photoAdapter.notifyDataSetChanged();
                MODE = MODE_SIMPLE;
                menu.getItem(0).setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickPhoto(View view, int position) {
        switch (MODE) {
            case MODE_HIGHLIGHT:
                onClickInModeHighlight(view, position);
                if (delPhotos.size() == 0) {
                    onLongClickPhoto(view, position);
                }
                break;
            case MODE_SIMPLE:
                Intent intent = new Intent(this, PhotoActivity.class);
                intent.putExtra("path", photos.get(position).getFile().getAbsolutePath());
                startActivity(intent);
        }
    }

    @Override
    public void onLongClickPhoto(View view, int position) {
        MODE = (MODE == MODE_HIGHLIGHT) ? MODE_SIMPLE : MODE_HIGHLIGHT;
        if (MODE == MODE_HIGHLIGHT) {
            menu.getItem(DELETE_PHOTO).setVisible(true);
            onClickInModeHighlight(view, position);
        } else {
            menu.getItem(DELETE_PHOTO).setVisible(false);
        }
    }

    private void onClickInModeHighlight(View view, int position) {
        if (view.getAlpha() != 0) {
            view.setAlpha(0);
            delPhotos.remove(Integer.valueOf(position));
        } else {
            view.setAlpha(0.5f);
            delPhotos.add(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (MODE == MODE_HIGHLIGHT)
            delPhotos.clear();
        photoAdapter.notifyDataSetChanged();
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }
}
