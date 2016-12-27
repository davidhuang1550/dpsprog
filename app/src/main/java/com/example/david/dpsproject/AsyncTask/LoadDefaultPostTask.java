package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Model.PostModel;
import com.example.david.dpsproject.Presenter.DefaultProgressBarPresenter;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by david on 2016-11-30.
 */
public class LoadDefaultPostTask extends AsyncTask<Void,Void,Void>{
    private Activity mActivity;
    private DatabaseReference databaseReference;

    private ArrayList<String> category;
    private ArrayList<Post> posts;
    private SwipeRefreshLayout refreshLayout;
    private long old_time_diff;
    private long time_diff;
    private MyPostAdapter adapter;
    private ArrayList<Post> p;
    private ListView listView;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private PostModel postModel;
    public LoadDefaultPostTask(Activity activity, DatabaseReference dbf, View view, SwipeRefreshLayout l,
                               ArrayList<String> cat, DefaultProgressBarPresenter progress, ListView le, MyPostAdapter adapt, ArrayList<Post> po, PostModel postmod){
        mActivity=activity;
        databaseReference=dbf;
        category=cat;
        adapter=adapt;
        posts= new ArrayList<Post>();
        refreshLayout=l;
        p = po;
        listView=le;
        defaultProgressBarPresenter = progress;
        postModel=postmod;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        try {
            time_diff = (System.currentTimeMillis() / 1000);
            old_time_diff=time_diff;
            posts.clear();
            do {
                int limit=15/category.size();
                time_diff -=  86400;
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
                                ((navigation)mActivity).HideProgressDialog();
                                Toast.makeText(mActivity,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    old_time_diff=time_diff;
                Thread.sleep(500);
            } while (posts.size()<10);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(Void aVoid) {
            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeoutleft);
            Collections.shuffle(posts);

            for (int i = 0; i < 10; i++) {
                p.add(posts.get(i));
            }
            defaultProgressBarPresenter.hidemProgressBarFooter();
            ((navigation)mActivity).HideProgressDialog();
            listView.startAnimation(animation);
            adapter.notifyDataSetChanged();
            if (refreshLayout != null) refreshLayout.setRefreshing(false);
            postModel.setTimestamp(old_time_diff);
            postModel.setPostId(posts);
            postModel.setCurrentList(p, 10);


    }

}
