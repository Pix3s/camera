package com.novikovs.camera.interfaces;

import android.view.View;

/**
 * Created by VladislavNovikov on 07.09.17.
 */

public interface OnItemClickListener {
    void onClickPhoto(View view, int position);
    void onLongClickPhoto(View view, int position);
}
