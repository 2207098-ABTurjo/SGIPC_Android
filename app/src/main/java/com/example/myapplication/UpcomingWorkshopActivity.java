package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpcomingWorkshopActivity extends AppCompatActivity {

    private TextView noWorkshopMessage;
    private ConstraintLayout workshopView;
    private Button addWorkshopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upcoming_workshop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noWorkshopMessage = findViewById(R.id.no_workshop_message);
        workshopView = findViewById(R.id.workshop_view);
        addWorkshopButton = findViewById(R.id.add_workshop_button);

        boolean workshopAvailable = true;

        if (workshopAvailable) {
            noWorkshopMessage.setVisibility(View.GONE);
            workshopView.setVisibility(View.VISIBLE);
        } else {
            noWorkshopMessage.setVisibility(View.VISIBLE);
            workshopView.setVisibility(View.GONE);
        }

        boolean isCommitteeMember = true;

        if (isCommitteeMember) {
            addWorkshopButton.setVisibility(View.VISIBLE);
        } else {
            addWorkshopButton.setVisibility(View.GONE);
        }
    }
}
