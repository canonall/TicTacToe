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
    private Animation startLogoMiddle;
    private Animation translateLogoUp;
    private Animation startButtonsBottom;
    private Animation translateButtonsUp;


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

        loadAnimations();

        AnimationSet animationSetLogo = new AnimationSet(true);
        animationSetLogo.addAnimation(rotateClockwiseAnimation);
        animationSetLogo.addAnimation(startLogoMiddle);
        animationSetLogo.addAnimation(translateLogoUp);

        AnimationSet animationSetRelativeLayout = new AnimationSet(true);
        animationSetRelativeLayout.addAnimation(startButtonsBottom);
        animationSetRelativeLayout.addAnimation(translateButtonsUp);

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

    private void loadAnimations() {
        rotateClockwiseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation_clockwise);
        startLogoMiddle = AnimationUtils.loadAnimation(this, R.anim.start_logo_middle);
        startButtonsBottom = AnimationUtils.loadAnimation(this, R.anim.start_buttons_bottom);
        translateLogoUp = AnimationUtils.loadAnimation(this, R.anim.translate_logo_up);
        translateButtonsUp = AnimationUtils.loadAnimation(this, R.anim.translate_buttons_up);
    }
}