package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {

    private RecyclerView membersRecyclerView;
    private MemberAdapter memberAdapter;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.member_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        membersRecyclerView = findViewById(R.id.members_recycler_view);

        memberList = new ArrayList<>();
        memberList.add(new Member("Bipro Biswas", "General Member"));
        memberList.add(new Member("Ullas Biswas", "Committee Member"));
        memberList.add(new Member("Feroz Shah", "General Member"));

        memberAdapter = new MemberAdapter(memberList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membersRecyclerView.setLayoutManager(layoutManager);
        membersRecyclerView.setAdapter(memberAdapter);
    }
}
