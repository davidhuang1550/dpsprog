package com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 2017-01-07.
 */

public class DataBaseConnectionsPresenter {
    private DatabaseReference dbReference;
    private FirebaseAuth authentication;
    private FirebaseUser firebaseUser;

    public DataBaseConnectionsPresenter(){
        authentication= FirebaseAuth.getInstance(); // get instance of my firebase console
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database
        firebaseUser = authentication.getCurrentUser();
    }
    public void setFirebaseUser(){
        firebaseUser = authentication.getCurrentUser();
    }
    public void setFirebaseUser(FirebaseUser fbu){
        firebaseUser = fbu;
    }
    public DatabaseReference getDbReference(){
        return dbReference;
    }
    public FirebaseAuth getFireBaseAuth(){
        return authentication;
    }
    public FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }
    public String getUID(){
        return firebaseUser.getUid();
    }
}
