package com.example.david.dpsproject.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Fragments.SearchUser;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.Fragments.Searchpage;

/**
 * Created by xlhuang3 on 11/8/2016.
 */
public class SearchDialog extends DialogFragment {
        private String key;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            key="";
//        alertDialog.setMessage("You must be logged in to create post");

        final EditText editText = new EditText(getActivity());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        alertDialog.setView(editText);
        final Bundle arg = getArguments();
        if(arg!=null){
            key= arg.getString("Key");
            alertDialog.setTitle("Search "+ key);
        }
        alertDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!(editText.getText().toString().equals(""))) {

                    if (!key.equals("")) {
                        Bundle bundle = new Bundle();
                        if(key.equals("Post")){

                        }
                        else if(key.equals("User")){
                            SearchUser searchUser = new SearchUser();
                            bundle.putString("username",editText.getText().toString());
                            searchUser.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, searchUser).commit();
                        }
                        else if(key.equals("Sub")){
                            Searchpage searchpage = new Searchpage();
                            bundle.putString("Sub", editText.getText().toString());
                            searchpage.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, searchpage).commit();
                        }
                    }

                }
                else{
                    Toast.makeText(getActivity(),"Nothing was Searched",Toast.LENGTH_SHORT).show();
                   // editText.setHint("Intput Some Characters");
                  //  alertDialog.setView(editText);
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setShowsDialog(false);
                dismiss();
            }
        });



        return alertDialog.create();
    }


}
