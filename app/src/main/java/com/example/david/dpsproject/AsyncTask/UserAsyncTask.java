package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
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
    protected void onPreExecute() {
        navgiate.ShowProgressDialog();
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

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //   HideProgressDialog();
        if (tempU != null) {
            name.setText(tempU.getUserName());
            final View layout = (View) navgiate.findViewById(R.id.navPic);
            if (tempU.getPicture() != "" && tempU.getPicture() != null) {
                byte[] decodedString = Base64.decode(tempU.getPicture(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                navgiate.setprofilepic(decodedByte);
                layout.setBackground(new BitmapDrawable(navgiate.getResources(), decodedByte));
            }
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
        navgiate.HideProgressDialog();
        FragmentManager fragmentManager = navgiate.getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame,frontPage,"FrontPage").commit();
    }

}
