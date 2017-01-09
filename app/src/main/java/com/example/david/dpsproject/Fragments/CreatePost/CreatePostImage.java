package com.example.david.dpsproject.Fragments.CreatePost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.david.dpsproject.AsyncTask.ImageAsyncTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.CreatePostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by xlhuang3 on 11/8/2016.
 */
public class CreatePostImage  extends Fragment implements View.OnClickListener,Parcelable,CreatePostInterface {
    private View myView;

    private Button Upload;
    private Button Create;
    private Button moreOptions;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    String sub_cat;
    TextView title;
    Activity mActivity;
    Post post;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity= getActivity();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags){
    }

    public static final Parcelable.Creator<CreatePostImage> CREATOR
            = new Parcelable.Creator<CreatePostImage>() {
        public CreatePostImage createFromParcel(Parcel in) {
            return new CreatePostImage();
        }

        public CreatePostImage[] newArray(int size) {
            return new CreatePostImage[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.create_post_image,container,false);

        FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.compose);
        if(fab!=null)fab.hide(); // hide it in the create post area
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();


        Upload = (Button)myView.findViewById(R.id.upload);
        Create = (Button)myView.findViewById(R.id.post_button_upload);
        moreOptions = (Button)myView.findViewById(R.id.moreoptionsbutton);

        Upload.setOnClickListener(this);
        Create.setOnClickListener(this);
        moreOptions.setOnClickListener(this);


        return myView;

    }

    public Post checkvalidation(){
        TextView sub_cat_view = (TextView) myView.findViewById(R.id.sub_post);
        sub_cat = sub_cat_view.getText().toString();
        title = (TextView) myView.findViewById(R.id.title_post);


        if(((navigation)mActivity).getImageUpload()!=null&& sub_cat.trim().length() > 0 && title.getText().toString().trim().length() > 0) {
            post= new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    title.getText().toString(), System.currentTimeMillis() / 1000, sub_cat);
        } else {
            Toast.makeText(getActivity(), "Every Field Must not be empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        return post;
    }
    public void setPost(Post p){
        post=p;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.upload:
                if(((navigation)mActivity).checkReadExternalPermission()) {
                    try {
                        final Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        mActivity.startActivityForResult(galleryIntent, 1);
                    }catch(RuntimeException e){e.printStackTrace();}
                }else{((navigation)mActivity).requestForSpecificPermission();}
                break;

            case R.id.post_button_upload:
                TextView sub_cat_view = (TextView) myView.findViewById(R.id.sub_post);
                sub_cat = sub_cat_view.getText().toString();
                title = (TextView) myView.findViewById(R.id.title_post);


                if (((navigation)mActivity).checkReadExternalPermission()) {
                    if(checkvalidation()!=null) {
                        post.setYesNo();
                        final ImageAsyncTask CreateImagePost = new ImageAsyncTask(mActivity, dataBaseConnectionsPresenter.getDbReference(), this, post);
                        CreateImagePost.execute();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CreateImagePost.getStatus() == AsyncTask.Status.RUNNING) {
                                    Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 5000);

                    }  else{
                        Toast.makeText(mActivity,"Every Field Must not be empty",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ((navigation)mActivity).requestForSpecificPermission();
                }

                break;
            case R.id.moreoptionsbutton:
            try {
                FrameLayout frameLayout = (FrameLayout) myView.findViewById(R.id.replaceable_frame);
                frameLayout.removeAllViews();
                Bundle bundle = new Bundle();
                bundle.putParcelable("Fragment", (Parcelable) this); // not serializable
                bundle.putString("Image","true");
                CreatePostMoreOptions createPostMoreOptions = new CreatePostMoreOptions();
                createPostMoreOptions.setArguments(bundle);
                FragmentManager fragmentManager = mActivity.getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.replaceable_frame, createPostMoreOptions).commit();
            }catch(Exception e){e.printStackTrace();}
                break;



        }
    }
    public CreatePostPresenter createpost(Post post){
        return new CreatePostPresenter(post, mActivity, dataBaseConnectionsPresenter, sub_cat,
                ((navigation) mActivity).getworkingUser(), this);
    }
}
