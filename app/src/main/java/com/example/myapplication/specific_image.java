package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class specific_image extends AppCompatActivity {

    private ImageView imageViewWidget;
    private TextView des_Tv,username;

    private String senderUserID,receiver_UserID,currentUserId;

    private Button deleteBtN;
    FirebaseAuth mAuth;
   private DatabaseReference PostsRef,UserRef;
   DatabaseReference galleryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_image);



        Intent intent=getIntent();
        String image=intent.getExtras().getString("image");
        String des=intent.getExtras().getString("des");
        final String user=intent.getExtras().getString("username");
        final String visit_UserId=intent.getExtras().getString("visit_UserId");


        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        PostsRef=FirebaseDatabase.getInstance().getReference().child("Posts");

        username=(TextView)findViewById(R.id.userTv);
        imageViewWidget=(ImageView)findViewById(R.id.imageViewWidget);
        des_Tv=(TextView)findViewById(R.id.desTv);
        deleteBtN=(Button)findViewById(R.id.Dbutton);


        des_Tv.setText(des);
        username.setText(user);

        if(currentUserId.equals(visit_UserId))
        {
            deleteBtN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostsRef.child(currentUserId).removeValue();
                    Intent intent1=new Intent(specific_image.this,SecondActivity.class);
                    startActivity(intent1);
                }
            });

        }
        else
        {
            deleteBtN.setVisibility(View.GONE);

        }

        Glide.with(specific_image.this).load(image).into(imageViewWidget);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               //String visit_user_id=getRef()

                Intent intent1=new Intent(specific_image.this,otherActivity.class);
                intent1.putExtra("visit_UserId",visit_UserId);
                startActivity(intent1);

            }
        });

    }
}
