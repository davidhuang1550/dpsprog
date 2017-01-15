package com.example.david.dpsproject.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by david on 2017-01-08.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String post = remoteMessage.getNotification().getTitle();
        String sub = remoteMessage.getNotification().getBody();
        // put bundle into intent then open it in main activity to open. fragment post

        Intent intent = new Intent(this,navigation.class);
        Bundle bundle = new Bundle();
        bundle.putString("PostId",post);
        bundle.putString("SubId",sub);
        intent.putExtra("Info",bundle);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("You got a response");
        notificationBuilder.setSmallIcon(R.mipmap.logo);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());





    }
}
