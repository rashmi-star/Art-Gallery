package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.LENGTH_SHORT;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userPassword,userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setupUIViews();

        mAuth=FirebaseAuth.getInstance();


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validate()) {
                    String user_email= userEmail.getText().toString().trim();
                    String user_password=  userPassword.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this,"Registration Successful", LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,Edit_Activity.class));

                            }else{
                                Toast.makeText(RegistrationActivity.this,"Registration Failed", LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userPassword=(EditText)findViewById(R.id.etUserPassword);
        userEmail=(EditText)findViewById(R.id.etUserEmail);
        regButton=(Button)findViewById(R.id.Registerbtn);
        userLogin=(TextView)findViewById(R.id.tvUserLogin);
    }
    private Boolean validate(){
        Boolean result=false;


        String pass=userPassword.getText().toString();
        String email=userEmail.getText().toString();
        if(pass.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"please enter all the details",Toast.LENGTH_SHORT).show();

        }else{
            result=true ;
        }
        return result;
    }

}

