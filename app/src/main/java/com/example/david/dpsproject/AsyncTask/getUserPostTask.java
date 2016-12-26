package com.example.david.dpsproject.AsyncTask;

import android.os.AsyncTask;

import com.example.david.dpsproject.Class.Users;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by david on 2016-12-25.
 */

public class getUserPostTask extends AsyncTask<Void,Void,Void> {
    private DatabaseReference databaseReference;
    private Users users;


    public getUserPostTask(DatabaseReference db, Users u){
        databaseReference=db;
        users=u;
    }



    @Override
    protected Void doInBackground(Void... voids) {

       // for()



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
