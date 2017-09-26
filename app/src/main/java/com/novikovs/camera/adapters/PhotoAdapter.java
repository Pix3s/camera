package com.novikovs.camera.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.novikovs.camera.interfaces.OnItemClickListener;
import com.novikovs.camera.models.Photo;
import com.novikovs.camera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by VladislavNovikov on 06.09.17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Photo> photos;
    private ArrayList<Integer> delPhotos;
    private OnItemClickListener onItemClickListener;

    public PhotoAdapter(Context context, ArrayList<Photo> photos, ArrayList<Integer> delPhotos, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.photos = photos;
        this.delPhotos = delPhotos;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setImgPhoto(photos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imgPhoto;
        private ImageView imgPhotoHighlight;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.photo);
            imgPhotoHighlight = (ImageView) itemView.findViewById(R.id.photo_highlight);
            imgPhotoHighlight.setOnClickListener(this);
            imgPhotoHighlight.setOnLongClickListener(this);
        }

        public void setImgPhoto(Photo photo, int position) {
            imgPhotoHighlight.setAlpha(delPhotos.contains(position)? 0.5f : 0);
            Picasso.with(context)
                    .load(photo.getFile())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_photo)
                    .centerInside()
                    .resize(400, 400)
                    .into(imgPhoto);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClickPhoto(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onItemClickListener.onLongClickPhoto(v, getAdapterPosition());
            return true;
        }
    }
}
