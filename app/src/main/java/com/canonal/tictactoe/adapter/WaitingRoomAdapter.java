package com.canonal.tictactoe.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.listener.RvWaitingRoomItemClickListener;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Invitee;
import com.canonal.tictactoe.model.Inviter;
import com.canonal.tictactoe.model.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class WaitingRoomAdapter extends RecyclerView.Adapter<WaitingRoomAdapter.ViewHolder> {

    private Set<Player> playerSet;
    private List<Player> playerList;
    private Player myPlayer;
    private LayoutInflater layoutInflater;
    private RvWaitingRoomItemClickListener rvWaitingRoomItemClickListener;

    public WaitingRoomAdapter(Set<Player> playerSet, Player myPlayer, Context context, RvWaitingRoomItemClickListener rvWaitingRoomItemClickListener) {
        this.playerSet = playerSet;

        //convert set to list to bind views easily
        this.playerList = new ArrayList<>(playerSet);

        this.myPlayer = myPlayer;
        this.layoutInflater = LayoutInflater.from(context);
        this.rvWaitingRoomItemClickListener = rvWaitingRoomItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rv_waiting_room, parent, false);
        return new ViewHolder(view, rvWaitingRoomItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Don't show invite button next to my player name
        if (myPlayer.getUserId().equals(playerList.get(position).getUserId())) {
            holder.tvInvite.setVisibility(View.INVISIBLE);
        }

        String username = playerList.get(position).getUsername();
        holder.tvPlayerName.setText(username);

    }

    @Override
    public int getItemCount() {
        return playerSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPlayerName;
        TextView tvInvite;
        RvWaitingRoomItemClickListener rvWaitingRoomItemClickListener;
        Resources resources;

        public ViewHolder(View itemView, RvWaitingRoomItemClickListener rvWaitingRoomItemClickListener) {
            super(itemView);

            tvPlayerName = itemView.findViewById(R.id.tv_player_name);
            tvInvite = itemView.findViewById(R.id.tv_invite);
            resources = layoutInflater.getContext().getResources();

            this.rvWaitingRoomItemClickListener = rvWaitingRoomItemClickListener;
            itemView.setOnClickListener(this);

            tvInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Invitee inviteePlayer = getInviteePlayer();
                    Inviter inviterPlayer = getInviterPlayer();
                    GameInvite gameInvite = getGameInvite(inviteePlayer, inviterPlayer);


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child(resources.getString(R.string.path_gameInvite));

                    //todo
                    //addOnSuccessListener


                    databaseReference.child(gameInvite.getInvitee().getPlayer().getUserId())
                            .setValue(gameInvite);

                    Log.d("INVITE POSITION", "onClick: invitee player " + inviteePlayer.getPlayer().getUserId() + " " + inviteePlayer.getPlayer().getUsername());
                    Log.d("INVITE POSITION", "onClick: inviter player " + inviterPlayer.getPlayer().getUserId() + " " + inviterPlayer.getPlayer().getUsername());
                    Log.d("INVITE", "onClick:INVITEE");

                }
            });

        }

        @Override
        public void onClick(View view) {
            rvWaitingRoomItemClickListener.onRvItemClick(getAdapterPosition());
        }

        private Invitee getInviteePlayer() {

            Invitee inviteePlayer = new Invitee();

            Player player = new Player();
            String inviteePlayerId = playerList.get(getAdapterPosition()).getUserId();
            String inviteePlayerName = playerList.get(getAdapterPosition()).getUsername();

            player.setUserId(inviteePlayerId);
            player.setUsername(inviteePlayerName);

            inviteePlayer.setPlayer(player);
            return inviteePlayer;

        }

        private Inviter getInviterPlayer() {

            Inviter inviterPlayer = new Inviter();
            inviterPlayer.setPlayer(myPlayer);
            return inviterPlayer;

        }

        private GameInvite getGameInvite(Invitee invitee, Inviter inviter) {
            GameInvite gameInvite = new GameInvite();
            gameInvite.setInvitee(invitee);
            gameInvite.setInviter(inviter);
            return gameInvite;
        }
    }
}
