package com.adityay.sachintons.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.adityay.sachintons.R;
import com.adityay.sachintons.databinding.ActivityMainBinding;
import com.adityay.sachintons.fragments.MainFragment;
import com.adityay.sachintons.receivers.ConnectionReceiver;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.ll_main)
//    ViewGroup llMainLayout;

    private ConnectionReceiver connectionReceiver;
    private int flag = 0;
    private MainFragment mainFragment = new MainFragment();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setElevation(0);
        loadFragment();
    }

    private void receiverRegister() {
        connectionReceiver = new ConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .commit();
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
        flag = 0;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected){
            flag = 1;
            Snackbar.make(binding.llMain, "No Internet Connection", Snackbar.LENGTH_INDEFINITE).show();
        }else{
            if(flag == 1){
                Snackbar snackbar = Snackbar.make(binding.llMain, "Connected To Internet", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.green));
                snackbar.show();
                mainFragment.reloadFragment();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment instanceof MainFragment){
            if(((MainFragment) fragment).getViewPager().getCurrentItem() != 0){
                ((MainFragment) fragment).getViewPager().setCurrentItem(0);
            }else{
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }
    }
}
