package com.example.david.dpsproject.Fragments.ViewPost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by david on 2016-11-03.
 */
public class postview extends Fragment  {
    private TextView Title;
    private TextView Description;
    private View myView;
    private Activity mActivity;
    Post post;
    private Uri imagefile;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((navigation)mActivity).hideFab();
        bundle = getArguments();
        post = (Post) bundle.getSerializable("Post_Object");
        imagefile=null;
        if(post.getImage()!=null&& (!post.getImage().equals(""))){
            myView =inflater.inflate(R.layout.fragment_postview_picture,container,false);
            final ImageView imageView = (ImageView)myView.findViewById(R.id.imageView);
            try {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://dpsproject-85e85.appspot.com/Images/" + post.getImage());
                storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Picasso.with(mActivity).load(task.getResult()).fit().into(imageView);
                        imagefile=task.getResult();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity, "Failed to download image", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){e.printStackTrace();}

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("URI",imagefile.toString());

                    ZoomInFragment zoomInFragment = new ZoomInFragment();
                    zoomInFragment.setArguments(bundle);


                    FragmentManager fragmentManager = mActivity.getFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.content_frame,zoomInFragment).commit();

                }
            });
        }
        else{
            myView = inflater.inflate(R.layout.fragment_postview, container, false);
            Description = (TextView) myView.findViewById(R.id.PostDesc);
            Description.setText(post.getDescription());
        }



        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        Title = (TextView) myView.findViewById(R.id.PostTitle);
        Title.setText(post.getTitle());

        Users users =((navigation)mActivity).getworkingUser();
        if (users != null) {

            FragmentManager fragmentManager = mActivity.getFragmentManager();
            boolean go_post_view_button = users.findArrayListPost(post.getSubN(),post.getKey());
            if (go_post_view_button) {
                if(post.getPiechart()==null) {
                    post_view_button pv = new post_view_button();
                    pv.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.replaceable_frame, pv).commit();
                }
                else{
                    MultiOptionView multiOptionView = new MultiOptionView();
                    multiOptionView.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.replaceable_frame,multiOptionView).commit();
                }
            } else {
                if(post.getPiechart()!=null){
                    PieChartFragment pieChartFragment = new PieChartFragment();
                    pieChartFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.replaceable_frame,pieChartFragment).commit();
                }
                else{
                    VoteBarFrame voteBarFrame = new VoteBarFrame();
                    bundle.putInt("yes", post.getYes());
                    bundle.putInt("no", post.getNo());
                    voteBarFrame.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.replaceable_frame, voteBarFrame).commit();
                }

            }


        }else{
            LoginPostView loginPostView= new LoginPostView();
            final FragmentManager fragmentManager = mActivity.getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.replaceable_frame,loginPostView).commit();
        }
        return myView;
    }

}
