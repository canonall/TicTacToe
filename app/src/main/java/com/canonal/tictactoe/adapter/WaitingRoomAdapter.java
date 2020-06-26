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
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class WaitingRoomAdapter extends RecyclerView.Adapter<WaitingRoomAdapter.ViewHolder> {

    private List<Player> playerList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public WaitingRoomAdapter(List<Player> playerList, Context context, OnItemClickListener onItemClickListener) {
        this.playerList = playerList;
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
        String username = playerList.get(position).getUsername();
        holder.tvPlayerName.setText(username);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
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
