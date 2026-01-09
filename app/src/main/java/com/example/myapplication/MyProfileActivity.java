package com.example.myapplication;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    private EditText nameEditText, rollEditText, emailEditText, codeforcesEditText;
    private Spinner memberTypeSpinner;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUid = firebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);

        nameEditText = findViewById(R.id.profile_name);
        rollEditText = findViewById(R.id.profile_roll);
        emailEditText = findViewById(R.id.profile_email);
        codeforcesEditText = findViewById(R.id.profile_codeforces);
        memberTypeSpinner = findViewById(R.id.profile_member_type);
        saveButton = findViewById(R.id.profile_save_button);

        rollEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.member_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberTypeSpinner.setAdapter(adapter);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    nameEditText.setText(user.name != null ? user.name : "");
                    rollEditText.setText(user.roll != null ? user.roll : "");
                    emailEditText.setText(user.email != null ? user.email : "");
                    codeforcesEditText.setText(user.codeforcesHandle != null ? user.codeforcesHandle : "");

                    String type = user.memberType != null ? user.memberType : "Select Member Type";
                    ArrayAdapter spinnerAdapter = (ArrayAdapter) memberTypeSpinner.getAdapter();
                    int spinnerPos = spinnerAdapter.getPosition(type);
                    if (spinnerPos >= 0) memberTypeSpinner.setSelection(spinnerPos);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String roll = rollEditText.getText().toString().trim();
        String codeforces = codeforcesEditText.getText().toString().trim();
        String memberType = memberTypeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(roll) || memberType.equals("Select Member Type")) {
            Toast.makeText(this, "Please fill name, roll and select member type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isDigitsOnly(roll)) {
            Toast.makeText(this, "Roll must be digits only", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child("name").setValue(name);
        mDatabase.child("roll").setValue(roll);
        mDatabase.child("codeforcesHandle").setValue(codeforces);
        mDatabase.child("memberType").setValue(memberType).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MyProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
