package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {

    private EditText nameEditText, rollEditText, emailEditText, passwordEditText, confirmPasswordEditText, codeforcesHandleEditText;
    private Spinner memberTypeSpinner;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameEditText = findViewById(R.id.editTextText2);
        rollEditText = findViewById(R.id.editTextText3);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        codeforcesHandleEditText = findViewById(R.id.editTextText4);
        memberTypeSpinner = findViewById(R.id.spinner);

        rollEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        rollEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

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
            return;
        }

        if (!TextUtils.isDigitsOnly(roll)) {
            Toast.makeText(this, "Roll must be an integer number (digits only)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            writeNewUser(user.getUid(), name, roll, email, codeforcesHandle, memberType);
                        }
                        Intent intent = new Intent(Sign_up.this, Home_page.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user_role", memberType);
                        startActivity(intent);
                        finish();

                    } else {
                        String message = "Authentication failed.";
                        if (task.getException() != null) message = task.getException().getMessage();
                        Toast.makeText(Sign_up.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void writeNewUser(String userId, String name, String roll, String email, String codeforcesHandle, String memberType) {
        User user = new User(name, roll, email, codeforcesHandle, memberType);
        mDatabase.child("users").child(userId).setValue(user);
    }
}