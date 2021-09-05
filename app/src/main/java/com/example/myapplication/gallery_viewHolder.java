package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class gallery_viewHolder extends RecyclerView.ViewHolder {

    public TextView text;
    public ImageView image;


    public gallery_viewHolder(@NonNull View itemView) {
        super(itemView);

       // text=(TextView)itemView.findViewById(R.id.imgTv);
        image=(ImageView)itemView.findViewById(R.id.imgView);



    }
}
