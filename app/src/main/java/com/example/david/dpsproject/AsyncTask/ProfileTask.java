package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by david on 2016-11-23.
 */
public class ProfileTask extends AsyncTask<Void,Void,Void>{
    private String sub_book_post;
    private String profile_book_post;
    private View myView;
    private Activity mActivity;
    private ArrayList<Post> ptemp;
    private ListView listView;

    private DatabaseReference dbReference;
    private Users user;

    private ProgressDialog pDialog;
    public ProfileTask(String sub_posts,String profile_post, View v,Activity activity,DatabaseReference dbRef, Users u){
        sub_book_post=sub_posts;
        profile_book_post=profile_post;
        myView=v;
        mActivity=activity;

        ptemp=new ArrayList<Post>();

        dbReference=dbRef;
        user=u;

    }
    public void ShowProgressDialog() { // progress
        if (pDialog == null) {
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Loading Posts");
            pDialog.setIndeterminate(true);
        }
        pDialog.show();
    }
    public void HideProgressDialog() {
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    protected void onPreExecute() {
        ShowProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            do {
                Iterator iterator;
                if(profile_book_post.equals("Posts")) {
                    iterator = user.getPosts().entrySet().iterator();
                }else{
                    iterator = user.getBookmarks().entrySet().iterator();
                }
                    while (iterator.hasNext()){
                        Map.Entry pair = (Map.Entry)iterator.next();
                        ArrayList<String> values = (ArrayList<String>) pair.getValue();
                        for(String s: values) {
                            dbReference.child("Sub").child(pair.getKey().toString()).child("posts").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    if(post!=null){
                                        post.setKey(dataSnapshot.getKey());
                                        ptemp.add(post);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }


                /*dbReference.child("Users").child(user.get).child(profile_book_post).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot s : dataSnapshot.getChildren()) {

                            for (DataSnapshot temp : s.getChildren()) {
                                dbReference.child("Sub").child(s.getKey()).child("posts").child(temp.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            Post post = dataSnapshot.getValue(Post.class);
                                            if (post != null) {
                                                post.setKey(dataSnapshot.getKey());
                                                ptemp.add(post);
                                            }

                                        } catch (DatabaseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
                Thread.sleep(1000);
            }while(ptemp.size() == 0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        HideProgressDialog();
        //Collections.shuffle(ptemp);
        listView = (ListView) myView.findViewById(R.id.profile_list_view);
        MyPostAdapter adapter = new MyPostAdapter(mActivity, ptemp);
        listView.setAdapter(adapter);
    }






}
