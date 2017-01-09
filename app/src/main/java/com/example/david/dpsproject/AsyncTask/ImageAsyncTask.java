package com.example.david.dpsproject.AsyncTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Fragments.CreatePost.CreatePostImage;
import com.example.david.dpsproject.Presenter.CreatePostPresenter;
import com.example.david.dpsproject.navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by david on 2017-01-04.
 */

public class ImageAsyncTask extends AsyncTask<Void,Void,Void> {
    private Activity mActivity;
    private Bitmap bitMap;
    private DatabaseReference dbReference;
    private Post post;
    private String title;
    private String sub_cat;
    private CreatePostImage createPostImage;
    private boolean yesno;

    public ImageAsyncTask(Activity activity, DatabaseReference databaseReference, CreatePostImage create,Post p){
        mActivity=activity;
        dbReference=databaseReference;
        createPostImage=create;
        post=p;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitMap = BitmapFactory.decodeFile(((navigation) mActivity).getFilePath(), options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dpsproject-85e85.appspot.com/Images/");
        DatabaseReference dbImageRef= dbReference.child("Image").push();
        dbImageRef.setValue(new String("0"));
        StorageReference ImageRef = storageRef.child(dbImageRef.getKey());
        UploadTask uploadTask = ImageRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
        //String posterId,String title,Long T,String image,String sn
        post.setImage(dbImageRef.getKey());
        createPostImage.setPost(post);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(post!=null) {
            CreatePostPresenter createPostPresenter = createPostImage.createpost(post);
            createPostPresenter.createPost();
        }
    }
}
