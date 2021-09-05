package com.example.myapplication;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

     public TextView t1;
     public ImageView i1;

    public ViewHolder(@NonNull View itemView ) {
        super(itemView);

    i1=(ImageView)itemView.findViewById(R.id.imageViewWidget);
    t1=(TextView)itemView.findViewById(R.id.nameWidget);


    }




}
