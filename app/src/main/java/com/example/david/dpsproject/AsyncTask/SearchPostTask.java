package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Model.SearchPostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostTask extends AsyncTask<Void,Void,Void> {
    private DatabaseReference databaseReference;
    private String searchString;
    private ArrayList<SearchPost> ptemp;
    private SearchPostModel searchPostModel;
    public SearchPostTask(DatabaseReference db, String s,SearchPostModel searchPostm){
        databaseReference=db;
        searchString=s;

        ptemp= new ArrayList<>();
        searchPostModel=searchPostm;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
                databaseReference.child("SubPost").orderByChild("title").equalTo(searchString).limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
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
    protected void onPostExecute(Void aVoid) {
       // defaultProgressBarPresenter.hidemProgressBarFooter();
        searchPostModel.setSearchPosts(ptemp);
        searchPostModel.LoadMore();

    }
}
