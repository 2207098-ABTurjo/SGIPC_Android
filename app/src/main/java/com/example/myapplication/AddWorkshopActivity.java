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

public class AddWorkshopActivity extends AppCompatActivity {

    private EditText workshopTopicInput;
    private TextView workshopDateInput;
    private TextView workshopTimeInput;
    private EditText workshopDurationInput;
    private Button addButton;

    private int year, month, day, hour, minute;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workshop);

        workshopTopicInput = findViewById(R.id.workshop_topic_input);
        workshopDateInput = findViewById(R.id.workshop_date_input);
        workshopTimeInput = findViewById(R.id.workshop_time_input);
        workshopDurationInput = findViewById(R.id.workshop_duration_input);
        addButton = findViewById(R.id.add_button);

        workshopDateInput.setOnClickListener(v -> showDatePicker());
        workshopTimeInput.setOnClickListener(v -> showTimePicker());

        addButton.setOnClickListener(v -> {
            String topic = workshopTopicInput.getText().toString();
            String duration = workshopDurationInput.getText().toString();
            String dateTime = selectedDate + " " + selectedTime;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("topic", topic);
            resultIntent.putExtra("time", dateTime);
            resultIntent.putExtra("duration", Integer.parseInt(duration));

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
                    workshopDateInput.setText(selectedDate);
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
                    workshopTimeInput.setText(selectedTime);
                }, hour, minute, false);
        timePickerDialog.show();
    }
}