package com.example.david.dpsproject.Notifications;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by david on 2017-01-08.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private Context context;
    private RequestQueue requestQueue;

    private MySingleton(Context con){
        context=con;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }
    public static synchronized  MySingleton getmInstance( Context con){
        if(mInstance==null){
            mInstance= new MySingleton(con);
        }
        return mInstance;
    }
    public<T> void addRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
