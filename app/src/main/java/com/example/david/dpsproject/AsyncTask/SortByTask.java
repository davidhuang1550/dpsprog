package com.example.david.dpsproject.AsyncTask;

import android.os.AsyncTask;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Model.SortByModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
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

public class SortByTask extends AsyncTask<Void,Void,Void> {
    private DatabaseReference databaseReference;
    private String sub_cat;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private ArrayList<Post> posts;
    private Query query;
    private SortByModel sortByModel;

    public SortByTask(DatabaseReference db, String sub, DefaultProgressBarPresenter dpbp,Query q, SortByModel sbm){
        databaseReference=db;
        sub_cat=sub;
        posts= new ArrayList<>();
        defaultProgressBarPresenter=dpbp;
        query=q;
        sortByModel=sbm;
    }

    @Override
    protected void onPreExecute() {
        defaultProgressBarPresenter.showmProgressBarFooter();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    posts.clear();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Post post = s.getValue(Post.class);
                        post.setKey(s.getKey());
                        posts.add(post);
                    }
                    Collections.reverse(posts);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
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
        defaultProgressBarPresenter.hidemProgressBarFooter();
        sortByModel.setStartKey(posts.get(0).getKey());
        sortByModel.setPost(posts);
    }
}
