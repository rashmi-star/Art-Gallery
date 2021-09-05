package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.android.gms.auth.api.signin.internal.Storage;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Edit_Activity extends AppCompatActivity {

    private static final int Gallery_pick=1;

    private EditText username;
    private EditText about;
    private EditText link;
    private Button save;
    private ImageView profile;
    private TextView temp;

    private Uri ImageUri;

    private StorageReference UserProfileImageRef;

    String currentUserId;

    FirebaseAuth mAuth;

    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);

        username = (EditText) findViewById(R.id.username_et);
        about = (EditText) findViewById(R.id.about_et);
        link = (EditText) findViewById(R.id.link_et);



        save = (Button) findViewById(R.id.save);
        profile = (ImageView) findViewById(R.id.profile);

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);



        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                profileval();
                /*{

                    Intent intent=new Intent(Edit_Activity.this,SecondActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(Edit_Activity.this,Edit_Activity.class);
                    startActivity(intent);
                }*/


            }
           /* Animation animation=new AlphaAnimation(1.0f,0.0f);
            animation.setDuration(1000);
            save.startAnimation(animation);*/
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Open_gallery();


            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String username1=dataSnapshot.child("user").getValue().toString();
                    String about1=dataSnapshot.child("about").getValue().toString();
                    String link1=dataSnapshot.child("link").getValue().toString();

                    username.setText(username1);
                    about.setText(about1);
                    link.setText(link1);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("profileimage"))
                {
                    String image=dataSnapshot.child("profileimage").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.ic_add_a_photo_black_24dp).into(profile);
                }

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
            profile.setImageURI(ImageUri);

        }
    }

    private void SaveAccountDetails()
    {
        String UserName=username.getText().toString();
        String About=about.getText().toString();
        String Link=link.getText().toString();


            HashMap usermap = new HashMap();
            usermap.put("user",UserName);
            usermap.put("about",About);
            usermap.put("link",Link);
            ref.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToArtGallery();
                        Toast.makeText(Edit_Activity.this,"updated user info database",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String message=task.getException().getMessage();
                        Toast.makeText(Edit_Activity.this,"Error occured"+message,Toast.LENGTH_LONG).show();
                    }

                }
            });

       /* List<String> mylist=new ArrayList<String>();
        mylist.add("user");
        mylist.add("hi");

        //Set<String> keySet=usermap.keySet();

       // for()


        List<String> subset = new ArrayList<>();
        Iterator<String> iterator = mylist.iterator();
        while (iterator.hasNext()) {
            String val=iterator.next();
            if (usermap.containsKey(mylist)) {
                subset.add(val);
            }
        }*/




        /*Boolean b;
        b=Collections.disjoint(mylist,keySet);
        if(b)
        {
            temp.setText("no");
        }
        else
        {
            temp.setText("Yes");
        }


        boolean a;
        a=usermap.keySet().containsAll(mylist);*/

    }

    private void SendUserToArtGallery()
    {
        Intent intent=new Intent(Edit_Activity.this,my_gallery.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void StoringImageToFirebaseStorage()
    {

        final StorageReference filepath=UserProfileImageRef.child(currentUserId + ".jpg");

        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl =
                                uri.toString();

                        ref.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Edit_Activity.this, "Image stored", Toast.LENGTH_SHORT).show();
                                } else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(Edit_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




                    }
                });
            }


        });
        SaveAccountDetails();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent= new Intent(Edit_Activity.this,my_gallery.class);
        startActivity(intent);
    }

    private void profileval(){

        String UserName=username.getText().toString();
        String About=about.getText().toString();
        String Link=link.getText().toString();


        if(UserName.isEmpty() || About.isEmpty() || Link.isEmpty() )
        {
            Toast.makeText(this,"please enter all the details",Toast.LENGTH_SHORT).show();

        }else{
            StoringImageToFirebaseStorage();
        }

    }
}
