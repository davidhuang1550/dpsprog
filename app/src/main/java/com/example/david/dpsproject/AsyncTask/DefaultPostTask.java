package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by davidhuang on 2016-12-18.
 */

public class DefaultPostTask extends AsyncTask<Void,Void,Void> {

    private Activity mActivity;
    private DatabaseReference databaseReference;
    private ArrayList<String> category;
    private ArrayList<Post> posts;
    private long old_time_diff;
    private long time_diff;
    private ProgressBarPresenter progressBarPresenter;
    private int count;

    public DefaultPostTask(Activity activity,DatabaseReference dbf,ArrayList<String> cat,long startime, ProgressBarPresenter p){
        mActivity=activity;
        databaseReference=dbf;
        category=cat;
        time_diff=startime;
        posts= new ArrayList<>();
        progressBarPresenter = p;
    }

    @Override
    protected void onPreExecute() {
        progressBarPresenter.showmProgressBarFooter();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        count=0;
        old_time_diff=time_diff;
          posts.clear();
        try {
            do {
                int limit = 15 / category.size();

                time_diff -= 86400;

                for (int i = 0; i < category.size(); i++) {
                    databaseReference.child("Sub").child(category.get(i)).child("posts").orderByChild("timestamp").startAt(time_diff).endAt(old_time_diff).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                for (DataSnapshot s : dataSnapshot.getChildren()) {
                                    Post post = s.getValue(Post.class);
                                    post.setKey(s.getKey());
                                    posts.add(post);
                                }
                            } catch (DatabaseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBarPresenter.hidemProgressBarFooter();
                            Toast.makeText(mActivity, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                old_time_diff = time_diff;
                count++;
                System.out.println(count);
            } while (count < 7 && posts.size() < 10);
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

             if(count<7){
                ((navigation)mActivity).addtoposts(posts);
             }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        progressBarPresenter.hidemProgressBarFooter();
        if(count>6)progressBarPresenter.showErrorBar();

    }
}
