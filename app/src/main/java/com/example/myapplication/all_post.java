package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class all_post extends AppCompatActivity {

    private  static final String TAG="ALL USER";
    private static final int NUM_COLUMNS=2;

    RecyclerView recyclerView;
    DatabaseReference Postsref;


    FirebaseRecyclerOptions<variables> options;
    FirebaseRecyclerAdapter<variables,ViewHolder> adapter;


    private Context mContext;

   //private ArrayList<String> mImageUrls=new ArrayList<>();
   //private ArrayList<String> mNames=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post);




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
               Glide.with(all_post.this)
                        .load(model.getImageLink())
                        .apply(requestOptions)
                        .into(holder.i1);


               holder.t1.setText(model.getNames());
               holder.i1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String visit_UserId=getRef(position).getKey();//the most important

                        Intent intent = new Intent(all_post.this, specific_image.class);
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


    }


    /*private void initImageBitmaps(){
        mImageUrls.add("https://i.ytimg.com/vi/7WCbIjqjHM4/maxresdefault.jpg");
        mNames.add("hello");



        mImageUrls.add("https://res.cloudinary.com/demo/image/upload/q_90/happy_dog.jpg");
        mNames.add("hello");

        mImageUrls.add("https://blog.liebherr.com/appliances/my/wp-content/uploads/sites/8/2016/10/Deepavali_1_Panther.jpg");
        mNames.add("hello");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/logindemo-98840.appspot.com/o/Profile%20Images%2FicrHVE54xfQakhf2WdsB3F2pByh1.jpg?alt=media&token=091d7424-1408-47e3-810d-106cf8fa979d");
        mNames.add("hello");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/logindemo-98840.appspot.com/o/Post%20Images%2F2215null.jpg?alt=media&token=67b771c5-e083-46a7-9d7c-64f6b1b18a15");
        mNames.add("hi");


        mImageUrls.add("https://media.treehugger.com/assets/images/2015/02/dog-happy.jpg.662x0_q70_crop-scale.jpg");
        mNames.add("hello");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/0/05/Elakala-waterfalls-vertical.jpg");
        mNames.add("hello");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/0/05/Elakala-waterfalls-vertical.jpg");
        mNames.add("hello");


        mImageUrls.add("https://www.gandydancer.org/wp-content/uploads/2016/12/fireworks.jpg");
        mNames.add("hello");

        initRecyclerView();


    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView=findViewById(R.id.rvMain);
        staggered_RecyclerView staggered_recyclerView=new staggered_RecyclerView(this,mNames,mImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggered_recyclerView);

    }*/






  @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();

    }

    @Override
    protected void onStop() {

        if(adapter!=null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post,menu);
        return super.onCreateOptionsMenu(menu);
    }



}
