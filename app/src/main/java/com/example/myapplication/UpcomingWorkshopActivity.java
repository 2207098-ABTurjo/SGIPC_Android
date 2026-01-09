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

public class UpcomingWorkshopActivity extends AppCompatActivity {

    private RecyclerView workshopsRecyclerView;
    private WorkshopAdapter workshopAdapter;
    private ArrayList<Workshop> workshopList = new ArrayList<>();
    private Button addWorkshopButton;
    private TextView noWorkshopsText;

    private DatabaseReference workshopsRef;

    private final ActivityResultLauncher<Intent> addWorkshopLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle data = result.getData().getExtras();
                    String topic = data.getString("topic");
                    String time = data.getString("time");
                    int duration = data.getInt("duration");

                    Workshop workshop = new Workshop(topic, time, duration);
                    workshopsRef.push().setValue(workshop);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_workshop);

        workshopsRecyclerView = findViewById(R.id.workshops_recycler_view);
        addWorkshopButton = findViewById(R.id.add_workshop_button);
        noWorkshopsText = findViewById(R.id.no_workshops_text);

        String userRole = getIntent().getStringExtra("user_role");
        if ("Committee Member".equalsIgnoreCase(userRole)) {
            addWorkshopButton.setVisibility(View.VISIBLE);
        } else {
            addWorkshopButton.setVisibility(View.GONE);
        }

        workshopsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workshopAdapter = new WorkshopAdapter(this, workshopList);
        workshopsRecyclerView.setAdapter(workshopAdapter);

        workshopsRef = FirebaseDatabase.getInstance().getReference("workshops");

        workshopsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workshopList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Workshop w = child.getValue(Workshop.class);
                    if (w != null) workshopList.add(w);
                }
                Collections.sort(workshopList, (w1, w2) -> w1.getTime().compareTo(w2.getTime()));
                workshopAdapter.notifyDataSetChanged();

                if (workshopList.isEmpty()) {
                    noWorkshopsText.setVisibility(View.VISIBLE);
                    workshopsRecyclerView.setVisibility(View.GONE);
                } else {
                    noWorkshopsText.setVisibility(View.GONE);
                    workshopsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        addWorkshopButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpcomingWorkshopActivity.this, AddWorkshopActivity.class);
            addWorkshopLauncher.launch(intent);
        });
    }
}