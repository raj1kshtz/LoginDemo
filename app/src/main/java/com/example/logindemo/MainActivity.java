package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

    private EditText  Name;
    private EditText  PassWord;
    private TextView  info;
    private Button    Login;
    private Button    Register;
    private int counter = 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.UserName);
        PassWord = (EditText)findViewById(R.id.Password);
        info = (TextView) findViewById(R.id.incorrectAttempts);
        Login = (Button)findViewById(R.id.Login);
        Register = (Button)findViewById(R.id.buttonRegister);

        info.setText("Number of remaining attempts : 3");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), PassWord.getText().toString());

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void validate(String userName, String passWord){
        progressDialog.setMessage("Hello there");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    verifyEmail();
                }else{
                    progressDialog.dismiss();
                    counter--;
                    info.setText("Number of remaining attempts : "+counter);
                    if(counter == 0){
                        Login.setEnabled(false);
                        Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void verifyEmail(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();
        if(emailFlag){
            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }else{
            Toast.makeText(MainActivity.this,"Verify your Email",Toast.LENGTH_SHORT).show();
        }
    }

}