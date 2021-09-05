package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.HashMap;

public class postgallery extends AppCompatActivity {

    static final int Gallery_pick=1 ;

    DatabaseReference myGalleryRef,newRef,userRef;
    String currentUserId;



    StorageReference myGalleyPost;

    FirebaseAuth mAuth;

    public String description,username;

    Button postBtn;
    ImageView imgView;
    EditText des;

    Uri ImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postgallery);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        myGalleryRef = FirebaseDatabase.getInstance().getReference().child("MyGallery").child(currentUserId);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        myGalleyPost = FirebaseStorage.getInstance().getReference();

        postBtn = (Button) findViewById(R.id.postbtn);
        imgView = (ImageView) findViewById(R.id.galleryImg);
        des=(EditText)findViewById(R.id.desEv);

       // RetriveUsername();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_gallery();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }

        });



    }

    /*private void RetriveUsername()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                username=dataSnapshot.child("user").getValue().toString();


                HashMap usermap=new HashMap();
                usermap.put("username",username);
                myGalleryRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(postgallery.this,"username also added",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void ValidatePostInfo () {

        description = des.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(postgallery.this, "image not selected", Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()) {
            Toast.makeText(postgallery.this, "is it okay with no description", Toast.LENGTH_SHORT).show();
        } else {
            StoringImageToFirebaseStorage();

        }
    }


    private void StoringImageToFirebaseStorage()
    {

        final StorageReference filepath=myGalleyPost.child("Post Images").child(ImageUri.getLastPathSegment()+".jpg");

        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                         final String downloadUrl =
                                uri.toString();




                        HashMap postMap= new HashMap();
                        postMap.put("description",description);
                        postMap.put("imageLink",downloadUrl);
                        myGalleryRef.push().updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(postgallery.this,"updated user info database",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        Intent intent=new Intent(postgallery.this,my_gallery.class);
                        startActivity(intent);


                        /*myGalleryRef.child("imageLink").push().setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(postgallery.this, "Image stored", Toast.LENGTH_SHORT).show();
                                } else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(postgallery.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/




                    }
                });
            }


        });
        //addDataToDatabase();
    }

    /*private void addDataToDatabase()
    {}*/


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
            imgView.setImageURI(ImageUri);
        }
    }

}
