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

public class EditActivity extends AppCompatActivity {

    EditText ename, epass, ephone;
    String name, pass, phone;
    Button edit;
    TextView login;
    ProgressBar pbar;
    FirebaseAuth fa;
    FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ename = findViewById(R.id.name);
        ephone = findViewById(R.id.phone);
        epass = findViewById(R.id.password);
        edit = findViewById(R.id.edit);
        login = findViewById(R.id.login);
        pbar = findViewById(R.id.pbar);
        fa = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        ephone.setText(sharedPreferences.getString("phone", " "));
        ename.setText(sharedPreferences.getString("name", " "));
        epass.setText(sharedPreferences.getString("pass", " "));

        String email = sharedPreferences.getString("email", " ");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                name = ename.getText().toString();
                pass = epass.getText().toString();
                phone = ephone.getText().toString();
                if (name.length() == 0 || pass.length() == 0) {
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


                    fs.collection("User").document(fa.getCurrentUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("email", email);
                            myEdit.putString("name", name);
                            myEdit.putString("pass", pass);
                            myEdit.putString("phone", phone);
                            myEdit.apply();

                            pbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Edit Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(EditActivity.this, MainActivity.class));
    }
}