package com.adityay.sachintons.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adityay.sachintons.R;
import com.adityay.sachintons.customviews.CustomImageView;
import com.adityay.sachintons.databinding.ActivitySplashBinding;
import com.adityay.sachintons.receivers.ConnectionReceiver;



public class SplashActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener, View.OnClickListener {

//    @BindView(R.id.tv_splash_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_splash)
//    ImageView ivSplash;

    private ConnectionReceiver connectionReceiver;
    private Dialog dialog;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //ButterKnife.bind(this);
        dialog = new Dialog(this);
        setSplashDecor();
    }

    private void setSplashDecor() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSplashTitle();
        animateViews(binding.ivSplash);
        animateViews(binding.tvSplashTitle);
    }

    private void receiverRegister() {
        connectionReceiver = new ConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void animateViews(View view) {
        ObjectAnimator  viewFadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        viewFadeIn.setDuration(1000);
        viewFadeIn.start();
    }

    private void setSplashTitle() {
        Shader textShader = new LinearGradient(0, 0, binding.tvSplashTitle.getWidth(), binding.tvSplashTitle.getTextSize(),
                Color.parseColor("#fb8c00"), Color.parseColor("#689f38"), Shader.TileMode.CLAMP);
        binding.tvSplashTitle.getPaint().setShader(textShader);
    }

    private void showSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected){
            showNoInternetDialog();
        }else{
            if(dialog != null  && dialog.isShowing()){
                dialog.cancel();
            }
            showSplashScreen();
        }
    }

    private void showNoInternetDialog() {
        dialog.setContentView(R.layout.error_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        CustomImageView ivError = dialog.findViewById(R.id.iv_error);
        ivError.setGifImageWithGlide(this, R.raw.no_internet);
        ivError.setScaleType(ImageView.ScaleType.FIT_XY);
        Button btnRetry = dialog.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiverRegister();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectionReceiver);
        dialog.cancel();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Please connect to Internet !!", Toast.LENGTH_SHORT).show();
        recreate();
    }
}
