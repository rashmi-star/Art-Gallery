package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class postActivity extends AppCompatActivity {

    private static final int Gallery_pick=1;
    private ImageButton post_pic;
    private EditText description;
    private Button post_btn;

    private Uri ImageUri;
    private String des,TAG;

    private StorageReference PostsImagesRefrence;

    private String saveCurrentDate,saveCurrentTime;
    private String  downloadUrl,current_User_id;
    private DatabaseReference UsersRef,PostsRef;
    private FirebaseAuth mAuth;

    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mAuth = FirebaseAuth.getInstance();
        current_User_id = mAuth.getCurrentUser().getUid();


        PostsImagesRefrence = FirebaseStorage.getInstance().getReference();


        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(current_User_id);
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(current_User_id);



        post_pic=(ImageButton)findViewById(R.id.postpic);
        description=(EditText)findViewById(R.id.pic_description);
        post_btn=(Button) findViewById(R.id.postpicbtn);



        post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_gallery();
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }

        });
    }


        private void ValidatePostInfo () {

            des = description.getText().toString();


            if (ImageUri == null) {
                Toast.makeText(postActivity.this, "image not selected", Toast.LENGTH_SHORT).show();
            } else if (des.isEmpty()) {
                Toast.makeText(postActivity.this, "is it okay with no description", Toast.LENGTH_SHORT).show();
            } else {
                StoringImageToFirebaseStorage();

            }
        }


    private void StoringImageToFirebaseStorage()
    {

        final StorageReference filepath=PostsImagesRefrence.child("Post Images").child(ImageUri.getLastPathSegment()+".jpg");

        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl =
                                uri.toString();


                        PostsRef.child("imageLink").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(postActivity.this, "Image stored", Toast.LENGTH_SHORT).show();
                                } else
                                    {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(postActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




                    }
                });
            }


        });
        Retrivingdatafromdatabase();
    }

    private void Retrivingdatafromdatabase()
    {
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String username=dataSnapshot.child("user").getValue().toString();


                HashMap postMap= new HashMap();
                postMap.put("names",des);
                postMap.put("username",username);
                PostsRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(postActivity.this,"updated user info database",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void Open_gallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            post_pic.setImageURI(ImageUri);
        }
    }

}










