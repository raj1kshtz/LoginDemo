package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextView Fname, Lname,emailID, Password,login;
    private Button register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        getData();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String email = emailID.getText().toString().trim();
                    String passWord = Password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendEmailVerification();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private Boolean validate(){
        String fname = Fname.getText().toString();
        String lname = Lname.getText().toString();
        String email = emailID.getText().toString();
        String password = Password.getText().toString();

        if(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Please enter all the details",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
    private void getData(){
        Fname = (TextView)findViewById(R.id.fName);
        Lname = (TextView)findViewById(R.id.lName);
        Password = (TextView)findViewById(R.id.regPassword);
        emailID = (TextView)findViewById(R.id.emailID);
        register = (Button)findViewById(R.id.regBtn);
        login = (TextView)findViewById(R.id.login1);

    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Succesfully Registered and verification mail sent!",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this,"Email Verification not sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}