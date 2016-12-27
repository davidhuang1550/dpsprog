package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.AsyncTask.ProfileTask;
import com.example.david.dpsproject.AsyncTask.getUserPostTask;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by david on 2016-12-25.
 */

public class SearchUserModel {
    private DatabaseReference databaseReference;
    private View myView;
    private String username;
    private Activity mActivity;
    private TextView joindate;
    private TextView numPost;
    private Users users;
    private  com.github.siyamed.shapeimageview.CircularImageView profileP;
    public SearchUserModel(DatabaseReference db, View view, String user, Activity activity){
        databaseReference=db;
        myView=view;
        username=user;
        joindate = (TextView)myView.findViewById(R.id.MemberSincedate);
        numPost = (TextView)myView.findViewById(R.id.NumberPosts);
        profileP = (com.github.siyamed.shapeimageview.CircularImageView) myView.findViewById(R.id.profileImage);
        mActivity=activity;

    }
    public void getUser(){
        databaseReference.child("Users").orderByChild("userName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    users= null;
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        users= d.getValue(Users.class);
                    }
                    if(users.getPicture()!=null){
                        Bitmap b =users.getDecodedPicture();
                        profileP.setImageDrawable(new BitmapDrawable(mActivity.getResources(),b));
                        profileP.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                    else{
                        profileP.setImageResource(R.drawable.profile);
                    }
                    joindate.setText(users.getJoinDate());
                    numPost.setText(Integer.toString(users.getNumOfPosts()));
                    loadListView();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadListView(){
        final getUserPostTask getProfilePost = new getUserPostTask(databaseReference,users,myView,mActivity);
        getProfilePost.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getProfilePost.getStatus()== AsyncTask.Status.RUNNING){
                    getProfilePost.cancel(true);

                    Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                }
            }
        },5000);
    }
}
