package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by david on 2016-12-25.
 */

public class getUserPostTask extends AsyncTask<Void,Void,Void> {
    private DatabaseReference databaseReference;
    private Users users;
    private ArrayList<Post> ptemp;
    private View myView;;
    private Activity mActivity;
    private ListView listView;
    private ProgressBarPresenter progressBarPresenter;
    private MyPostAdapter adapter;

    public getUserPostTask(DatabaseReference db, Users u, View view,Activity act){
        databaseReference=db;
        users=u;
        ptemp= new ArrayList<Post>();
        myView=view;
        mActivity=act;
        listView = (ListView) myView.findViewById(R.id.profile_list_view);
        progressBarPresenter = new ProgressBarPresenter(mActivity,listView);
        adapter= new MyPostAdapter(mActivity, ptemp);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPreExecute() {
        progressBarPresenter.showmProgressBarFooter();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Iterator it =users.getPosts().entrySet().iterator();
        ptemp.clear();
        try {

            do {
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String key = pair.getKey().toString();
                    ArrayList<String> values = (ArrayList<String>) pair.getValue();
                    for (String s : values) {
                        databaseReference.child("Sub").child(key).child("posts").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Post post = dataSnapshot.getValue(Post.class);
                                if (post != null) {
                                    post.setKey(dataSnapshot.getKey());
                                    ptemp.add(post);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(mActivity, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                Thread.sleep(500);
            }while (ptemp.size()==0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBarPresenter.hidemProgressBarFooter();
        adapter.notifyDataSetChanged();
    }
}
