package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    private Animation rotateClockwiseAnimation;
    private Animation startFromMiddle;
    private Animation translateToTop;
    private Animation startFromBottom;
    private Animation translateFromBottom;

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_online)
    TextView tvOnline;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.rl_btn_container)
    RelativeLayout rlBtnContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: AdMob Initiate ");
            }
        });

        rotateClockwiseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation_clockwise);
        startFromMiddle = AnimationUtils.loadAnimation(this, R.anim.start_from_middle);
        translateToTop = AnimationUtils.loadAnimation(this, R.anim.translate_to_top);
        startFromBottom = AnimationUtils.loadAnimation(this, R.anim.start_from_bottom);
        translateFromBottom = AnimationUtils.loadAnimation(this, R.anim.translate_from_bottom);

        AnimationSet animationSetLogo = new AnimationSet(true);
        AnimationSet animationSetRelativeLayout = new AnimationSet(true);

        animationSetLogo.addAnimation(rotateClockwiseAnimation);
        animationSetLogo.addAnimation(startFromMiddle);
        animationSetLogo.addAnimation(translateToTop);

        animationSetRelativeLayout.addAnimation(startFromBottom);
        animationSetRelativeLayout.addAnimation(translateFromBottom);

        ivLogo.startAnimation(animationSetLogo);
        rlBtnContainer.startAnimation(animationSetRelativeLayout);


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