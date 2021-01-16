package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.loginpage.Models.FirebaseUserModel;
import com.example.loginpage.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<ExampleItem> list;
    RecyclerView recyclerView;
    SearchView searchview;
    private ArrayList<String> al = new ArrayList<>();
   Button button;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String currentDeviceId;
    // private Toolbar mTopToolbar;
    User user = User.getInstance();
    FirebaseDatabase database;
    DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        
        SharedPreferences sharedpreferences = getSharedPreferences(user.appPreferences, Context.MODE_PRIVATE);
        user.sharedpreferences = sharedpreferences;

        currentDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(HomePage.this,ChattingActivity.class);
               startActivity(intent);
            }
        });


        ref = FirebaseDatabase.getInstance().getReference().child("Mouni");
        recyclerView = findViewById(R.id.recyclerView);
        searchview = findViewById(R.id.editTextSearch);

        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview.setIconified(false);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.Notification:
                Intent intent = new Intent(HomePage.this,NotificationActivity.class);
                startActivity(intent);
                break;
        }
        return true;

    }


    private void moveToChattingScreen() {
        Intent intent = new Intent(this, ChattingActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null);
        {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        list = new  ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            list.add(ds.getValue(ExampleItem.class));

                        }
                        CustomAdapter customadapter = new CustomAdapter(list);
                         recyclerView.setAdapter(customadapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomePage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchview != null){
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String s)
                {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }
    private void search(String str)
    {
        ArrayList<ExampleItem> myList = new ArrayList<>();
        for(ExampleItem object : list)
        {
            if (object.getQues().toLowerCase().contains(str.toLowerCase()) || object.getAns().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        CustomAdapter customAdapter = new CustomAdapter(myList);
        recyclerView.setAdapter(customAdapter);
    }

}