package com.canonal.tictactoe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.listener.UsernameDialogListener;

public class GameInviteDialog extends DialogFragment {

    private Context context;
    private GameInviteDialogListener gameInviteDialogListener;
    private boolean isInviteAccepted;

    public GameInviteDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_game_invite, null);

       // builder.setView(view)

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            gameInviteDialogListener = (GameInviteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement GameInviteDialogListener");
        }
    }
}
