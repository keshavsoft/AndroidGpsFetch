package com.example.gpsfetch.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsfetch.R;
import com.example.gpsfetch.data.entity.ImageEntity;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    private List<ImageEntity> imageList;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageEntity image = imageList.get(position);

        holder.txtLocation.setText("Lat: " + image.getLatitude() + ", Long: " + image.getLongitude());
        holder.txtImagePath.setText(image.getImagePath());

        // Decode Image
        Bitmap bitmap = BitmapFactory.decodeFile(image.getImagePath());
        if (bitmap != null) {
            holder.imageViewPhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView txtLocation, txtImagePath;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtImagePath = itemView.findViewById(R.id.txtImagePath);
        }
    }
}
