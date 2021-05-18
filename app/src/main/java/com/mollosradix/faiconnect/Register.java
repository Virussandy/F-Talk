package com.mollosradix.faiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText email,password,confirm_password;
    private Button register;
    private TextView reg_textview;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.reg_email_form);
        password = findViewById(R.id.reg_password_form);
        confirm_password = findViewById(R.id.reg_confirmpassword_form);
        register = findViewById(R.id.register_btn);
        reg_textview = findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        reg_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
        String email_id = email.getText().toString();
        String pass = password.getText().toString();
        String confirm_pass = confirm_password.getText().toString();

        progressDialog.show();

        if (TextUtils.isEmpty(email_id) && TextUtils.isEmpty(pass)){
            progressDialog.dismiss();
            email.setError("This field is required");
            password.setError("This Field is required");
            confirm_password.setError("This Field is required");
        }else if(TextUtils.isEmpty(email_id)){
            progressDialog.dismiss();
            email.setError("Email is required");
        }else if(TextUtils.isEmpty(pass)){
            progressDialog.dismiss();
            password.setError("Password is required");
        }else if(TextUtils.isEmpty(pass)){
            progressDialog.dismiss();
            confirm_password.setError("Confirm password is required");
        }else if(!pass.equals(confirm_pass) ){
            progressDialog.dismiss();
            password.setError("Password and Confirm password should match");
        }else {
            firebaseAuth.createUserWithEmailAndPassword(email_id,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}