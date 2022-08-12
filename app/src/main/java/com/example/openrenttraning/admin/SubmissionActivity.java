package com.example.openrenttraning.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.openrenttraning.R;
import com.example.openrenttraning.helper.userAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubmissionActivity extends AppCompatActivity {

    public static String date;
    List<String> uuu = new ArrayList<String>();
    List<String> uuu2 = new ArrayList<String>();
    userAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        date = getIntent().getStringExtra("DATE");
        RecyclerView userRv = findViewById(R.id.usersrv);
        userRv.setLayoutManager(new LinearLayoutManager(SubmissionActivity.this));
        userRv.setHasFixedSize(true);
        userAdapter = new userAdapter(uuu, uuu2, SubmissionActivity.this);
        userRv.setAdapter(userAdapter);
        Log.e("TAG", date);
        FirebaseFirestore.getInstance().collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot q) {
                if(!q.isEmpty()){
                    for(DocumentSnapshot d: q){
                        uuu.add(d.getString("email"));
                        uuu2.add(d.getString("name"));
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
        SearchView searchView = findViewById(R.id.searchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter = new userAdapter(uuu, uuu2, SubmissionActivity.this);
                userRv.setAdapter(userAdapter);
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });


        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayou);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

    }
}