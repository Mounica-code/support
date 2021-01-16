package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.loginpage.Models.FirebaseUserModel;
import com.example.loginpage.Models.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    static EditText username;
    EditText password;
    Button loginButton;
    String users, pass;
    DatabaseReference usersRef;
    String currentDeviceId;
    FirebaseDatabase database;
    User user = User.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedpreferences = getSharedPreferences(user.appPreferences, Context.MODE_PRIVATE);
        user.sharedpreferences = sharedpreferences;

        currentDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");


        username = (EditText)findViewById(R.id.editTextNumber);
        password = (EditText)findViewById(R.id.editTextPassword);
        loginButton = (Button)findViewById(R.id.button);

        loginButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
               users = username.getText().toString();
                pass = password.getText().toString();

                if(users.equals("")){
                    showMessage("Invalid", "Please enter name");
                  //  username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    showMessage("Invalid", "Please enter name");
                    //password.setError("can't be blank");
                }
                else{


                    final FirebaseUserModel firebaseUserModel = new FirebaseUserModel();
                    firebaseUserModel.setUsername(users);
                    firebaseUserModel.setPassword(pass);
                    firebaseUserModel.setDeviceId(currentDeviceId);
                    firebaseUserModel.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

                    final ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
                    Dialog.setMessage("Please wait..");
                    Dialog.setCancelable(false);
                    Dialog.show();

                    final DatabaseReference newRef = usersRef.push();
                    newRef.setValue(firebaseUserModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Dialog.dismiss();
                            if (user.login(firebaseUserModel)) {
                                moveToChattingScreen();
                            }

                        }
                    });

                }

            }
        });
        username.getText().clear();
        password.getText().clear();
    }
    public void moveToChattingScreen() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    public void showMessage(String strTitle, String strMessage) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(strTitle)
                .setMessage(strMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}