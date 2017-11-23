package com.example.sea.material;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

import static com.example.sea.material.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    ContactsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        RecyclerView rvContacts =(RecyclerView)findViewById(R.id.rvContacts);

        contacts = Contact.createContactsList(20);

        adapter = new ContactsAdapter(this, contacts);

        rvContacts.setAdapter(adapter);//어댑터 연결

        /*스테그그리드레이아웃 적용*/
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(gridLayoutManager);

        /*데코레이션*/
        RecyclerView.ItemDecoration itemDecoration = new
                MarginItemDecoration(20);
        rvContacts.addItemDecoration(itemDecoration);
        rvContacts.setHasFixedSize(true);

    }
    ArrayList<Contact> contacts;
    /*메뉴 버튼 구성 및 어댑터 연결*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    /*메뉴버튼 기능*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Contact.setLastContactID(Contact.getLastContactID()+1);
        contacts.add(Contact.getLastContactID(),new Contact("Sea"));
        adapter.notifyItemInserted(Contact.getLastContactID());
        return super.onOptionsItemSelected(item);
    }
}
