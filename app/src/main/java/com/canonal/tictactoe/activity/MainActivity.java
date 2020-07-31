package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.canonal.tictactoe.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";


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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: ADMOB Initiliza ");
            }
        });
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
        startActivity(new Intent(MainActivity.this, WaitingRoomActivity.class));
    }
}