package com.canonal.tictactoe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.model.GameInvite;


public class GameInviteDialog extends DialogFragment {

    private Context context;
    private GameInvite gameInvite;
    private GameInviteDialogListener gameInviteDialogListener;
    private boolean isInviteAccepted;

    public GameInviteDialog(Context context, GameInvite gameInvite) {
        this.context = context;
        this.gameInvite = gameInvite;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_game_invite, null);

        TextView tvInviteText = view.findViewById(R.id.tv_invite_text);
        tvInviteText.setText(context.getResources().getString(R.string.invite_text, gameInvite.getInviter().getPlayer().getUsername()));

        builder.setView(view)

                .setNegativeButton(R.string.reject_game_invite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                    }
                })
                .setPositiveButton(R.string.accept_game_invite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameInviteDialogListener.createMatch(gameInvite.getInvitee().getPlayer(), gameInvite.getInviter().getPlayer());
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            gameInviteDialogListener = (GameInviteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement GameInviteDialogListener");
        }
    }
}
