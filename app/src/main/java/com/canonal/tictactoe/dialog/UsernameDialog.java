package com.canonal.tictactoe.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.UsernameDialogListener;
import com.canonal.tictactoe.utility.Constants;
import com.canonal.tictactoe.utility.operator.TextOperator;
import com.canonal.tictactoe.utility.operator.SharedPreferenceOperator;
import com.google.firebase.auth.FirebaseAuth;

public class UsernameDialog extends AppCompatDialogFragment {

    private EditText etUserName;
    private UsernameDialogListener usernameDialogListener;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public UsernameDialog(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_user_name, null);

        etUserName = view.findViewById(R.id.et_user_name);

        //load previous userName from SharedPreference
        String sharedPrefPlayerName = SharedPreferenceOperator.loadData(context);

        //if SharedPreference playerName  not null, then set it to editText
        if (!sharedPrefPlayerName.equals(Constants.NO_SHARED_PREF_PLAYER_NAME)) {
            etUserName.setText(sharedPrefPlayerName);
        }

        builder.setView(view)

                .setTitle(getResources().getString(R.string.user_name))

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Return to main menu


                    }
                })

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //This method is overridden in onResume due to custom handling
                    }
                });


        return builder.create();
    }

    //onStart() is where dialog.show() is actually called on
    //the underlying dialog, so we have to do it there or
    //later in the lifecycle.
    //Doing it in onResume() makes sure that even if there is a config change
    //environment that skips onStart then the dialog will still be functioning
    //properly after a rotation.
    @Override
    public void onResume() {
        super.onResume();

        //Check whether userName is empty, if it is empty
        //don't close the dialog, if not save userName to SharedPreference
        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {

            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.colorAccent));
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userId = firebaseAuth.getUid();
                    String username = etUserName.getText().toString();

                    if (TextOperator.isFieldEmpty(username)) {
                        TextOperator.printEmptyFieldError(etUserName, context);

                    } else {
                        usernameDialogListener.createNewPlayer(userId, username);
                        SharedPreferenceOperator.saveData(username, context);
                        dialog.dismiss();
                    }
                }
            });

            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usernameDialogListener.cancelUsernameDialog();
                    dialog.dismiss();

                }
            });
        }
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
