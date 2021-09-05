package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private ImageButton post;
    //private ImageButton tuitorial,ReadyBrushes;
    private ImageView profilepic;

    private DatabaseReference UsersRef;
    private FirebaseAuth mAuth;

    private String currentUserId;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    private  static final String TAG="ALL USER";
    private static final int NUM_COLUMNS=2;

    RecyclerView recyclerView;
    DatabaseReference Postsref;


    FirebaseRecyclerOptions<variables> options;
    FirebaseRecyclerAdapter<variables,ViewHolder> adapter;


    public static final String mypref="nightModePrefs";
    public static final String key_isnight="isNightMode";

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        sharedPreferences= getSharedPreferences(mypref, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(key_isnight, false)) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else{

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }



        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        //tuitorial=(ImageButton)findViewById(R.id.tuitorial_imgButton);
        //ReadyBrushes=(ImageButton)findViewById(R.id.ready_Imgbtn);
        post = (ImageButton) findViewById(R.id.postpicbtn);
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();


        Postsref= FirebaseDatabase.getInstance().getReference().child("Posts");
        recyclerView=(RecyclerView)findViewById(R.id.rvMain);




        //this.mContext=mContext;

        options=new FirebaseRecyclerOptions.Builder<variables>()
                .setQuery(Postsref,variables.class).build();
        adapter=new FirebaseRecyclerAdapter<variables, ViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final variables model)
            {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background);//very very important

               /*Picasso.get().load(model.getImageLink()).into(holder.i1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
                    }
                });*/
                Glide.with(SecondActivity.this)
                        .load(model.getImageLink())
                        .apply(requestOptions)
                        .into(holder.i1);


                holder.t1.setText(model.getNames());
                holder.i1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String visit_UserId=getRef(position).getKey();//the most important

                        Intent intent = new Intent(SecondActivity.this, specific_image.class);
                        intent.putExtra("image",model.getImageLink());
                        intent.putExtra("des",model.getNames());
                        intent.putExtra("username",model.getUsername());
                        intent.putExtra("visit_UserId",visit_UserId);
                        startActivity(intent);


                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_rv, parent, false);
                return new ViewHolder(view);
            }

        };

        //GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);



        //initImageBitmaps();
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        View nav = nav_view.inflateHeaderView(R.layout.navi_header);

        profilepic=(ImageView)nav.findViewById(R.id.profile_pic_nav);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                profilepic.setClipToOutline(true);
            }
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("profileimage"))
                {
                    String image=dataSnapshot.child("profileimage").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.ic_add_a_photo_black_24dp).into(profilepic);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {

                    case R.id.profile: {
                        Intent intent=new Intent(SecondActivity.this,my_gallery.class);
                        startActivity(intent);
                        break;

                    }
                    case R.id.settings: {
                        //post.setVisibility(View.GONE);
                        /*Intent intent=new Intent(SecondActivity.this,setting.class);
                        startActivity(intent);*/
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.dl, new SettingsFragment());
                        fragmentTransaction.addToBackStack(null).commit();
                        Toast.makeText(SecondActivity.this, "SETTINGS", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.positivity: {

                        Intent intent = new Intent(SecondActivity.this, positivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.paint: {

                        Intent intent = new Intent(SecondActivity.this, games.class);
                        startActivity(intent);
                        break;

                    }
                    case R.id.logout: {

                        mauth = FirebaseAuth.getInstance();

                        mauth.signOut();
                        finish();
                        startActivity(new Intent(SecondActivity.this, MainActivity.class));

                        break;
                    }
                }
                switch (id) {

                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.postpicbtn:
                Intent intent=new Intent(SecondActivity.this,postActivity.class);
                startActivity(intent);
        }
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
           // post.setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();
    }

}
