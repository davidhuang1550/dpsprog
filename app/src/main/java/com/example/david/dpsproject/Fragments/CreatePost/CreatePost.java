package com.example.david.dpsproject.Fragments.CreatePost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.CreatePostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by david on 2016-10-27.
 */

public class CreatePost extends Fragment implements View.OnClickListener, Parcelable,CreatePostInterface {
    View myView;
    Button moreOptions;
    Button post_button;
    String sub_cat;
    TextView title;
    TextView desc;
    Activity mActivity;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;


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
        mActivity.setTitle("Create Post");
        myView = inflater.inflate(R.layout.create_post,container,false);
        post_button = (Button)myView.findViewById(R.id.post_button_upload);

        moreOptions = (Button)myView.findViewById(R.id.moreoptionsbutton);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.compose);
        if(fab!=null)fab.hide(); // hide it in the create post area

        post_button.setOnClickListener(this);
        moreOptions.setOnClickListener(this);
        return myView;
    }

    public void onClick(View v) {
        if(v.getId()== R.id.post_button_upload) {
            Post post=checkvalidation();
            if(post!=null){
                post.setYesNo();
                CreatePostPresenter createPostPresenter= createpost(post);
                createPostPresenter.createPost();
            }

        }

        else if(v.getId()==R.id.moreoptionsbutton){
            FrameLayout frameLayout = (FrameLayout)myView.findViewById(R.id.replaceable_frame);
            frameLayout.removeAllViews();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Fragment",(Parcelable) this); // not serializable
            bundle.putString("Image","false");
            CreatePostMoreOptions createPostMoreOptions= new CreatePostMoreOptions();
            createPostMoreOptions.setArguments(bundle);
            FragmentManager fragmentManager = mActivity.getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.replaceable_frame,createPostMoreOptions).commit();
        }

    }

    public Post checkvalidation() {
        TextView sub_cat_view = (TextView) myView.findViewById(R.id.sub_post);
        sub_cat = sub_cat_view.getText().toString();
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
        title = (TextView) myView.findViewById(R.id.title_post);
        desc = (TextView) myView.findViewById(R.id.description_post);


        if (sub_cat.trim().length() > 0 && title.getText().toString().trim().length() > 0 && desc.getText().toString().trim().length() > 0) {
        } else {
            Toast.makeText(getActivity(), "Every Field Must not be empty", Toast.LENGTH_SHORT).show();
            return null;
        }
        Post post = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                title.getText().toString(), desc.getText().toString(), System.currentTimeMillis() / 1000, sub_cat);
        return post;
    }
    public CreatePostPresenter createpost(Post post){
        return new CreatePostPresenter(post, mActivity, dataBaseConnectionsPresenter, sub_cat, ((navigation) mActivity).getworkingUser(), this);
    }

}
