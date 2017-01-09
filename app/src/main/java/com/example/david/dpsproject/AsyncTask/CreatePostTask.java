package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Class.Sub;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by david on 2017-01-03.
 */

public class CreatePostTask extends AsyncTask<Void,Void,Void> {
    private ProgressDialog pDialog;
    private Activity mActivity;
    private DatabaseReference dbReference;
    private Fragment fragment;
    private String sub_cat;
    private Post post;
    private Users users;
    private FirebaseUser firebaseUser;

    public CreatePostTask(Activity activity, DatabaseReference dbr,Fragment frag,String sub, Post p, Users u, FirebaseUser fbu,ProgressDialog pd){
        mActivity=activity;
        dbReference=dbr;
        fragment=frag;
        sub_cat=sub;
        users=u;
        firebaseUser=fbu;
        post=p;
        pDialog=pd;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            dbReference.child("Sub").child(sub_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        if (dataSnapshot.getValue() == null) {

                            Sub sub = new Sub();
                            Post first_post = new Post("ADMIN", "FIRST POST OF THE SUB", "", new Long(0), sub_cat); // first one

                            sub.pushPost(first_post);
                            dbReference.child("Sub").child(sub_cat).setValue(sub);
                        }
                        DatabaseReference postref = dbReference.child("Sub").child(sub_cat).child("posts").push();
                        postref.setValue(post);

                        users.addToHashMapPost(sub_cat, postref.getKey());
                        users.IncNumOfPosts();
                        dbReference.child("Users").child(firebaseUser.getUid()).child("Posts").setValue(users.getPosts());
                        dbReference.child("Users").child(firebaseUser.getUid()).child("NumOfPosts").setValue(users.getNumOfPosts());


                        SearchPost searchPost = new SearchPost(sub_cat, post.getTitle());
                        dbReference.child("SubPost").child(postref.getKey()).setValue(searchPost);

                        if (dataSnapshot.getValue() == null)
                            dbReference.child("Sub").child(sub_cat).child("posts").child("0").removeValue(); // remove inital commit

                    } catch (DatabaseException e) {
                        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
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
        pDialog.dismiss();
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        ((navigation) mActivity).showFab();
        fragmentManager.beginTransaction().remove(fragment).commit();
    }
}
