package com.canonal.tictactoe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.UsernameDialogListener;
import com.google.firebase.auth.FirebaseAuth;

public class UsernameDialog extends AppCompatDialogFragment {

    private EditText etUserName;
    private UsernameDialogListener usernameDialogListener;
    private FirebaseAuth firebaseAuth;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_user_name, null);

        firebaseAuth = FirebaseAuth.getInstance();
        etUserName = view.findViewById(R.id.et_user_name);


        builder.setView(view)
                .setTitle(getResources().getString(R.string.user_name))

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Return to main menu
                        getActivity().finish();

                    }
                })

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String userId = firebaseAuth.getUid();
                        String username = etUserName.getText().toString();

                        usernameDialogListener.getUserInfo(userId,username);

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            usernameDialogListener = (UsernameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement UsernameDialogListener");
        }
    }
}
