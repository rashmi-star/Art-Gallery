package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class positivity extends AppCompatActivity {

    List<String> myList;
    DatabaseReference reference;
    List<String> displaylistContent;

    public Button motivate_btn;


   // String[] positive={"Have a good day","have a fantastic day"};
    String current;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positivity);

        text = (TextView) findViewById(R.id.textTv);
        motivate_btn=(Button)findViewById(R.id.motivateme_btn);
        myList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("positive");
        displaylistContent=new ArrayList<>();

        //uploadList(null);
        DisplayList(null);
        //Random random=new Random();
        //current=positive[random.nextInt(positive.length)];

        //text.setText(current);
    }

        /*public void uploadList (View v)
        {
            myList.add("“Everything you can imagine is real.”");
            myList.add("All the visible world is only light on form");
            myList.add("\"Drawing is vision on paper.\"");
            myList.add("\"Photography is an immediate reaction, drawing is a meditation.\"");
            myList.add("\"Drawing is not what one sees but what one can make others see.\"");
            myList.add("\"In drawing, one must look for or suspect that there is more than is casually seen.\"");
            myList.add("“If you can draw well, tracing won’t hurt; and if you can’t draw well, tracing won’t help.”");

            reference.setValue(myList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(positivity.this, "updated successfully", Toast.LENGTH_SHORT);

                }
            });

        }*/
        public void DisplayList(View v)
        {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        displaylistContent.clear();
                        for (DataSnapshot dss:dataSnapshot.getChildren())
                        {
                            String quote =  dss.getValue(String.class);
                            displaylistContent.add(quote);

                        }
                        motivate_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                Random random=new Random();
                                current= displaylistContent.get(random.nextInt(displaylistContent.size()));
                                text.setText(current);


                            }
                        });
                       /* Random random=new Random();
                        current= displaylistContent.get(random.nextInt(displaylistContent.size()));
                        text.setText(current);*/



                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

}
