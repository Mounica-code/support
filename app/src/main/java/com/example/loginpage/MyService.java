package com.example.loginpage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       if (remoteMessage.getData().size()>0)
       {
           String title,message,img_url;

           title = remoteMessage.getData().get("title");
           message = remoteMessage.getData().get("message");
           img_url = remoteMessage.getData().get("img_url");

           Intent intent = new Intent(this,NotificationActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
           Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
           builder.setContentTitle(title);
           builder.setContentText(message);
           builder.setContentIntent(pendingIntent);
           builder.setSound(sounduri);
           builder.setSmallIcon(R.drawable.ic_notification);


           com.android.volley.toolbox.ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
               @Override
               public void onResponse(Bitmap response) {

                   builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                   NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                   notificationManager.notify(0,builder.build());
               }
           }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener(){
               @Override
               public void onErrorResponse(VolleyError error) {

               }
           });

           MySingleton.getmInstance(this).addToReuestQue(imageRequest);
       }
    }
}