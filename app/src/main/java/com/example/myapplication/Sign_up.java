package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sign_up extends AppCompatActivity {

    private EditText nameEditText, rollEditText, emailEditText, passwordEditText, confirmPasswordEditText, codeforcesHandleEditText;
    private Spinner memberTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditText = findViewById(R.id.editTextText2);
        rollEditText = findViewById(R.id.editTextText3);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        codeforcesHandleEditText = findViewById(R.id.editTextText4);
        memberTypeSpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.member_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberTypeSpinner.setAdapter(adapter);
    }

    public void signUp(View view) {
        String name = nameEditText.getText().toString().trim();
        String roll = rollEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String codeforcesHandle = codeforcesHandleEditText.getText().toString().trim();
        String memberType = memberTypeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(roll) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(codeforcesHandle) ||
                memberType.equals("Select Member Type")) {
            Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, Home_page.class);
            startActivity(intent);
        }
    }
}
