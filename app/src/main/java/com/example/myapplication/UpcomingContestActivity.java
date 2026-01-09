package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class UpcomingContestActivity extends AppCompatActivity {

    private RecyclerView contestsRecyclerView;
    private ContestAdapter contestAdapter;
    private ArrayList<Contest> contestList = new ArrayList<>();
    private Button addContestButton;
    private TextView noContestsText;

    private DatabaseReference contestsRef;

    private final ActivityResultLauncher<Intent> addContestLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle data = result.getData().getExtras();
                    String title = data.getString("title");
                    String time = data.getString("time");
                    int duration = data.getInt("duration");
                    String link = data.getString("link");

                    // push to Firebase
                    Contest contest = new Contest(title, time, duration, link);
                    contestsRef.push().setValue(contest);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_contest);

        contestsRecyclerView = findViewById(R.id.contests_recycler_view);
        addContestButton = findViewById(R.id.add_contest_button);
        noContestsText = findViewById(R.id.no_contests_text);

        String userRole = getIntent().getStringExtra("user_role");
        if ("Committee Member".equalsIgnoreCase(userRole)) {
            addContestButton.setVisibility(View.VISIBLE);
        } else {
            addContestButton.setVisibility(View.GONE);
        }

        contestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contestAdapter = new ContestAdapter(this, contestList);
        contestsRecyclerView.setAdapter(contestAdapter);

        contestsRef = FirebaseDatabase.getInstance().getReference("contests");

        contestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contestList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Contest c = child.getValue(Contest.class);
                    if (c != null) contestList.add(c);
                }
                Collections.sort(contestList, (c1, c2) -> c1.getTime().compareTo(c2.getTime()));
                contestAdapter.notifyDataSetChanged();

                if (contestList.isEmpty()) {
                    noContestsText.setVisibility(View.VISIBLE);
                    contestsRecyclerView.setVisibility(View.GONE);
                } else {
                    noContestsText.setVisibility(View.GONE);
                    contestsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        addContestButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpcomingContestActivity.this, AddContestActivity.class);
            addContestLauncher.launch(intent);
        });
    }
}