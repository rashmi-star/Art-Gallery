package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView userRegistration;
    private FirebaseAuth mfirebaseauth;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name =(EditText) findViewById(R.id.etName);
        Password=(EditText)findViewById(R.id.etPassword);
        Login=(Button)findViewById(R.id.loginbtn);
        userRegistration=(TextView)findViewById(R.id.tvregister);



        mfirebaseauth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);
        FirebaseUser user=mfirebaseauth.getCurrentUser();
        

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loginval())
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });
    }

    private void validate(String userName,String userPassword){

        progress.setMessage("Loading");
        progress.show();


        mfirebaseauth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    progress.dismiss();
                    Toast.makeText(MainActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(MainActivity.this,SecondActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });

    }
    private Boolean loginval(){
        Boolean result=false;


        String name= Name.getText().toString();
        String pass=Password.getText().toString();
        if(name.isEmpty() || pass.isEmpty() ){
            Toast.makeText(this,"please enter all the details",Toast.LENGTH_SHORT).show();

        }else{
            result=true ;
        }
        return result;
    }

}
