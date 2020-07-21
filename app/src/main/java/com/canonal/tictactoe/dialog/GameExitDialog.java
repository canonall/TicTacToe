package com.canonal.tictactoe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.GameExitDialogListener;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.Player;

public class GameExitDialog extends DialogFragment {

    private Context context;
    private GameExitDialogListener gameExitDialogListener;
    private ActiveGame activeGame;


    public GameExitDialog(Context context, ActiveGame activeGame) {
        this.context = context;
        this.activeGame = activeGame;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(getString(R.string.exit_game_message))

                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameExitDialogListener.stayGame();
                    }
                })

                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        gameExitDialogListener.exitGame(activeGame);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            gameExitDialogListener = (GameExitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement GameExitDialogListener");
        }
    }
}
