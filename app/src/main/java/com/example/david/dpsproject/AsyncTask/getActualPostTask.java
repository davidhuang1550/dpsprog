package com.example.david.dpsproject.AsyncTask;

import android.os.AsyncTask;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-26.
 */

public class getActualPostTask extends AsyncTask<Void,Void,Void> {
    private ArrayList<Post> posts;
    private DatabaseReference databaseReference;
    private MyPostAdapter myAdapater;
    private ArrayList<SearchPost> searchPosts;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private int position;

    public getActualPostTask(DatabaseReference db, DefaultProgressBarPresenter dpp,
                             ArrayList<SearchPost> sp,MyPostAdapter adapter,ArrayList<Post> ps, int pos){
        databaseReference=db;
        defaultProgressBarPresenter=dpp;
        searchPosts=sp;
        myAdapater=adapter;
        posts=ps;
        position=pos;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            int i;
            int cap=position;
            if(searchPosts.size()<position+10)cap=searchPosts.size();
            else cap +=10;
            for (i = position; i < cap; i++) {
                databaseReference.child("Sub").child(searchPosts.get(i).getCategory()).child("posts").child(searchPosts.get(i).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        if(post!=null){
                            post.setKey(dataSnapshot.getKey());
                            posts.add(post);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        defaultProgressBarPresenter.hidemProgressBarFooter();
        myAdapater.notifyDataSetChanged();

    }
}
