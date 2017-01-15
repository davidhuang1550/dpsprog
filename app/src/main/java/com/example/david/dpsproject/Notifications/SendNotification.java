package com.example.david.dpsproject.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 2017-01-08.
 */

public class SendNotification {

    private Activity mActivity;
    private String token;
    private String PosterId;
    private String Key;
    private String Category;
    private String app_server_url ="https://ecbolic-careers.000webhostapp.com/fcm_insert.php";
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    public SendNotification(Activity activity,String Poster,String k,String sub){
        mActivity=activity;
        PosterId= Poster;
        Key=k;
        Category=sub;
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();

    }

    public void send(){
        dataBaseConnectionsPresenter.getDbReference().child("Users").child(PosterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Users users = dataSnapshot.getValue(Users.class);
                    token=users.getFcmToken();
                    sendToken();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendToken(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, app_server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("fcm_token",token);
                params.put("post_key",Key);
                params.put("sub_key",Category);

                return params;
            }
        };
        MySingleton.getmInstance(((navigation)mActivity)).addRequestQueue(stringRequest);




    }
}
