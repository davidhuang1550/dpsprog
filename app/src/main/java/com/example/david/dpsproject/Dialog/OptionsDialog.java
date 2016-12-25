package com.example.david.dpsproject.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.david.dpsproject.R;

import java.util.zip.Inflater;

/**
 * Created by david on 2016-12-22.
 */

public class OptionsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder optionDialog = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(),R.layout.dialogoptions,null);

        Button buttonUser = (Button)view.findViewById(R.id.SearchUser);
        Button buttonPost = (Button)view.findViewById(R.id.SearchPost);
        Button buttonSub= (Button)view.findViewById(R.id.SearchSub);

        final Bundle bundle = new Bundle();

        final SearchDialog searchDialog = new SearchDialog();

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("Key","Post");
                searchDialog.setArguments(bundle);
                searchDialog.show(getFragmentManager(),"options Dialog");
                dismiss();
            }
        });
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("Key","Sub");
                searchDialog.setArguments(bundle);
                searchDialog.show(getFragmentManager(),"options Dialog");
                dismiss();
            }
        });
        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("Key","User");
                searchDialog.setArguments(bundle);
                searchDialog.show(getFragmentManager(),"options Dialog");
                dismiss();
            }
        });


        optionDialog.setView(view);

        return optionDialog.create();

    }
}
