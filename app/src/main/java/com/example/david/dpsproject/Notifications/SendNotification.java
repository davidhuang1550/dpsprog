package com.example.david.dpsproject.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 2017-01-08.
 */

public class SendNotification {

    private Activity mActivity;
    private String app_server_url ="https://ecbolic-careers.000webhostapp.com/fcm_insert.php";
    public SendNotification(Activity activity){
        mActivity=activity;
    }



    public void sendToken(){
        SharedPreferences sharedPreferences = mActivity.getApplicationContext().getSharedPreferences(mActivity.getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token= sharedPreferences.getString(mActivity.getString(R.string.FCM_TOKEN),"");
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

                return params;
            }
        };
        MySingleton.getmInstance(((navigation)mActivity)).addRequestQueue(stringRequest);




    }
}
