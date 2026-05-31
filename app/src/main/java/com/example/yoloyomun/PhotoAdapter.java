package com.example.yoloyomun;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Binds saved photo files into the archive gallery grid. */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    public interface OnPhotoClickListener {
        void onPhotoClick(File photo);
    }

    private final List<File> photos = new ArrayList<>();
    private final OnPhotoClickListener listener;

    public PhotoAdapter(OnPhotoClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<File> newPhotos) {
        photos.clear();
        photos.addAll(newPhotos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_archive_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        File photo = photos.get(position);

        Bitmap thumb = ImageUtils.decodeScaled(photo.getAbsolutePath(), 300, 300);
        if (thumb != null) {
            holder.thumb.setImageBitmap(thumb);
        } else {
            holder.thumb.setImageResource(R.drawable.ic_photo_placeholder);
        }

        // Accessibility: each cell is announced as "Saved photo N".
        holder.thumb.setContentDescription(
                holder.itemView.getContext().getString(R.string.archive_photo_desc, position + 1));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        final SquareImageView thumb;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.photo_thumb);
        }
    }
}
