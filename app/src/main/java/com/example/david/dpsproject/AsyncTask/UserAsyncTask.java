package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Fragments.FrontPage;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by david on 2016-11-28.
 */
public class UserAsyncTask extends AsyncTask<Void,Void,Void>{

    private navigation navgiate;
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;
    private Users tempU;
    private TextView name;
    private FrontPage frontPage;
    public UserAsyncTask(navigation nav,DatabaseReference databaseReferenceb,FirebaseUser fbu,FrontPage fp){
        navgiate=nav;
        dbReference=databaseReferenceb;
        firebaseUser=fbu;
        frontPage=fp;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            do {

                dbReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            tempU = dataSnapshot.getValue(Users.class);
                            name = (TextView) navgiate.findViewById(R.id.headText);

                        } catch (DatabaseException e) {
                            Toast.makeText(navgiate, "something went wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //   Thread.sleep(1000);
            } while (name != null && tempU != null);
            Thread.sleep(1000);
            navgiate.setUser(tempU);
            SharedPreferences sharedPreferences = navgiate.getSharedPreferences(navgiate.getString(R.string.FCM_PREF),Context.MODE_PRIVATE);
            String token =sharedPreferences.getString(navgiate.getString(R.string.FCM_TOKEN),"");
            if(!token.equals("")) dbReference.child("Users").child(firebaseUser.getUid()).child("FcmToken").setValue(FirebaseInstanceId.getInstance().getToken());


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        navgiate.HideProgressDialog();

        if(tempU!=null){

            name.setText(tempU.getUserName());
            Menu menu=navgiate.getSubMenu();

            if(menu!=null) {
                final ArrayList<String> subcat = tempU.getSubcategory();
                for (int i = 0; i < subcat.size(); i++) {
                    menu.add(R.id.second_nav, Menu.NONE, 0, subcat.get(i));

                }
            }

        }
        else{
            navgiate.freeUserData();
            FirebaseAuth.getInstance().signOut();

        }

       if(navgiate.isNotification())navgiate.setPostview();
        else {
            FragmentManager fragmentManager = navgiate.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, frontPage, "FrontPage").commit();
        }
    }

}
