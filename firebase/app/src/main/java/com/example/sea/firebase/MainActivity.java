package com.example.sea.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sea on 2017-11-19.
 */

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage mFirebaseStorage;
    EditText editName;
    EditText editHp;
    ListView listView;
    ArrayList<Listviewi> data;
    Listviewi item;
    ListviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = (EditText) findViewById(R.id.editName);
        editHp = (EditText) findViewById(R.id.editHp);
        final Button btnAdd = (Button) findViewById(R.id.btnAdd);

        data = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewAdapter(MainActivity.this, R.layout.item, data);
        listView.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스 인스턴스 레퍼런스
                DatabaseReference myRef = database.getReference("users");//유저노드 접근
                String key = myRef.push().getKey();//고유 키 값 생성

                HashMap<String, Object> postValues = new HashMap<>();
                postValues.put("name", editName.getText().toString());
                postValues.put("hp", editHp.getText().toString());

                myRef.child(key).setValue(postValues);//유저 노드에 고유 키값으로 데이터구조 추가

                Toast.makeText(MainActivity.this, "Add Successful.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스 인스턴스 레퍼런스
        DatabaseReference myRef = database.getReference("users");//유저노드 접근
        Query contacts = myRef.orderByChild("name").limitToFirst(10);
        contacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String a = snapshot.child("name").getValue(String.class);
                    String b = snapshot.child("hp").getValue(String.class);
                    item = new Listviewi(a,b);
                    data.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "zzz",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}