package com.example.myapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpcomingContestActivity extends AppCompatActivity {

    private TextView noContestMessage;
    private ConstraintLayout contestView;
    private Button addContestButton;
    private Chronometer countdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upcoming_contest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noContestMessage = findViewById(R.id.no_contest_message);
        contestView = findViewById(R.id.contest_view);
        addContestButton = findViewById(R.id.add_contest_button);
        countdownTimer = findViewById(R.id.countdown_timer);


        boolean contestAvailable = true; 

        if (contestAvailable) {
            noContestMessage.setVisibility(View.GONE);
            contestView.setVisibility(View.VISIBLE);
            countdownTimer.setBase(SystemClock.elapsedRealtime() + 3600000);
            countdownTimer.start();
        } else {
            noContestMessage.setVisibility(View.VISIBLE);
            contestView.setVisibility(View.GONE);
        }

        boolean isCommitteeMember = true;

        if (isCommitteeMember) {
            addContestButton.setVisibility(View.VISIBLE);
        } else {
            addContestButton.setVisibility(View.GONE);
        }
    }
}
