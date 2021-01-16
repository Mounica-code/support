package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatApp extends AppCompatActivity  {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);
        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidlogin-79b98.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith );
        reference2 = new Firebase("https://androidlogin-79b98.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();


                if(userName.equals(UserDetails.username)){
                    addMessageBox("You\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chatWith + "\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){



        TextView textView1;
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy(HH:mm)");
        date = dateFormat.format(calendar.getTime());

        if(type == 1) {
            textView1 = new TextView(ChatApp.this);
            textView1.setText(date);
            TextView textView = new TextView(ChatApp.this);
            RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.setMargins(50,0,0,0);
            textView.setLayoutParams(textViewLayoutParams);
            textView1.setLayoutParams(textViewLayoutParams);
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textView.setLayoutParams(textViewLayoutParams);
            textView.setText(message);
            textView1.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            layout.addView(textView);
            layout.addView(textView1);
            textView.setBackgroundResource(R.drawable.rounded_corner1);



        }
        else{
            textView1 = new TextView(ChatApp.this);
            textView1.setText(date);
            TextView textView = new TextView(ChatApp.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(700,0,0,0);
            textView.setLayoutParams(params);
            textView1.setLayoutParams(params);
            textView.setText(message);
            textView.setGravity(Gravity.RIGHT | Gravity.TOP);
            textView.setBackgroundColor(Color.parseColor("#f0e68c"));
            textView1.setGravity(Gravity.RIGHT | Gravity.TOP);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            layout.addView(textView);
            layout.addView(textView1);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

       // layout.addView(textView);
       // layout.addView(textView1);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
