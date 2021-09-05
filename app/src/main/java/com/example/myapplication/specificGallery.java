package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class specificGallery extends AppCompatActivity {

    private ImageView imageView;
    private TextView text;
    private Button deleteBtn;

    DatabaseReference galleryRef;
   FirebaseAuth mAuth;
    private String currentUserId,key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_gallery);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        galleryRef=FirebaseDatabase.getInstance().getReference();
        //key=galleryRef.getKey();


        Intent intent=getIntent();
        final String image=intent.getExtras().getString("imageView");
        final String des=intent.getExtras().getString("text");

        imageView=(ImageView)findViewById(R.id.specificImg);
        text=(TextView)findViewById(R.id.description);

        deleteBtn=(Button)findViewById(R.id.dltePost);



        Glide.with(this).load(image).into(imageView);
        text.setText(des);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query query=galleryRef.child("MyGallery").child(currentUserId).orderByChild("imageLink").equalTo(image);
                Query desQuery=galleryRef.child("MyGallery").child(currentUserId).orderByChild("description").equalTo(des);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot image:dataSnapshot.getChildren())
                        {
                            image.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                desQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot des:dataSnapshot.getChildren())
                        {
                            des.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



              //galleryRef.child("MyGallery").child(currentUserId).child(key).child("imageLink").removeValue();
              //galleryRef.child("MyGallery").child(currentUserId).child(key).child("description").removeValue();



                Toast.makeText(specificGallery.this,"deleted",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(specificGallery.this,my_gallery.class);
                startActivity(intent);

            }
        });




    }
}
