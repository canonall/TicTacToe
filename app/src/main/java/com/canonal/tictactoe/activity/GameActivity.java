package com.canonal.tictactoe.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.canonal.tictactoe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {


    @BindView(R.id.btn_00)
    Button btn00;
    @BindView(R.id.btn_01)
    Button btn01;
    @BindView(R.id.btn_02)
    Button btn02;
    @BindView(R.id.btn_10)
    Button btn10;
    @BindView(R.id.btn_11)
    Button btn11;
    @BindView(R.id.btn_12)
    Button btn12;
    @BindView(R.id.btn_20)
    Button btn20;
    @BindView(R.id.btn_21)
    Button btn21;
    @BindView(R.id.btn_22)
    Button btn22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_00)
    public void onBtn00Clicked() {
        click(btn00);
    }

    @OnClick(R.id.btn_01)
    public void onBtn01Clicked() {
        click(btn01);
    }

    @OnClick(R.id.btn_02)
    public void onBtn02Clicked() {
        click(btn02);
    }

    @OnClick(R.id.btn_10)
    public void onBtn10Clicked() {
        click(btn10);
    }

    @OnClick(R.id.btn_11)
    public void onBtn11Clicked() {
        click(btn11);
    }

    @OnClick(R.id.btn_12)
    public void onBtn12Clicked() {
        click(btn12);
    }

    @OnClick(R.id.btn_20)
    public void onBtn20Clicked() {
        click(btn20);
    }

    @OnClick(R.id.btn_21)
    public void onBtn21Clicked() {
        click(btn21);
    }

    @OnClick(R.id.btn_22)
    public void onBtn22Clicked() {
        click(btn22);
    }

    private void click(Button btn) {
        btn.setText(getResources().getString(R.string.x));

    }
}