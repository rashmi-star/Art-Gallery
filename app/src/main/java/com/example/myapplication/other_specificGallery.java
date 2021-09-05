package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class other_specificGallery extends AppCompatActivity {

    private ImageView imageView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_specific_gallery);

        /*mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        galleryRef= FirebaseDatabase.getInstance().getReference();
        //key=galleryRef.getKey();*/


        Intent intent=getIntent();
        final String image=intent.getExtras().getString("imageView");
        final String des=intent.getExtras().getString("text");


        imageView=(ImageView)findViewById(R.id.specificImg);
        text=(TextView)findViewById(R.id.description);




        Glide.with(this).load(image).into(imageView);
        text.setText(des);











    }
}
