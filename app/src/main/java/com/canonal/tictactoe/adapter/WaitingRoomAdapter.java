package com.canonal.tictactoe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.Player;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class WaitingRoomAdapter extends RecyclerView.Adapter<WaitingRoomAdapter.ViewHolder> {

    private Set<Player> playerSet;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public WaitingRoomAdapter(Set<Player> playerSet, Context context, OnItemClickListener onItemClickListener) {
        this.playerSet = playerSet;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rv_waiting_room, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //convert set to list
        List<Player> playerList = new ArrayList<>(playerSet);

        String username = playerList.get(position).getUsername();
        holder.tvPlayerName.setText(username);

    }

    @Override
    public int getItemCount() {
        return playerSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPlayerName;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            tvPlayerName = itemView.findViewById(R.id.tv_player_name);

            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
