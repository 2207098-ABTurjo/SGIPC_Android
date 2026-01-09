package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddContestActivity extends AppCompatActivity {

    private EditText contestTitleInput;
    private TextView contestDateInput;
    private TextView contestTimeInput;
    private EditText contestDurationInput;
    private EditText contestLinkInput;
    private Button addButton;

    private int year, month, day, hour, minute;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contest);

        contestTitleInput = findViewById(R.id.contest_title_input);
        contestDateInput = findViewById(R.id.contest_date_input);
        contestTimeInput = findViewById(R.id.contest_time_input);
        contestDurationInput = findViewById(R.id.contest_duration_input);
        contestLinkInput = findViewById(R.id.contest_link_input);
        addButton = findViewById(R.id.add_button);

        contestDateInput.setOnClickListener(v -> showDatePicker());
        contestTimeInput.setOnClickListener(v -> showTimePicker());

        addButton.setOnClickListener(v -> {
            String title = contestTitleInput.getText().toString();
            String duration = contestDurationInput.getText().toString();
            String link = contestLinkInput.getText().toString();
            String dateTime = selectedDate + " " + selectedTime;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("time", dateTime);
            resultIntent.putExtra("duration", Integer.parseInt(duration));
            resultIntent.putExtra("link", link);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    contestDateInput.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                    contestTimeInput.setText(selectedTime);
                }, hour, minute, false);
        timePickerDialog.show();
    }
}