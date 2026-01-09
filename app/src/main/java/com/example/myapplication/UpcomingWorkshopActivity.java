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

public class UpcomingWorkshopActivity extends AppCompatActivity {

    private RecyclerView workshopsRecyclerView;
    private WorkshopAdapter workshopAdapter;
    private ArrayList<Workshop> workshopList = new ArrayList<>();
    private Button addWorkshopButton;

    private final ActivityResultLauncher<Intent> addWorkshopLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle data = result.getData().getExtras();
                    String topic = data.getString("topic");
                    String time = data.getString("time");
                    int duration = data.getInt("duration");

                    workshopList.add(new Workshop(topic, time, duration));
                    Collections.sort(workshopList, (w1, w2) -> w1.getTime().compareTo(w2.getTime()));
                    workshopAdapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_workshop);

        workshopsRecyclerView = findViewById(R.id.workshops_recycler_view);
        addWorkshopButton = findViewById(R.id.add_workshop_button);

        String userRole = getIntent().getStringExtra("user_role");
        if ("Committee Member".equals(userRole)) {
            addWorkshopButton.setVisibility(View.VISIBLE);
        } else {
            addWorkshopButton.setVisibility(View.GONE);
        }

        workshopsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workshopAdapter = new WorkshopAdapter(this, workshopList);
        workshopsRecyclerView.setAdapter(workshopAdapter);

        workshopList.add(new Workshop("Intro to CP", "2024-12-20 18:00", 120));
        workshopList.add(new Workshop("Data Structures", "2024-12-27 18:00", 180));
        Collections.sort(workshopList, (w1, w2) -> w1.getTime().compareTo(w2.getTime()));
        workshopAdapter.notifyDataSetChanged();

        addWorkshopButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpcomingWorkshopActivity.this, AddWorkshopActivity.class);
            addWorkshopLauncher.launch(intent);
        });
    }
}