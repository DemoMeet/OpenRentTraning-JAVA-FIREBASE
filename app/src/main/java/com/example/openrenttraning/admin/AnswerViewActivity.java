package com.example.openrenttraning.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.openrenttraning.R;
import com.example.openrenttraning.SessionActivity;
import com.example.openrenttraning.helper.AnswerModel;
import com.example.openrenttraning.helper.QuestionModel;
import com.example.openrenttraning.helper.answerViewAdapter;
import com.example.openrenttraning.helper.questionAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AnswerViewActivity extends AppCompatActivity {

    TextView name, date;
    String email, dates;

    public static String user;
    answerViewAdapter arr;
    RecyclerView ansrv;
    ArrayList<AnswerModel> answer = new ArrayList<AnswerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_view);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        ansrv = findViewById(R.id.ansrv);



        Intent i = getIntent();
        name.setText(i.getStringExtra("Name"));
        user = i.getStringExtra("Name");
        date.setText(i.getStringExtra("DATE"));
        dates = i.getStringExtra("DATE");
        email = i.getStringExtra("Email");

        arr = new answerViewAdapter(answer, AnswerViewActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ansrv.setHasFixedSize(true);
        ansrv.setLayoutManager(manager);
        ansrv.setAdapter(arr);

        FirebaseFirestore.getInstance().collection("Answer").whereEqualTo("Date", dates).whereEqualTo("User", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot d: queryDocumentSnapshots){
                        answer.add(new AnswerModel(d.getString("Question"), d.getString("Adding Time"),d.getString("Posting Time"),d.getString("Answer"),d.getBoolean("Voice")));
                        arr.notifyDataSetChanged();
                    }
                }
            }
        });


    }
}