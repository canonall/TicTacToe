package com.canonal.tictactoe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.UsernameDialogListener;
import com.canonal.tictactoe.utility.Constants;
import com.canonal.tictactoe.utility.EmptyFieldChecker;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class UsernameDialog extends AppCompatDialogFragment {

    private EditText etUserName;
    private String sharedPrefPlayerName;
    private UsernameDialogListener usernameDialogListener;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public UsernameDialog(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_user_name, null);

        firebaseAuth = FirebaseAuth.getInstance();
        etUserName = view.findViewById(R.id.et_user_name);

        loadData();

        if (!sharedPrefPlayerName.equals(Constants.NO_SHARED_PREF_PLAYER_NAME)) {
            etUserName.setText(sharedPrefPlayerName);
        }

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


                    }
                });


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v){
                    String userId = firebaseAuth.getUid();
                    String username = etUserName.getText().toString();

                    if (EmptyFieldChecker.isFieldEmpty(username)) {
                        EmptyFieldChecker.printEmptyError(etUserName, context);

                    } else {
                        usernameDialogListener.createNewPlayer(userId, username);

                        saveData(username);

                        d.dismiss();
                    }
                }
            });
        }
    }

    private void saveData(String playerName) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.PLAYER_NAME, playerName);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        sharedPrefPlayerName = sharedPreferences.getString(Constants.PLAYER_NAME, Constants.NO_SHARED_PREF_PLAYER_NAME);

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
