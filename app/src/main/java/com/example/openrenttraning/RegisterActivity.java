package com.example.openrenttraning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    EditText ename, eemail, epass, ephone;
    String email, name, pass, phone;
    Button register;
    TextView login;
    ProgressBar pbar;
    FirebaseAuth fa;
    FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        eemail = findViewById(R.id.email);
        ename = findViewById(R.id.name);
        ephone = findViewById(R.id.phone);
        epass = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        pbar = findViewById(R.id.pbar);
        fa = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                email = eemail.getText().toString();
                name = ename.getText().toString();
                pass = epass.getText().toString();
                phone = ephone.getText().toString();
                if (email.length() == 0 || name.length() == 0 || pass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password Must Be 6 Character!!", Toast.LENGTH_SHORT).show();
                } else if (phone.length() < 10) {
                    Toast.makeText(getApplicationContext(), "Phone Length Must Be 10 Digit!!", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, String> user = new HashMap<>();
                    user.put("email", email);
                    user.put("name", name);
                    user.put("pass", pass);
                    user.put("phone", phone);
                    fa.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fs.collection("User").document(task.getResult().getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("email", email);
                                        myEdit.putString("name", name);
                                        myEdit.putString("pass", pass);
                                        myEdit.putString("phone", phone);
                                        myEdit.apply();

                                        pbar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                            } else {
                                pbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}