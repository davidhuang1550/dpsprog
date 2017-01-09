package com.example.david.dpsproject.AsyncTask;

import android.os.AsyncTask;

import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Model.SearchPostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by david on 2017-01-07.
 */

public class SearchPostGetMoreTask extends AsyncTask<Void,Void,Void> {

    private DatabaseReference databaseReference;
    private String SearchString;
    private SearchPostModel searchPostModel;
    private ArrayList<SearchPost> ptemp;
    private String Key;
    public SearchPostGetMoreTask(DatabaseReference db, String s, SearchPostModel searchPostm, String k){
        databaseReference=db;
        SearchString=s;
        searchPostModel=searchPostm;
        ptemp= new ArrayList<>();
        Key=k;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            databaseReference.child("SubPost").orderByChild("title").equalTo(SearchString).limitToFirst(50).startAt(Key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s: dataSnapshot.getChildren()){
                        SearchPost post = s.getValue(SearchPost.class);
                        if(post!=null) {
                            post.setKey(s.getKey());
                            ptemp.add(post);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        searchPostModel.addSearchPost(ptemp);
        searchPostModel.LoadMore();
    }
}
