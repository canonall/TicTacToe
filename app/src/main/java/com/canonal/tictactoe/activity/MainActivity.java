package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.canonal.tictactoe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_online)
    TextView tvOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_start)
    public void onTvStartClicked() {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }

    @OnClick(R.id.tv_about)
    public void onTvAboutClicked() {
    }

    @OnClick(R.id.tv_online)
    public void onTvOnlineClicked() {
        startActivity(new Intent(MainActivity.this, OnlineGameActivity.class));
    }
}