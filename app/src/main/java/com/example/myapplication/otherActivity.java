package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class
otherActivity extends AppCompatActivity {


    private TextView aboutTv;
    private TextView LinkTv;
    private TextView usernameTv;
    private ImageView profileView;
    private DatabaseReference UserRef,myGalleryRef;
    private FirebaseAuth mAuth;
    private int count=5;
    private ImageView post;

    RecyclerView recyclerView;

    FirebaseRecyclerOptions<galleryVariables> options;
    FirebaseRecyclerAdapter<galleryVariables,gallery_viewHolder> adapter;


    private String currentUserId;

    private  String username,about,link,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

            Intent intent=getIntent();
            String visit_UserId=intent.getExtras().getString("visit_UserId");



            aboutTv=(TextView)findViewById(R.id.abouttv);
            LinkTv=(TextView)findViewById(R.id.linktv);
            usernameTv=(TextView) findViewById((R.id.usernametv));
            profileView=(ImageView)findViewById((R.id.profile));
            //post=(ImageView)findViewById(R.id.postImgView);

            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

            mAuth=FirebaseAuth.getInstance();
            currentUserId=mAuth.getCurrentUser().getUid();

            UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
            myGalleryRef=FirebaseDatabase.getInstance().getReference().child("MyGallery").child(visit_UserId);

            options=new FirebaseRecyclerOptions.Builder<galleryVariables>().setQuery(myGalleryRef,galleryVariables.class).build();
            adapter=new FirebaseRecyclerAdapter<galleryVariables, gallery_viewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final gallery_viewHolder holder, int position, @NonNull final galleryVariables model) {

                    RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.ic_launcher_background);

                    Glide.with(otherActivity.this).load(model.getImageLink()).apply(requestOptions).into(holder.image);

                    //holder.text.setText(model.getDescription());
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(otherActivity.this,other_specificGallery.class);//check this line
                            intent.putExtra("imageView",model.getImageLink());
                            intent.putExtra("text",model.getDescription());
                            startActivity(intent);
                        }
                    });

                }

                @NonNull
                @Override
                public gallery_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_gallery,parent,false);
                    return new gallery_viewHolder(view);
                }
            };


            UserRef.child(visit_UserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        username=dataSnapshot.child("user").getValue().toString();
                        about=dataSnapshot.child("about").getValue().toString();
                        link=dataSnapshot.child("link").getValue().toString();

                        aboutTv.setText(about);
                        LinkTv.setText(link);
                        usernameTv.setText(username);

                        if(dataSnapshot.hasChild("profileimage"))
                        {
                            image = dataSnapshot.child("profileimage").getValue().toString();

                            Picasso.get().load(image).placeholder(R.drawable.ic_add_a_photo_black_24dp).into(profileView);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
            LinkTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(otherActivity.this,webView.class);
                    intent.putExtra("link",link);
                    startActivity(intent);
                }
            });


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                profileView.setClipToOutline(true);
            }
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),3);
            //GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            adapter.startListening();
            recyclerView.setAdapter(adapter);


        }





        @Override
        public void onBackPressed() {
            Intent intent= new Intent(otherActivity.this,SecondActivity.class);
            startActivity(intent);
        }









}

