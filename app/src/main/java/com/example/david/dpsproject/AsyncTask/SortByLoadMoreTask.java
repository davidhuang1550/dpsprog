package com.example.david.dpsproject.AsyncTask;

import android.os.AsyncTask;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Model.SortByModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by david on 2016-12-28.
 */

public class SortByLoadMoreTask  extends AsyncTask<Void,Void,Void> {

    private DatabaseReference databaseReference;
    private String sub_cat;
    private ProgressBarPresenter progressBarPresenter;
    private ArrayList<Post> posts;
    private Query query;
    private SortByModel sortByModel;
    private String key;
    public SortByLoadMoreTask(DatabaseReference db, String sub, ProgressBarPresenter dpbp, Query q, SortByModel sbm, String k){
        databaseReference=db;
        sub_cat=sub;
        query=q;
        posts= new ArrayList<>();
        progressBarPresenter=dpbp;
        sortByModel=sbm;
        key=k;
    }

    @Override
    protected void onPreExecute() {
        progressBarPresenter.showmProgressBarFooter();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for(DataSnapshot s: dataSnapshot.getChildren()){
                    Post post = s.getValue(Post.class);
                    post.setKey(s.getKey());
                    posts.add(post);
                }
                Collections.reverse(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBarPresenter.hidemProgressBarFooter();
        if(posts.isEmpty())progressBarPresenter.showErrorBar();
        else {
            sortByModel.setStartKey(posts.get(0).getKey());
            sortByModel.setPost(posts);
        }
    }


}
