package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class UpcomingContestActivity extends AppCompatActivity {

    private RecyclerView contestsRecyclerView;
    private ContestAdapter contestAdapter;
    private ArrayList<Contest> contestList = new ArrayList<>();
    private Button addContestButton;

    private final ActivityResultLauncher<Intent> addContestLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle data = result.getData().getExtras();
                    String title = data.getString("title");
                    String time = data.getString("time");
                    int duration = data.getInt("duration");
                    String link = data.getString("link");

                    contestList.add(new Contest(title, time, duration, link));
                    Collections.sort(contestList, (c1, c2) -> c1.getTime().compareTo(c2.getTime()));
                    contestAdapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_contest);

        contestsRecyclerView = findViewById(R.id.contests_recycler_view);
        addContestButton = findViewById(R.id.add_contest_button);

        String userRole = getIntent().getStringExtra("user_role");
        if ("Committee Member".equals(userRole)) {
            addContestButton.setVisibility(View.VISIBLE);
        } else {
            addContestButton.setVisibility(View.GONE);
        }

        contestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contestAdapter = new ContestAdapter(this, contestList);
        contestsRecyclerView.setAdapter(contestAdapter);


        contestList.add(new Contest("Weekly Contest #1", "2024-12-24 22:00", 90, "https://www.google.com"));
        contestList.add(new Contest("Weekly Contest #2", "2024-12-31 22:00", 120, "https://www.google.com"));
        Collections.sort(contestList, (c1, c2) -> c1.getTime().compareTo(c2.getTime()));
        contestAdapter.notifyDataSetChanged();

        addContestButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpcomingContestActivity.this, AddContestActivity.class);
            addContestLauncher.launch(intent);
        });
    }
}