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
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private ProgressDialog progressDialog;
    private TextView register;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String auth_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        String intentUrl = intent.getStringExtra("url");

        email = findViewById(R.id.email_form);
        password = findViewById(R.id.password_form);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(SharedPreferences.getLoggedStatus(getApplicationContext())){
            Intent newIntent = new Intent(getApplicationContext(),Dashboard.class);
            newIntent.putExtra("url",intentUrl);
            startActivity(newIntent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_fun();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

    }

    private void login_fun() {
        String email_id = email.getText().toString();
        String pass = password.getText().toString();

        progressDialog.show();

        if (TextUtils.isEmpty(email_id) && TextUtils.isEmpty(pass)){
            progressDialog.dismiss();
            email.setError("This field is required");
            password.setError("This Field is required");
        }else if(TextUtils.isEmpty(email_id)){
            progressDialog.dismiss();
            email.setError("Mobile no. is required");
        }else if(TextUtils.isEmpty(pass)){
            progressDialog.dismiss();
            password.setError("Password is required");
        }else {
            firebaseAuth.signInWithEmailAndPassword(email_id, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    auth_id = firebaseAuth.getUid();
                    SharedPreferences.setLoggedIn(getApplicationContext(),true);
                    startActivity(new Intent(Login.this,Dashboard.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}