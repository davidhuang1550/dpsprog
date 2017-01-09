package com.example.david.dpsproject.Fragments.CreatePost;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.AsyncTask.ImageAsyncTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.CreatePostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 2017-01-02.
 */

public class CreatePostMoreOptions extends Fragment implements View.OnClickListener{
    private Activity mActivity;
    private View myView;
    private Button addMore;
    private Button submit;
    private TableLayout tableLayout;
    private ArrayList<EditText> newEditText;
    private int responseNum;
    private Post post;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private CreatePostInterface fragment;
    private boolean isImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            fragment = (CreatePostInterface) bundle.getParcelable("Fragment");
            isImage=(bundle.getString("Image").equals("true"))?true:false;
        }
        myView = inflater.inflate(R.layout.multipleanswerpostview,container,false);
        addMore = (Button)myView.findViewById(R.id.addMore);
        submit = (Button)myView.findViewById(R.id.submitpost);
        tableLayout= (TableLayout)myView.findViewById(R.id.layouttable);
        addMore.setOnClickListener(this);
        submit.setOnClickListener(this);
        responseNum=0;
        newEditText= new ArrayList<>();
        EditText editText1 = (EditText)myView.findViewById(R.id.field0);
        EditText editText2 = (EditText)myView.findViewById(R.id.field1);
        newEditText.add(editText1);
        newEditText.add(editText2);

        return myView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.addMore){
            if((responseNum)<13) {
                tableLayout.addView(AddRow(), (responseNum + 2));
                responseNum++;
            }
            else{
                Toast.makeText(mActivity,"Max Options is 15",Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId()==R.id.submitpost){
            post =fragment.checkvalidation();
            if(post!=null) {
                HashMap<String, Integer> hashMap = new HashMap<>();
                boolean go = true;
                for (EditText et : newEditText) {
                    if (et.getText().toString().trim().length() > 0) {
                        hashMap.put(et.getText().toString(), 0);
                    } else {
                        Toast.makeText(mActivity, "please fill in all rows", Toast.LENGTH_SHORT).show();
                        go = false;
                        break;
                    }
                }
                if (go){
                    post.setPiechart(hashMap);
                    if(isImage){
                        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
                        final ImageAsyncTask CreateImagePost= new ImageAsyncTask(mActivity, dataBaseConnectionsPresenter.getDbReference(),(CreatePostImage) fragment,post);
                        CreateImagePost.execute();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(CreateImagePost.getStatus()== AsyncTask.Status.RUNNING){
                                    Toast.makeText(mActivity,"Something went wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },5000);
                    }
                    else {
                        CreatePostPresenter createPostPresenter = fragment.createpost(post);
                        createPostPresenter.createPost();
                    }
                }
            }
        }
    }
    public TableRow AddRow(){
        TableRow tr = new TableRow(mActivity);
        TextView textView = new TextView(mActivity);

        textView.setId(mActivity.getResources().getIntArray(R.array.options)[responseNum+2]);
        textView.setPadding(30,30,30,30);
        textView.setText("Option "+(responseNum+3));
        tr.addView(textView);

        EditText editText = new EditText(mActivity);
        editText.setId((mActivity.getResources().getIntArray(R.array.optionsfield)[responseNum+2]));
        editText.setBackgroundResource(R.drawable.postborder);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,20,0);

        tr.addView(editText,params);
        newEditText.add(editText);

        return tr;
    }
}
